package io.oxiles.chain.settings;

public enum ChainType
{
    ETHEREUM("ethereum"),
    HASHGRAPH("hashgraph");

    private String chainName;

    ChainType(String chainName) {
        this.chainName = chainName;
    }

    public String getChainName() {
        return chainName;
    }
}