package com.btireland.talos.core.common.test.tag;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Tag("BDDTest")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BDDTest {
}
