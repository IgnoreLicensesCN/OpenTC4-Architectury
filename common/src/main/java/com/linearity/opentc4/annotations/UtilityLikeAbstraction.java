package com.linearity.opentc4.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE;

//if on interface,itself won't be checked by "instanceof",
// wont become game content,won't mean more things than it's super class/interface in logic
// like "IDefaultWorldlyContainer extends WorldlyContainer",it's just WorldlyContainer
// sometimes i just lazy to copy-and-paste code(which is hard to take care of) and want to use "default"
//same to abstract class
@Retention(RetentionPolicy.SOURCE)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, MODULE, PARAMETER, TYPE})
public @interface UtilityLikeAbstraction {
    String reason() default "";
}
