package thaumcraft.common.items.eldritch;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.config.ConfigBlocks;

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
        var player = useOnContext.getPlayer();
        var clickedPos = useOnContext.getClickedPos();
        if (Platform.getEnvironment() != Env.CLIENT) {
            return super.useOn(useOnContext);
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
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
            int x = clickedPos.getX();
            int y = clickedPos.getY();
            int z = clickedPos.getZ();

            //TODO:Meta -> state
            world.setBlock(clickedPos.above(1), ConfigBlocks.blockEldritch, 0, 3);
            world.setBlock(clickedPos.above(3), ConfigBlocks.blockEldritch, 1, 3);
            world.setBlock(clickedPos.above(4), ConfigBlocks.blockEldritch, 2, 3);
            world.setBlock(clickedPos.above(5), ConfigBlocks.blockEldritch, 2, 3);
            world.setBlock(clickedPos.above(6), ConfigBlocks.blockEldritch, 2, 3);
            world.setBlock(clickedPos.above(7), ConfigBlocks.blockEldritch, 2, 3);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(useOnContext);
    }
}
