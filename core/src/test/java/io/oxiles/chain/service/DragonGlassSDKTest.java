package io.oxiles.chain.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.oxiles.chain.hashgraph.ContractTransactionListener;
import io.oxiles.chain.service.strategy.DragonGlassSDK;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

public class DragonGlassSDKTest {

    private DragonGlassSDK underTest;
    ContractTransactionListener txListener;

    @Before
    public void init() throws IOException {

        Logger fooLogger = (Logger) LoggerFactory.getLogger(DragonGlassSDK.class);

        OkHttpClient client = new OkHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        txListener = mock(ContractTransactionListener.class);

        this.underTest = new DragonGlassSDK(objectMapper, client.newBuilder().build(),"https://api.dragonglass.me/hedera/api",
                Executors.newScheduledThreadPool(1), 10000, txListener, "b611ab4e-37ef-3dc7-9831-a01091d6a2e5");

    }

    @Test
    public void testDragonGlassTransaction() throws  InterruptedException {

        underTest.transactionFlowable("0.0.46764");
        Thread.sleep(10000);
        verify(txListener, atLeastOnce());

    }


    @Test
    public void testDragonGlassTokenTransfer() throws  InterruptedException {

        underTest.tokenTransferFlowable("0.0.107612");
        Thread.sleep(20000);
        verify(txListener, atLeastOnce());
    }


}
