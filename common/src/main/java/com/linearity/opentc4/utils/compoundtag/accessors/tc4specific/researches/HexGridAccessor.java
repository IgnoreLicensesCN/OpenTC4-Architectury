package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.researches;

import com.linearity.opentc4.utils.compoundtag.accessors.utility.ModifiableMapAccessor;
import thaumcraft.common.lib.research.HexEntry;
import thaumcraft.common.lib.utils.HexCoord;

public class HexGridAccessor extends ModifiableMapAccessor<HexCoord, HexEntry> {
    public HexGridAccessor(String tagKey) {
        super(tagKey, new HexCoordAccessor(tagKey + "_hex_coord"),new HexEntryAccessor(tagKey + "_hex_type"));
    }
}
