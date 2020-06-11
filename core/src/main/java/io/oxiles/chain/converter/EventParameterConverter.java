package io.oxiles.chain.converter;

import io.oxiles.dto.event.parameter.EventParameter;

/**
 * A converter that converts the input value of type T, into an EventParameter.
 *
 * @param <T> The input type
 *
 * @author Craig Williams <craig.williams@consensys.net>
 */
public interface EventParameterConverter<T> {

    EventParameter convert(T toConvert);
}
