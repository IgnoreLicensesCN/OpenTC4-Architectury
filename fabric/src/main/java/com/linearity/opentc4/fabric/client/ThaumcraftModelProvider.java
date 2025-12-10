package com.linearity.opentc4.fabric.client;

import dev.felnull.specialmodelloader.api.data.SpecialModelDataGenHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class ThaumcraftModelProvider extends FabricModelProvider {

    public static final List<Consumer<ItemModelGenerators>> itemModelGenerators = new ArrayList<>(40){
        @Override
        public boolean add(Consumer<ItemModelGenerators> itemModelGeneratorsConsumer) {
            if (itemModelGeneratorInstance != null) {
                itemModelGeneratorsConsumer.accept(itemModelGeneratorInstance);
            }
            return super.add(itemModelGeneratorsConsumer);
        }
    };
    public static ThaumcraftModelProvider INSTANCE;
    public ThaumcraftModelProvider(FabricDataOutput output) {
        super(output);
        INSTANCE = this;
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }
    public static ItemModelGenerators itemModelGeneratorInstance;
    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGeneratorInstance = itemModelGenerator;
        for (Consumer<ItemModelGenerators> itemModelGeneratorConsumer : itemModelGenerators) {
            itemModelGeneratorConsumer.accept(itemModelGenerator);
        }
    }


}