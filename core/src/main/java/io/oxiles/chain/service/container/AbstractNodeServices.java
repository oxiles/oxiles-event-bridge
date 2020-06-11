package io.oxiles.chain.service.container;

import io.oxiles.chain.settings.NodeType;
import lombok.Data;

@Data
public abstract class AbstractNodeServices implements NodeServices{

    private String nodeName;
    private NodeType nodeType;
}