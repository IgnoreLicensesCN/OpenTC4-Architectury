package com.linearity.opentc4.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import thaumcraft.common.items.abstracts.IDowsingTool;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;

import java.util.ArrayList;
import java.util.List;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateBaseMixin {
    @ModifyReturnValue(
            method = "getDrops",
            at = @At("RETURN")
    )
    private List<ItemStack> opentc4$dowseDrops(List<ItemStack> drops, LootParams.Builder builder) {

        var toolStack = builder.getOptionalParameter(LootContextParams.TOOL);
        if (toolStack == null) {
            return drops;
        }
        var dowsingLevel = EnchantmentHelper.getEnchantments(toolStack).get(ThaumcraftEnchantments.ThaumcraftEnchantmentInstances.DOWSING());
        if (!(toolStack.getItem() instanceof IDowsingTool || dowsingLevel > 0)) {
            return drops;
        }
        var random = RandomSource.createNewThreadLocalInstance();
        List<ItemStack> dropAsNormal = new ArrayList<>(drops.size());
        for (ItemStack drop : drops) {
            var dowsingResult = IDowsingTool.findDowsingResult(
                    drop,
                    toolStack,
                    random
            );
            if (dowsingResult == null) {
                dropAsNormal.add(drop);
            } else {
                var remaining = dowsingResult.a();
                if (!remaining.isEmpty()) {
                    dropAsNormal.add(remaining);
                }
                dropAsNormal.addAll(dowsingResult.b());
            }
        }
        return dropAsNormal;
    }
}
