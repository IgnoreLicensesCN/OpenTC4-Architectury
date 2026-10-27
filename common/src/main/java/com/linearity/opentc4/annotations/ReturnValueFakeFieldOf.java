package com.linearity.opentc4.annotations;

import java.lang.annotation.*;

/**
 * e.g. make a map inside itemstack and put instance inside,which is called "fake field"
 * so if @ModifyingFakeFieldAsResult is marked for result we're modifying this fake field as result--
 * <p>CLAIM ON NOW or result may be modified next call</p>
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE_USE})
public @interface ReturnValueFakeFieldOf {
}
