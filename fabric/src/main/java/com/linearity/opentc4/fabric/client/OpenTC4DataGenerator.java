package com.linearity.opentc4.fabric.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import thaumcraft.client.renderers.item.RenderUtils;

public class OpenTC4DataGenerator implements DataGeneratorEntrypoint {
    public static FabricDataGenerator generator;
    public static FabricDataGenerator.Pack generatorPack;

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        generator = fabricDataGenerator;
        generatorPack = generator.createPack();
        generatorPack.addProvider(ThaumcraftModelProvider::new);
        RenderUtils.init();
    }
}
