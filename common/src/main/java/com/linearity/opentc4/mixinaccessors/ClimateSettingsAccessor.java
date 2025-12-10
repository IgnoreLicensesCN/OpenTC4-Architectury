package com.linearity.opentc4.mixin;

public interface ClimateSettingsAccessor {
    boolean opentc4$hasPrecipitation();
    float opentc4$getTemperature();
    float opentc4$getDownfall();
}
