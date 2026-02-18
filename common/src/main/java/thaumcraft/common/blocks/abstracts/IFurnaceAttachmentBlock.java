package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.tiles.abstracts.IThaumcraftFurnace;

public interface IFurnaceAttachmentBlock {
    void onVanillaFurnaceBurning(
            Level level,
            AbstractFurnaceBlockEntity furnaceBlockEntity,
            BlockState furnaceState,
            BlockPos furnacePos,
            BlockState attachmentState,
            BlockPos attachmentPos,
            CallbackInfo ci
    );
    default void onThaumcraftFurnaceBurning(
            Level level,
            IThaumcraftFurnace be,
            BlockState furnaceState,
            BlockPos furnacePos,
            BlockState attachmentState,
            BlockPos attachmentPos
            ){

    }
    default void attachmentOnCalculatingRequiredCookTime(
            Level level,
            IThaumcraftFurnace be,
            BlockState furnaceState,
            BlockPos furnacePos,
            BlockState attachmentState,
            BlockPos attachmentPos,
            int defaultFurnaceRequiredCookTime
    ){
        be.setRequiredCookTime((int)(be.getRequiredCookTime() - defaultFurnaceRequiredCookTime*0.125));
    }
    default void onThaumcraftFurnaceStartBurningFuel(
            Level level,
            IThaumcraftFurnace be,
            BlockState furnaceState,
            BlockPos furnacePos,
            BlockState attachmentState,
            BlockPos attachmentPos,
            int defaultRequiredCookTime
    ){

    }
}
