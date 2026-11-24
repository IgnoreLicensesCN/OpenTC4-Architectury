package com.linearity.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//TODO:[maybe wont finished]warn guys about this with a plugin
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface InterfaceShouldAttachTo {
    Class<?>[] value();
}
