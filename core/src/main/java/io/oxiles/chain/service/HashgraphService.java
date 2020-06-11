package io.oxiles.chain.service;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

public class HashgraphService {

    @Getter
    @Setter
    protected OkHttpClient okHttp;

    public HashgraphService(OkHttpClient okHttpClient) {
        this.okHttp = okHttpClient;
    }


    public void connect() {

    }

    public void disconnect() {

    }

    public void reconnect() {

    }

    public boolean isConnected() {
        return false;
    }

}
