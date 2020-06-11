package io.oxiles.chain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BlockchainServiceRegistrar.class)
public class BaseChainConfiguration {

}
