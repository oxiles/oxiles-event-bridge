package io.oxiles.annotation;

import io.oxiles.config.BaseConfiguration;
import io.oxiles.config.DatabaseConfiguration;
import io.oxiles.config.*;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({BaseConfiguration.class, DatabaseConfiguration.class})
public @interface EnableEventeum {
}
