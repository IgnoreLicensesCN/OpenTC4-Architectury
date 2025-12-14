package com.linearity.opentc4.mixinstackhelper;

public class MilkContext {
    public static final ThreadLocal<Boolean> FROM_MILK =
            ThreadLocal.withInitial(() -> false);//a bit trick from chatGPT,then i have a way to identify if milkBucketItem called removeAll
}
