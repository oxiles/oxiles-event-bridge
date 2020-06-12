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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class KabutoSDK {

    private ObjectMapper objectMapper;
    private String kabutoUrl;
    private final ScheduledExecutorService scheduledExecutorService;
    private final long pollingInterval;
    private OkHttpClient okHttpClient;
    private ContractTransactionListener contractTransactionListener;
    private String apiKey;

    public KabutoSDK(ObjectMapper objectMapper, OkHttpClient ok, String kabutoUrl, ScheduledExecutorService scheduledExecutorService, long pollingInterval, ContractTransactionListener txListener, String apiKey) {
        this.okHttpClient = ok;
        this.objectMapper = objectMapper;
        this.kabutoUrl = kabutoUrl;
        this.scheduledExecutorService = scheduledExecutorService;
        this.pollingInterval = pollingInterval;
        this.contractTransactionListener = txListener;
        this.apiKey = apiKey;
    }

    public Disposable transactionFlowable(String contractId){
        return kabutoTransactionFlowable(contractId).subscribe(response -> {
            response.forEach( transactionData -> {
                log.info(transactionData.id);
            });
        });
    }

    private Flowable<List<KabutoTransactionData>> kabutoTransactionFlowable(String contractId) {
        return Flowable.create((subscriber) -> {
            subscriber.getClass();
            this.run(subscriber, contractId);
        }, BackpressureStrategy.BUFFER);
    }

    private <T> void run(FlowableEmitter<? super T> emitter, String contractiId) {

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(kabutoUrl + "/transaction").newBuilder();
                urlBuilder.addQueryParameter("q", "{\"type\": \"CONTRACT_CALL\", \"entity\": \""+ contractiId + "\"}\n");
                String url = urlBuilder.build().toString();


                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("accept", "application/json")
                        .get()
                        .build();

                Unchecked.supplier(() -> {

                    try (Response response = this.okHttpClient.newCall(request).execute()) {

                        if (response.code() == 200) {
                            List<KabutoTransactionData> transactionList = new ArrayList<>();

                            try {

                               transactionList = this.objectMapper.reader().forType(new TypeReference<ArrayList<KabutoTransactionData>>
                                        () {
                                }).withRootName("transactions").readValue(response.body().string());

                                transactionList.forEach(
                                    Unchecked.consumer(tx -> {
                                        HashGraphTransactionData data = HashGraphTransactionData.factory(tx);
                                        log.info("New Transaction id detected: {}",data.id);
                                        contractTransactionListener.onTransaction(data);
                                }));
                            }
                            catch(Exception e){
                                log.error("Error getting tx data", e);
                            }

                            return  transactionList;

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
