package thaumcraft.common.items.misc;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;

import java.util.List;

import static thaumcraft.common.blocks.ThaumcraftBlocks.FLUX_GAS;
import static thaumcraft.common.blocks.ThaumcraftBlocks.FLUX_GOO;


public class PrimePearlItem extends Item {
    public PrimePearlItem() {
        super(new Properties().rarity(Rarity.EPIC).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.translatable("item.PrimePearlItem.text.1")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("item.PrimePearlItem.text.2")
                .withStyle(ChatFormatting.DARK_PURPLE));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (player instanceof ServerPlayer serverPlayer) {
            ResearchManager.unlockResearchForPlayer(level, serverPlayer, "PRIMPEARL", "ELDRITCHMINOR");
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {

        var world = useOnContext.getLevel();
        if ((Platform.getEnvironment() == Env.CLIENT)) {return super.useOn(useOnContext);}

        var player = useOnContext.getPlayer();
        if (player == null) {return super.useOn(useOnContext);}
        var pos = useOnContext.getClickedPos();

        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof AbstractNodeBlockEntity node) {
//            player.swingItem();
            player.swing(useOnContext.getHand(), true);
            if (!world.isClientSide) {
                var itemstack = useOnContext.getItemInHand();
                itemstack.shrink(1);
                boolean research = ThaumcraftApiHelper.isResearchComplete(player.getName().getString(), "PRIMNODE");

                for(Aspect a : node.getAspects().getAspects().keySet()) {
                    int m = node.getNodeVisBase(a);
                    if (!a.isPrimal()) {
                        if (world.getRandom().nextBoolean()) {
                            node.setNodeVisBase(a, (short)(m - 1));
                        }
                    } else {
                        m = m - 2 + world.getRandom().nextInt(research ? 9 : 6);
                        node.setNodeVisBase(a, (short)m);
                    }
                }

                for(Aspect a : Aspect.getPrimalAspects()) {
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
                    node.setNodeModifier(null);
                } else if (node.getNodeModifier() == null && world.getRandom().nextInt(5) == 0) {
                    node.setNodeModifier(NodeModifier.BRIGHT);
                }

                node.setChanged();
                world.sendBlockUpdated(pos,node.getBlockState(),node.getBlockState(),Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
//                node.markDirty();
                world.explode(null, (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)1.5F, (double)pos.getZ() + (double)0.5F, 3.0F + world.getRandom().nextFloat() * (float)(research ? 3 : 5), Level.ExplosionInteraction.BLOCK);

                for(int a = 0; a < 33; ++a) {
                    int xx = pos.getX() + world.getRandom().nextInt(6) - world.getRandom().nextInt(6);
                    int yy = pos.getY() + world.getRandom().nextInt(6) - world.getRandom().nextInt(6);
                    int zz = pos.getZ() + world.getRandom().nextInt(6) - world.getRandom().nextInt(6);
                    var pos1 = new BlockPos(xx, yy, zz);
                    var stateToSet = world.getBlockState(pos1);
                    if (stateToSet.isAir()) {
                        //TODO:I left meta behind,all meta are 8(a kind of fluid state?)
                        if (yy < pos.getY()) {
                            BlockState state = FLUX_GOO.defaultBlockState().setValue(FluxGooBlock.LEVEL,8);
                            world.setBlock(pos1, state, Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
                        } else {
                            BlockState state = FLUX_GAS.defaultBlockState().setValue(FluxGooBlock.LEVEL,8);
                            world.setBlock(pos1, state, Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
                        }
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }



        return super.useOn(useOnContext);

    }
}
