package io.oxiles.chain.config.factory;

import io.oxiles.chain.converter.EventParameterConverter;
import io.oxiles.chain.factory.ContractEventDetailsFactory;
import io.oxiles.chain.factory.DefaultContractEventDetailsFactory;
import io.oxiles.chain.settings.Node;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.web3j.abi.datatypes.Type;

@Data
public class ContractEventDetailsFactoryFactoryBean
        implements FactoryBean<ContractEventDetailsFactory> {

    EventParameterConverter<Type> parameterConverter;
    Node node;
    String nodeName;

    @Override
    public ContractEventDetailsFactory getObject() throws Exception {
        return new DefaultContractEventDetailsFactory(
                parameterConverter, node, nodeName);
    }

    @Override
    public Class<?> getObjectType() {
        return ContractEventDetailsFactory.class;
    }
}
