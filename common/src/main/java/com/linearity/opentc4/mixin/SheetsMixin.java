package com.linearity.opentc4.mixin;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.crafted.HungryChestBlockEntity;

import static net.minecraft.client.renderer.Sheets.CHEST_SHEET;

@Mixin(Sheets.class)
public class SheetsMixin {
    @Unique
    private static final Material HUNGRY_CHEST_MATERIAL = new Material(
            CHEST_SHEET, new ResourceLocation(Thaumcraft.MOD_ID,"entity/chest/hungry_chest")
    );

    @Inject(
            method = "chooseMaterial(" +
                    "Lnet/minecraft/world/level/block/entity/BlockEntity;" +
                    "Lnet/minecraft/world/level/block/state/properties/ChestType;" +
                    "Z)" +
                    "Lnet/minecraft/client/resources/model/Material;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void opentc4$chooseMaterial(
            BlockEntity be,
            ChestType type,
            boolean xmas,
            CallbackInfoReturnable<Material> cir
    ) {
        if (be.getClass() == HungryChestBlockEntity.class) {
            cir.setReturnValue(HUNGRY_CHEST_MATERIAL);
        }
    }
}

