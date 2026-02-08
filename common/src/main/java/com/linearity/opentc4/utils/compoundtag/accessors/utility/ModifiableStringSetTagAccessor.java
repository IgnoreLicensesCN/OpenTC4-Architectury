package com.linearity.opentc4.utils.compoundtag.accessors.utility;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.StringTagAccessor;

public class ModifiableStringSetTagAccessor extends ModifiableSetTagAccessor<String> {
    public ModifiableStringSetTagAccessor(String tagKey) {
        super(tagKey, new StringTagAccessor(tagKey+"_key"));
    }
}
