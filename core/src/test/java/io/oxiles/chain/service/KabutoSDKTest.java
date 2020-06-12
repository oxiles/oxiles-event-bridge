package io.oxiles.chain.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.oxiles.chain.service.strategy.KabutoSDK;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;

@Ignore
public class KabutoSDKTest {



    private KabutoSDK underTest;


    @Before
    public void init() throws IOException {

        Logger fooLogger = (Logger) LoggerFactory.getLogger(KabutoSDK.class);

        OkHttpClient client = new OkHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.underTest = new KabutoSDK(objectMapper, client.newBuilder()
                .build(),"http://api.testnet.kabuto.sh/v1", Executors.newScheduledThreadPool(1), 10000, null, null);
    }

    @Test
    public void testKabuto() throws  InterruptedException {

        underTest.transactionFlowable("0.0.46764");
        Thread.sleep(20000);
    }


}
