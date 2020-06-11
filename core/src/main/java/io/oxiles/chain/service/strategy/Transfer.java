package io.oxiles.chain.service.strategy;

import lombok.Getter;
import lombok.Setter;

public class Transfer {
    @Getter
    @Setter
    String account;
    @Getter
    @Setter
    String amount;
    @Getter
    @Setter
    String type;
}
