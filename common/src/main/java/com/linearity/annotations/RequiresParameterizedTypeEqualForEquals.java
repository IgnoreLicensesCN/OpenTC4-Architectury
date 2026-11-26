package com.linearity.annotations;

import java.lang.annotation.*;

//remember to limit <T> same for #equals
//e.g. when Objects.equals(CellStateProperty<A>,CellStateProperty<B>) is true,we should have A == B is true
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface RequiresParameterizedTypeEqualForEquals {
}
