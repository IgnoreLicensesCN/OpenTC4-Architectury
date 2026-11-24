package com.linearity.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE_USE})
public @interface Modifiable {
}
