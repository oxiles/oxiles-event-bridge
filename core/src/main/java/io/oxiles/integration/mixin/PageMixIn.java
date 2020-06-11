package io.oxiles.integration.mixin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SimplePageImpl.class)
public interface PageMixIn {
}
