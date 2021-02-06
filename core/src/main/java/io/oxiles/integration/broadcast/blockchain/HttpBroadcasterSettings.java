package io.oxiles.integration.broadcast.blockchain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class HttpBroadcasterSettings {

    @Value("${broadcaster.http.blockEventsUrl:}")
    private String blockEventsUrl;

    @Value("${broadcaster.http.contractEventsUrl:}")
    private String contractEventsUrl;

    @Value("${broadcaster.http.transactionEventsUrl:}")
    private String transactionEventsUrl;


    @Value("${broadcaster.http.tokenTranaferEventsUrl:}")
    private String tokenTranaferEventsUrl;
}
