package thaumcraft.common.items.eldritch;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import java.util.List;

public class EldritchObeliskPlacerItem extends Item {

    public EldritchObeliskPlacerItem(){
        super(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.literal("§oCreative Mode Only"));//TODO:Translation Key
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var world = useOnContext.getLevel();
        if (world.isClientSide) {
            return InteractionResult.CONSUME;
        }
        var player = useOnContext.getPlayer();
        var clickedPos = useOnContext.getClickedPos();
        if (player == null) {
            return super.useOn(useOnContext);
        }

        if (useOnContext.getClickedFace() == Direction.UP) {
            player.swing(useOnContext.getHand(),true);

            for(int a = 1; a <= 6; ++a) {
                var state = world.getBlockState(clickedPos.above(a));
                if (!state.isAir()) {
                    return InteractionResult.FAIL;
                }
            }
            world.setBlock(clickedPos.above(1), ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_ALTAR.defaultBlockState(),Block.UPDATE_CLIENTS);
            world.setBlock(clickedPos.above(3), ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_OBELISK_WITH_TICKER.defaultBlockState(), Block.UPDATE_CLIENTS);
            world.setBlock(clickedPos.above(4), ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_OBELISK.defaultBlockState(), Block.UPDATE_CLIENTS);
            world.setBlock(clickedPos.above(5), ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_OBELISK.defaultBlockState(), Block.UPDATE_CLIENTS);
            world.setBlock(clickedPos.above(6), ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_OBELISK.defaultBlockState(), Block.UPDATE_CLIENTS);
            world.setBlock(clickedPos.above(7), ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_OBELISK.defaultBlockState(), Block.UPDATE_CLIENTS);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(useOnContext);
    }
}
