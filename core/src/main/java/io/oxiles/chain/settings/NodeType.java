package io.oxiles.chain.settings;

public enum NodeType
{
    NORMAL("NORMAL"),
    MIRROR("MIRROR"),
    DRAGONGLASS("DRAGONGLASS"),
    KABUTO("KABUTO");

    private String nodeType;

    NodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }
}
