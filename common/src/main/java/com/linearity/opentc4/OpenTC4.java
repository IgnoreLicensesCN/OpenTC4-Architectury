package com.linearity.opentc4;

import com.linearity.opentc4.utils.vanilla1710.BiomeWithTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.listeners.EventListeners;
import thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectCalculator;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.client.fx.migrated.Particles;
import thaumcraft.client.renderers.ThaumcraftRenderers;
import thaumcraft.client.renderers.item.RenderUtils;
import thaumcraft.common.aspects.ThaumcraftItemAspects;
import thaumcraft.common.blocks.ThaumcraftBlockAndItemColors;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.menu.ThaumcraftGUI;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.misc.dispensebehavior.ThaumcraftDispenseBehaviors;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;
import thaumcraft.common.lib.events.CommandThaumcraft;
import thaumcraft.common.lib.events.EventHandlerEntity;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.world.registries.ThaumcraftStructures;
import thaumcraft.common.lib.world.registries.ThaumcraftWorldGenConfiguredFeatures;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class OpenTC4 {
    public static boolean DEBUG_FLAG = false;;
    public static boolean CHECK_RESEARCH_RENDER_LOCATION_SAME_FLAG = DEBUG_FLAG;;
    public static final String MOD_ID = "opentc4";
    public static final String MOD_NAME = "OpenTC4";
    public static PlatformUniqueUtils platformUtils;
    public static final Logger LOGGER = LogManager.getLogger("OpenTC4");

    public static void init(PlatformUniqueUtils utils) {
        OpenTC4.platformUtils = utils;

        //sorry for abc but i'm tired
        utils.registerCommand(CommandThaumcraft::register);


    }
    public static void onInitialize() {
        Aspects.init();//not related to any item/block/entity or anything else.it's suitable
        ThaumcraftEnchantments.init();
        ThaumcraftBlocks.init();
        ThaumcraftItems.init();
        ThaumcraftBlockEntities.init();
        ThaumcraftEntities.init();
        ThaumcraftDispenseBehaviors.init();
        BiomeWithTypes.init();
        EventHandlerEntity.init();
        EntityUtils.init();
        ThaumcraftBiomeIDs.init();
        ThaumcraftEnchantments.init();
        ThaumcraftWorldGenConfiguredFeatures.init();
        ThaumcraftStructures.init();
        ThaumcraftGUI.init();

        EventListeners.init();
        ThaumcraftItemAspects.init();

    }
    public static void onInitializeClient() {
        onInitialize();

        Particles.init();
        OpenTC4CommonProxy.INSTANCE = new OpenTC4ClientProxy();
        RenderUtils.init();
        ThaumcraftBlockAndItemColors.init();
        ThaumcraftRenderers.init();

    }

    public static void onServerStarting(){
    }

    public static void onClientStarting() {
    }

    public static void onDatapackReload(){
        ItemBasicAspectCalculator.onDatapackReload();
        ItemBasicAspectGetter.onDatapackReload();
    }
}
