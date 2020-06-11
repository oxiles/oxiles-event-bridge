package io.oxiles.chain.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.oxiles.chain.service.strategy.DragonGlassSDK;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;

public class DragonGlassSDKTest {

    private DragonGlassSDK underTest;

    @Before
    public void init() throws IOException {

        Logger fooLogger = (Logger) LoggerFactory.getLogger(DragonGlassSDK.class);

        OkHttpClient client = new OkHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.underTest = new DragonGlassSDK(objectMapper, client.newBuilder().build(),"https://api.dragonglass.me/hedera/api",
                Executors.newScheduledThreadPool(1), 10000, null);
    }

    @Test
    public void testDragonGlass() throws  InterruptedException {

        underTest.transactionFlowable("0.0.46764");
        Thread.sleep(20000);
    }



}
