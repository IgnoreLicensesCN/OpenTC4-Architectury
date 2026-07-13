package thaumcraft.api.listeners.wandconsumption;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.WandConsumptionTypeResourceLocation;

public class ThaumcraftWandConsumptionTypes {
    public static final WandConsumptionType CONSUMPTION_NONE = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "none"));
    public static final WandConsumptionType CONSUMPTION_CRAFTING = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "crafting"));
    public static final WandConsumptionType CONSUMPTION_FOCUS = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "focus"));
    public static final WandConsumptionType CONSUMPTION_MULTIBLOCK_CONSTRUCT = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "multiblock_construct"));
    public static final WandConsumptionType CONSUMPTION_REPAIRING_ITEM = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "repair_item"));
    public static final WandConsumptionType CONSUMPTION_RECHARGE_RUNIC_SHIELD = new WandConsumptionType(WandConsumptionTypeResourceLocation.of(Thaumcraft.MOD_ID, "recharge_runic_shield"));
    public static void init(){

    }
}
