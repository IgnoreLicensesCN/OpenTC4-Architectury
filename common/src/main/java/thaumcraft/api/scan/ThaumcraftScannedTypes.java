package thaumcraft.api.scan;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;

public class ThaumcraftScannedTypes {
    public static final ScannedTypeResourceLocation ITEM = ScannedTypeResourceLocation.of(Thaumcraft.MOD_ID,"item");
    public static final ScannedTypeResourceLocation ENTITY = ScannedTypeResourceLocation.of(Thaumcraft.MOD_ID,"entity");
    public static final ScannedTypeResourceLocation VIS_NODE = ScannedTypeResourceLocation.of(Thaumcraft.MOD_ID,"vis_node");
    public static final ScannedTypeResourceLocation PLAYER = ScannedTypeResourceLocation.of(Thaumcraft.MOD_ID,"player");
}
