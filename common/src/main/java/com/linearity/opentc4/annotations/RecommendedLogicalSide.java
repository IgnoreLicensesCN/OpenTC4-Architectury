package com.linearity.opentc4.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE;

//only for reading code
//i always forget this.
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, MODULE, PARAMETER, TYPE})
public @interface RecommendedLogicalSide {
    LogicalSide value();

    enum LogicalSide { SERVER, CLIENT }
}

