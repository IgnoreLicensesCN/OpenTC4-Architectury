package thaumcraft.common.items.equipment.elemental;

import com.linearity.opentc4.utils.EntityTypeTests;
import com.linearity.opentc4.utils.FurthestBlockFinder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.abstracts.IDropFollowingUserTool;
import thaumcraft.common.items.abstracts.IRedirectBreakPosItem;
import thaumcraft.common.lib.network.fx.PacketFXBlockBubbleS2C;

import java.awt.*;

public class ElementalAxeItem extends AxeItem implements IDropFollowingUserTool, IRedirectBreakPosItem {
    public ElementalAxeItem(Tier tier, float f, float g, Properties properties) {
        super(tier, f, g, properties);
    }

    public ElementalAxeItem() {
        this(ThaumcraftItems.ToolAndArmorMaterial.TOOL_THAUMIUM_ELEMENTAL,
                5.0F,
                -3.0F,
                new Item.Properties().stacksTo(1).rarity(Rarity.RARE)
        );
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.startUsingItem(interactionHand);
        return super.use(level, player, interactionHand);
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }


    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack itemStack, int i) {

        if (user != null && level != null) {
            var itemsNear = level.getEntities(EntityTypeTests.ITEM_ENTITY_TEST, user.getBoundingBox().inflate(10),itemEntity -> true);
            itemsNear.forEach(itemEntity -> {
                var itemPos = itemEntity.position();
                var userPos = user.position();
                var vecToUser = itemPos.subtract(userPos.add(0,user.getEyeHeight()/2,0));
                vecToUser = vecToUser.normalize().multiply(0.3,0.3,0.3);
                var itemVelocity = itemEntity.getDeltaMovement().subtract(vecToUser);
                itemVelocity = new Vec3(Math.clamp(itemVelocity.x,-0.35,0.35),
                        Math.clamp(itemVelocity.y,-0.35,0.35),
                        Math.clamp(itemVelocity.z,-0.35,0.35)
                        );
                itemEntity.setDeltaMovement(itemVelocity);
                if (level.isClientSide) {
                    if (level instanceof ClientLevel clientLevel){
                        ClientFXUtils.crucibleBubble(clientLevel,
                                (float) itemPos.x + (2*level.random.nextFloat() - 1) * 0.125F,
                                (float) itemPos.y + (2*level.random.nextFloat() - 1) * 0.125F,
                                (float) itemPos.z + (2*level.random.nextFloat() - 1) * 0.125F,
                                0.33F,
                                0.33F,
                                1.0F
                        );
                    }
                }
            });
        }
    }

    @Override
    public void makeDropItemEntityAtPos(ItemStack usingToolStack, BlockState droppingState, ServerLevel level, BlockPos atPos, Entity entityToFollow, ItemStack stackToDrop) {
        IDropFollowingUserTool.super.makeDropItemEntityAtPos(usingToolStack, droppingState, level, atPos, entityToFollow, stackToDrop);
        new PacketFXBlockBubbleS2C(atPos.getX(),atPos.getY(),atPos.getZ(), 0x5454FF).sendToAllAround(level,atPos,32*32);
        level.playSound(entityToFollow,entityToFollow.blockPosition(), ThaumcraftSounds.BUBBLE, SoundSource.PLAYERS,0.15F, 1.0F);
    }

    @Override
    public boolean canMakeDropFollowPlayer(ItemStack usingToolStack, BlockState droppingState, ServerLevel level, BlockPos atPos, Entity entityToFollow, ItemStack stackToDrop) {
        var block = Block.byItem(stackToDrop.getItem());
        return block.defaultBlockState().is(BlockTags.MINEABLE_WITH_AXE);
    }

    @Override
    public BlockPos redirectBreakPosToPos(BlockPos originalPos, Entity entityUsingTool,ItemStack usingStack) {
        var level = entityUsingTool.level();
        if (level.getBlockState(originalPos).is(BlockTags.MINEABLE_WITH_AXE)) {
            return new FurthestBlockFinder(level,state -> state.is(BlockTags.MINEABLE_WITH_AXE),64,5).find(originalPos);
        }
        return originalPos;
    }
}
