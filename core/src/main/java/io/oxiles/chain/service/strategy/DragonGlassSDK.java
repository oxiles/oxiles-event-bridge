package io.oxiles.chain.service.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import io.oxiles.chain.hashgraph.ContractTransactionListener;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jooq.lambda.Unchecked;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DragonGlassSDK {

    private ObjectMapper objectMapper;
    private String dragonGlassUrl;
    private final ScheduledExecutorService scheduledExecutorService;
    private final long pollingInterval;
    private OkHttpClient okHttpClient;
    private ContractTransactionListener contractTransactionListener;

    public DragonGlassSDK(ObjectMapper objectMapper, OkHttpClient ok, String dragonGlassUrl, ScheduledExecutorService scheduledExecutorService, long pollingInterval, ContractTransactionListener txListener) {
        this.okHttpClient = ok;
        this.objectMapper = objectMapper;
        this.dragonGlassUrl = dragonGlassUrl;
        this.scheduledExecutorService = scheduledExecutorService;
        this.pollingInterval = pollingInterval;
        this.contractTransactionListener = txListener;

    }

    public Disposable transactionFlowable(String contractId){
        return dragonGlassTransactionFlowable(contractId).subscribe(response -> {
            response.forEach( transactionData -> {
                log.info(transactionData.id);
            });
        });
    }

    private Flowable<List<DragonGlassTransactionData>> dragonGlassTransactionFlowable(String contractId) {
        return Flowable.create((subscriber) -> {
            subscriber.getClass();
            this.run(subscriber, contractId);
        }, BackpressureStrategy.BUFFER);
    }

    private <T> void run(FlowableEmitter<? super T> emitter, String contractiId) {

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(dragonGlassUrl + "/transactions").newBuilder();
                urlBuilder.addQueryParameter("contractId", contractiId);
                String url = urlBuilder.build().toString();


                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("X-API-KEY", "b04c8155-1cc9-3b4d-93e8-e9ee2c8e65d3")
                        .get()
                        .build();

                Unchecked.supplier(() -> {

                    try (Response response = this.okHttpClient.newCall(request).execute()) {

                        if (response.code() == 200) {

                            DragonGlassTransactionMain transactionMain = this.objectMapper.reader().forType( new TypeReference<DragonGlassTransactionMain>
                                    (){}).withRootName("data").readValue(response.body().string());

                            transactionMain.data.forEach( tx -> {
                                HashGraphTransactionData data = HashGraphTransactionData.factory(tx);
                                log.info("New Transaction id detected: {}",data.id);
                                contractTransactionListener.onTransaction(data);
                            });

                            return  transactionMain.data;

                        }

                        if (response.code() == 400) {
                            log.error("Invalid request");
                            return null;

                        }

                        throw new Exception("Invalid Request");

                    }

                }).get();
            } catch (Throwable var3) {
                log.error("Error sending request", var3);
            }

        }, 0L, pollingInterval, TimeUnit.MILLISECONDS);
    }

}
