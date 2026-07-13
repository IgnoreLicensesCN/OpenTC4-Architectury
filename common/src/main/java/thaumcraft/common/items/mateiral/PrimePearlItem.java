package thaumcraft.common.items.mateiral;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CompoundAspect;
import thaumcraft.api.nodes.INodeBlockEntity;
import thaumcraft.api.nodes.NodeModifier;

import java.util.List;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.blocks.liquid.FluxGasBlock.fullOfGas;
import static thaumcraft.common.blocks.liquid.FluxGooBlock.fullOfGoo;
import static thaumcraft.api.research.ThaumcraftResearches.PRIME_PEARL_NODE_CONTROL;


public class PrimePearlItem extends Item {
    public PrimePearlItem() {
        super(new Properties().rarity(Rarity.EPIC).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(
                Component.translatable("item.PrimePearlItem.text.1")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(
                Component.translatable("item.PrimePearlItem.text.2")
                .withStyle(ChatFormatting.DARK_PURPLE));
    }

//    @Override
//    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
//
//        if (player instanceof ServerPlayer serverPlayer) {
//            ResearchManager.unlockResearchForPlayer(level, serverPlayer, "PRIMPEARL", "ELDRITCHMINOR");
//        }
//        return super.use(level, player, interactionHand);
//    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {

        var world = useOnContext.getLevel();
        if (world.isClientSide) {return InteractionResult.sidedSuccess(world.isClientSide());}

        var player = useOnContext.getPlayer();
        if (player == null) {return InteractionResult.sidedSuccess(world.isClientSide());}
        var pos = useOnContext.getClickedPos();

        BlockEntity te = LevelBlockEntityAccessing.getExistingBlockEntity(world, pos);
        if (te instanceof INodeBlockEntity node) {
            player.swing(useOnContext.getHand(), true);
            var itemstack = useOnContext.getItemInHand();
            itemstack.shrink(1);
            boolean research = PRIME_PEARL_NODE_CONTROL.isLivingEntityCompletedResearch(player);

            for(Aspect a : node.getAspects().keySet()) {
                int m = node.getNodeVisBase(a);
                if (!(a instanceof CompoundAspect)) {
                    if (world.getRandom().nextBoolean()) {
                        node.setNodeVisBase(a, (short)(m - 1));
                    }
                } else {
                    m = m - 2 + world.getRandom().nextInt(research ? 9 : 6);
                    node.setNodeVisBase(a, (short)m);
                }
            }

            for(Aspect a : Aspects.getPrimalAspects()) {
                int m = node.getNodeVisBase(a);
                int r = world.getRandom().nextInt(research ? 4 : 3);
                if (r > 0 && r > m) {
                    node.setNodeVisBase(a, (short)r);
                    node.addToContainer(a, 1);
                }
            }

            if (node.getNodeModifier() == NodeModifier.FADING && world.getRandom().nextBoolean()) {
                node.setNodeModifier(NodeModifier.PALE);
            } else if (node.getNodeModifier() == NodeModifier.PALE && world.getRandom().nextBoolean()) {
                node.setNodeModifier(NodeModifier.EMPTY);
            } else if (node.getNodeModifier() == NodeModifier.EMPTY && world.getRandom().nextInt(5) == 0) {
                node.setNodeModifier(NodeModifier.BRIGHT);
            }

            node.setChanged();
            world.setBlockAndUpdate(pos,node.getBlockState());
//                node.markDirty();
            world.explode(null, (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)1.5F, (double)pos.getZ() + (double)0.5F, 3.0F + world.getRandom().nextFloat() * (float)(research ? 3 : 5), Level.ExplosionInteraction.BLOCK);

            for(int a = 0; a < 33; ++a) {
                int xx = pos.getX() + world.getRandom().nextInt(6) - world.getRandom().nextInt(6);
                int yy = pos.getY() + world.getRandom().nextInt(6) - world.getRandom().nextInt(6);
                int zz = pos.getZ() + world.getRandom().nextInt(6) - world.getRandom().nextInt(6);
                var pos1 = new BlockPos(xx, yy, zz);
                var stateToSet = world.getBlockState(pos1);
                if (stateToSet.isAir()) {
                    if (yy < pos.getY()) {
                        world.setBlock(pos1, fullOfGoo(), Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
                    } else {
                        world.setBlock(pos1, fullOfGas(), Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
                    }
                }
            }

            return InteractionResult.SUCCESS;
        }



        return super.useOn(useOnContext);

    }
}
