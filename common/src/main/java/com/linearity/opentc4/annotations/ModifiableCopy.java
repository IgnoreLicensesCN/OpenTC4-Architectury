package com.linearity.opentc4.annotations;

import java.lang.annotation.*;

//surly you can modify the list(or something else)
//but it wont reflect to what it comes
//like i copied items from a bag.
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE_USE})
public @interface ModifiableCopy {
}
