package io.oxiles.chain.service.strategy;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import io.oxiles.chain.service.domain.Block;
import io.oxiles.chain.service.domain.wrapper.Web3jBlock;
import io.oxiles.model.LatestBlock;
import io.oxiles.service.AsyncTaskService;
import io.oxiles.service.EventStoreService;
import io.oxiles.service.exception.NotFoundException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PollingBlockSubscriptionStrategy extends AbstractBlockSubscriptionStrategy<EthBlock> {

    public PollingBlockSubscriptionStrategy(
            Web3j web3j, String nodeName, EventStoreService eventStoreService, BigInteger maxUnsyncedBlocksForFilter, AsyncTaskService asyncService) {
        super(web3j, nodeName, eventStoreService, maxUnsyncedBlocksForFilter, asyncService);
    }

    @Override
    public Disposable subscribe() {

        final Optional<LatestBlock> latestBlock = getLatestBlock();


        if (latestBlock.isPresent()) {

            BigInteger latestBlockNumber = latestBlock.get().getNumber();

            try {

                BigInteger currentBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();

                BigInteger cappedBlockNumber = BigInteger.valueOf(0);

                if (currentBlockNumber.subtract(latestBlockNumber).compareTo(maxUnsyncedBlocksForFilter) == 1) {

                    cappedBlockNumber = currentBlockNumber.subtract(maxUnsyncedBlocksForFilter);
                    log.info("BLOCK: Max Unsynced Blocks gap reached ´{} to {} . Applied {}. Max {}", latestBlockNumber, currentBlockNumber, cappedBlockNumber, maxUnsyncedBlocksForFilter);
                    latestBlockNumber = cappedBlockNumber;
                }
            } catch (Exception e) {
                log.error("Could not get current block to possibly cap range", e);
            }


            final DefaultBlockParameter blockParam = DefaultBlockParameter.valueOf(latestBlockNumber);

            blockSubscription = web3j.replayPastAndFutureBlocksFlowable(blockParam, true).retryWhen(errors -> errors.flatMap(error -> Flowable.timer(5, TimeUnit.SECONDS))).repeatWhen(errors -> errors.flatMap(error -> Flowable.timer(5, TimeUnit.SECONDS)))
                    .subscribe(block -> {
                        if (!block.hasError() && block.getBlock() != null) {
                            log.info("Receeving block rrom {}", nodeName);
                            triggerListeners(block);
                        } else {
                            log.info("REPLAY BLOCK IS NULL");
                            //throw new NotFoundException("REPLAY BLOCK EMPTY");
                        }


                    }, error -> {
                        log.error("Error on getting replay blocks", error);
                    });

        } else {
            blockSubscription = web3j.blockFlowable(true).delay(5000, TimeUnit.MILLISECONDS).retry().subscribe((block -> {
                if (!block.hasError() && block.getBlock() != null) {
                    triggerListeners(block);
                }
                else{
                    log.info("SUBSCRIBED BLOCK IS NULL");
                    throw new NotFoundException("BLOCK EMPTY");
                }
            }), (error -> {
                log.error("Error on getting ongoing blocks", error);
            }));
        }

        return blockSubscription;
    }

    @Override
    Block convertToEventeumBlock(EthBlock blockObject) {
        return new Web3jBlock(blockObject.getBlock(), nodeName);
    }
}
