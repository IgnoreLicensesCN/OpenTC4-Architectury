package thaumcraft.api.listeners.wandconsumption;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.WandConsumptionTypeResourceLocation;

public class ThaumcraftWandConsumptionTypes {
    public static final WandConsumptionType NONE = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "none"));
    public static final WandConsumptionType CRAFTING = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "crafting"));
    public static final WandConsumptionType FOCUS = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "focus"));
    public static final WandConsumptionType MULTIBLOCK_CONSTRUCT = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "multiblock_construct"));
    public static final WandConsumptionType REPAIR_ITEM = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "repair_item"));
    public static final WandConsumptionType RECHARGE_RUNIC_SHIELD = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "recharge_runic_shield"));
    public static void init(){

    }
}
