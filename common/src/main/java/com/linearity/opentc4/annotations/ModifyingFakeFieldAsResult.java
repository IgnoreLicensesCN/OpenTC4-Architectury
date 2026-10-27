package com.linearity.opentc4.annotations;

import java.lang.annotation.*;

/**
 * <p>considering {@link com.linearity.opentc4.mixinaccessors.IObjectCacheableItemStack}</p>
 * when it comes to a class contains a <pre>{@code List<?>}</pre>for lessen <pre>{@code new List<>();}</pre> calling we want to use a List for many times.
 * so we have only one list,we insert things inside.Every call may modify this list,and every call may return a same list.
 * so use this return value well before next call.
 * yeah you can consider return value as a field of some object(probably input object).usually use with ReturnValueFakeFieldOf
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE_USE})
public @interface ModifyingFakeFieldAsResult {
}
