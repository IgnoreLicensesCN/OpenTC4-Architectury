package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import org.jetbrains.annotations.Nullable;

public interface ITagAccessorOwner<AdditionalInfoClass> {
    @Nullable CompoundTagAccessor<AdditionalInfoClass> getOwningTagAccessor();
}
