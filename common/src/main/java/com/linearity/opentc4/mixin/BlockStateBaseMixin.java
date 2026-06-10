package com.linearity.opentc4.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import thaumcraft.common.items.abstracts.IDowsingTool;

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
        if (toolStack != null && toolStack.getItem() instanceof IDowsingTool dowsingTool) {
            var random = RandomSource.createNewThreadLocalInstance();
            List<ItemStack> dropsCopy = new ArrayList<>(drops);
            for (ItemStack drop : drops) {
                var dowsingResult = dowsingTool.findDowsingResult(
                        drop,
                        toolStack,
                        random
                );
                if (dowsingResult == null) {
                    dropsCopy.add(drop);
                }else {
                    var remaining = dowsingResult.a();
                    if (!remaining.isEmpty()) {
                        dropsCopy.add(remaining);
                    }
                    dropsCopy.addAll(dowsingResult.b());
                }
            }
            return dropsCopy;
        }
        return drops;
    }
}
