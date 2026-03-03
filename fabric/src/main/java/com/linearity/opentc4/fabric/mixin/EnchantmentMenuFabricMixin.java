package com.linearity.opentc4.fabric.mixin;

/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
//package vazkii.botania.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import thaumcraft.api.blockapi.IEnchantmentPowerProviderBlock;

//import vazkii.botania.common.block.PylonBlock;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuFabricMixin {
    @SuppressWarnings("target")
    @ModifyVariable(
            method = "method_17411(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",//maybe it's the only stole part i kept?
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 0
    )
    private int opentc4$calculateEnchantmentPower(int obj, ItemStack stack, Level level, BlockPos pos) {//from botaniaPylonEnchanting
//        for (int x = -1; x <= 1; ++x) {
//            for (int z = -1; z <= 1; ++z) {
//                if ((x != 0 || z != 0) && level.isEmptyBlock(pos.offset(x, 0, z)) && level.isEmptyBlock(pos.offset(x, 1, z))) {
//                    obj += getPylonValue(level.getBlockState(pos.offset(x * 2, 0, z * 2)), level, pos);
//                    obj += getPylonValue(level.getBlockState(pos.offset(x * 2, 1, z * 2)), level, pos);
//                    if (x != 0 && z != 0) {
//                        obj += getPylonValue(level.getBlockState(pos.offset(x * 2, 0, z)), level, pos);
//                        obj += getPylonValue(level.getBlockState(pos.offset(x * 2, 1, z)), level, pos);
//                        obj += getPylonValue(level.getBlockState(pos.offset(x, 0, z * 2)), level, pos);
//                        obj += getPylonValue(level.getBlockState(pos.offset(x, 1, z * 2)), level, pos);
//                    }
//                }
//            }
//        }
        float temp = 0;
        for(BlockPos blockPos2 : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
            var blockState = level.getBlockState(blockPos2);
            if (blockState.getBlock() instanceof IEnchantmentPowerProviderBlock provider) {
                temp += provider.getEnchantPowerBonus(blockState, level, pos);
            }
        }
        obj += (int) temp;
        return obj;
    }
//
//    @Unique
//    private float getPylonValue(BlockState state, LevelReader world, BlockPos pos) {
//        if (state.getBlock() instanceof PylonBlock pylon) {
//            return pylon.getEnchantPowerBonus(state, world, pos);
//        }
//        return 0;
//    }
}

