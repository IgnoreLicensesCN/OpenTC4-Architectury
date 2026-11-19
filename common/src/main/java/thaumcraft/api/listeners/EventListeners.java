package thaumcraft.api.listeners;

import thaumcraft.api.listeners.aspects.entity.basic.EntityBasicAspectGetters;
import thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectCalculator;
import thaumcraft.api.listeners.aspects.item.bonus.ItemBonusAspectCalculator;
import thaumcraft.api.listeners.croputils.CropHarvester;
import thaumcraft.api.listeners.croputils.DirectlyBreakHarvestableCropChecker;
import thaumcraft.api.listeners.croputils.ReplantPlaceableCropItemGetter;
import thaumcraft.api.listeners.croputils.UseHarvestableCropChecker;
import thaumcraft.api.listeners.elementalhoe.apply.ElementalHoeAffectManager;
import thaumcraft.api.listeners.elementalhoe.check.ElementalHoeAffectiveManager;
import thaumcraft.api.listeners.lamp.fertility.apply.FertilityLampAffectManager;
import thaumcraft.api.listeners.lamp.fertility.check.FertilityLampAffectiveManager;
import thaumcraft.api.listeners.researchtable.WriteAspectManager;
import thaumcraft.api.listeners.wandconsumption.ConsumptionModifierCalculator;
import thaumcraft.api.listeners.warp.WarpEventManager;
import thaumcraft.api.listeners.worldgen.eldritch.bossgen.ThaumcraftEldritchBossProvider;
import thaumcraft.api.listeners.worldgen.node.NodeGenerationManager;

public class EventListeners {
    public static void init(){
        ItemBasicAspectCalculator.init();
        NodeGenerationManager.init();
        ItemBonusAspectCalculator.init();
        EntityBasicAspectGetters.init();
        ConsumptionModifierCalculator.init();
        WarpEventManager.init();
        WriteAspectManager.init();
        ThaumcraftEldritchBossProvider.init();

        FertilityLampAffectiveManager.init();
        FertilityLampAffectManager.init();

        ElementalHoeAffectManager.init();
        ElementalHoeAffectiveManager.init();

        DirectlyBreakHarvestableCropChecker.init();
        UseHarvestableCropChecker.init();
        ReplantPlaceableCropItemGetter.init();
        CropHarvester.init();
    }
}
