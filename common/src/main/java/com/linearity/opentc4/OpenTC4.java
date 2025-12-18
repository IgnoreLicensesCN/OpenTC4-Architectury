package com.linearity.opentc4;

import com.linearity.opentc4.utils.vanilla1710.BiomeWithTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.client.fx.migrated.Particles;
import thaumcraft.client.renderers.item.RenderUtils;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.misc.dispensebehavior.ThaumcraftDispenseBehaviors;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;
import thaumcraft.common.lib.events.CommandThaumcraft;
import thaumcraft.common.lib.events.EventHandlerEntity;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenConfiguredFeatures;
import thaumcraft.common.lib.world.biomes.BiomeRegistration;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public final class OpenTC4 {
    public static final String MOD_ID = "opentc4";
    public static final String MOD_NAME = "OpenTC4";
    public static PlatformUniqueUtils platformUtils;
    public static final Logger LOGGER = LogManager.getLogger("OpenTC4");

    public static void init(PlatformUniqueUtils utils) {
        OpenTC4.platformUtils = utils;

        //sorry for abc but i'm tired
        utils.registerCommand(CommandThaumcraft::register);


    }
    public static void onServerStarting(){
        ThaumcraftEnchantments.init();
        ThaumcraftBlocks.init();
        ThaumcraftItems.init();
        ThaumcraftBlockEntities.init();
        ThaumcraftEntities.init();
        ThaumcraftDispenseBehaviors.init();
        BiomeWithTypes.init();
        EventHandlerEntity.init();
        EntityUtils.init();
        BiomeRegistration.init();
        ThaumcraftEnchantments.init();
        ThaumcraftWorldGenConfiguredFeatures.init();
    }

    public static void onClientStarting() {
        onServerStarting();

        Particles.init();
        OpenTC4CommonProxy.INSTANCE = new OpenTC4ClientProxy();
        RenderUtils.init();
    }
}
