package io.oxiles.integration.broadcast.blockchain;

import io.oxiles.dto.block.BlockDetails;
import io.oxiles.dto.event.ContractEventDetails;
import io.oxiles.dto.hcs.HCSMessageTransactionDetails;
import io.oxiles.dto.transaction.TransactionDetails;
import lombok.extern.slf4j.Slf4j;
import io.oxiles.chain.service.strategy.HashGraphTransactionData;
import io.oxiles.integration.broadcast.BroadcastException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * A BlockchainEventBroadcaster that broadcasts the events via a http post.
 *
 * The url to post to for block and contract events can be configured via the
 * broadcast.http.contractEvents and broadcast.http.blockEvents properties.
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
@Slf4j
public class HttpBlockchainEventBroadcaster implements BlockchainEventBroadcaster {

    private HttpBroadcasterSettings settings;

    private RestTemplate restTemplate;

    private RetryTemplate retryTemplate;

    public HttpBlockchainEventBroadcaster(HttpBroadcasterSettings settings, RetryTemplate retryTemplate) {
        this.settings = settings;

        restTemplate = new RestTemplate();
        this.retryTemplate = retryTemplate;
    }

    @Override
    public void broadcastNewBlock(BlockDetails block) {
        retryTemplate.execute((context) -> {
            final ResponseEntity<Void> response =
                    restTemplate.postForEntity(settings.getBlockEventsUrl(), block, Void.class);

            checkForSuccessResponse(response);
            return null;
        });
    }

    @Override
    public void broadcastContractEvent(ContractEventDetails eventDetails) {
        retryTemplate.execute((context) -> {
            final ResponseEntity<Void> response =
                    restTemplate.postForEntity(settings.getContractEventsUrl(), eventDetails, Void.class);

            checkForSuccessResponse(response);
            return null;
        });
    }

    @Override
    public void broadcastTransaction(HashGraphTransactionData hashGraphTransactionData) {
        /*retryTemplate.execute((context) -> {
            try {
                final ResponseEntity<Void> response =
                        restTemplate.postForEntity(settings.getTransactionEventsUrl(), kabutoTransactionData, Void.class);

                checkForSuccessResponse(response);
            }
            catch (Exception e){
                log.error("Error sending request",e);
                throw e;
            }
            return null;
        });*/
    }

    @Override
    public void broadcastMessageTransaction(HCSMessageTransactionDetails hcsMessageTransactionDetails) {
        retryTemplate.execute((context) -> {
            final ResponseEntity<Void> response =
                    restTemplate.postForEntity(settings.getTransactionEventsUrl(), hcsMessageTransactionDetails, Void.class);

            checkForSuccessResponse(response);
            return null;
        });
    }

    @Override
    public void broadcastTransaction(TransactionDetails transactionDetails) {
        /*retryTemplate.execute((context) -> {
            final ResponseEntity<Void> response =
                    restTemplate.postForEntity(settings.getTransactionEventsUrl(), transactionDetails, Void.class);

            checkForSuccessResponse(response);
            return null;
        });*/
    }

    private void checkForSuccessResponse(ResponseEntity<Void> response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new BroadcastException(
                    String.format("Received a %s response when broadcasting via http", response.getStatusCode()));
        }
    }
}
