package io.oxiles.chain.service.strategy;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class DragonGlassTransactionMain {
    @Getter
    @Setter
    String size;
    @Getter
    @Setter
    String totalCount;
    @Getter
    @Setter
    List<DragonGlassTransactionData> data;
}
