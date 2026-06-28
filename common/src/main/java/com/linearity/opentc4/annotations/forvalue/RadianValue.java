package com.linearity.opentc4.annotations.forvalue;

import java.lang.annotation.*;

/**
 * number is in radian (0 ~ 2Math.PI)
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
public @interface RadianValue {
    String[] value() default {};
}
