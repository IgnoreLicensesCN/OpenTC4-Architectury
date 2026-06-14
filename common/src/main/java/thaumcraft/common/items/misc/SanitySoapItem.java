package thaumcraft.common.items.misc;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

public class SanitySoapItem extends Item {
    public SanitySoapItem(Properties properties) {
        super(properties);
    }
    public SanitySoapItem() {
        this(new Properties());
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BLOCK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.startUsingItem(interactionHand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, new ItemStack(this));
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 200;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        int ticks = this.getUseDuration(itemStack) - ticksRemaining;
        if (ticks > 200) {
            livingEntity.stopUsingItem();
        }

        if (level.isClientSide){
            var pos = livingEntity.blockPosition();
            if (level.random.nextFloat() < 0.2F) {
                level.playSound(
                        livingEntity,
                        pos
                        , ThaumcraftSounds.ROOTS, SoundSource.PLAYERS, 0.1F, 1.5F + level.random.nextFloat() * 0.2F);
            }
            if (level instanceof ClientLevel clientLevel){
                for(int a = 0; a < ClientFXUtils.particleCount(5); ++a) {
                    ClientFXUtils.crucibleBubble(
                            clientLevel,
                            (float)pos.getX() + 0.5F + level.random.nextFloat(),
                            (float)livingEntity.getBoundingBox().minY + level.random.nextFloat() * livingEntity.getEyeHeight(),
                            (float)pos.getX() + 0.5F + level.random.nextFloat(),
                            1.0F, 0.8F, 0.9F);
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int ticksRemaining) {
        int usedTicks = this.getUseDuration(itemStack) - ticksRemaining;
        if (usedTicks > 190) {
            itemStack.shrink(1);
            var bpos = livingEntity.blockPosition();
            if (!level.isClientSide) {
                float chance = 0.33F;
                if (livingEntity.hasEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.WARP_WARD())) {
                    chance += 0.25F;
                }

                if (level.getBlockState(bpos).getFluidState().is(ThaumcraftFluids.ThaumcraftFluidInstances.PURE_FLUID_FLOWING())
                ) {
                    chance += 0.25F;
                }
                if (livingEntity instanceof ServerPlayer player) {
                    var warpInfo = WarpInfo.getFromPlayer(player);

                    if (level.getRandom().nextFloat() < chance
                            && warpInfo.getStickyWarp() > 0) {
                        warpInfo.addStickyWarp(-1);
                        warpInfo.syncSendPacket(player);
                    }

                    if (warpInfo.getTempWarp() > 0) {
                        warpInfo.setTempWarp(0);
                        warpInfo.syncSendPacket(player);
                    }
                }
            } else {
                level.playSound(livingEntity,bpos,ThaumcraftSounds.CRAFT_START, SoundSource.PLAYERS, 0.25F, 1.0F);

                if (level instanceof ClientLevel clientLevel) {
                    for(int a = 0; a < ClientFXUtils.particleCount(20); ++a) {
                        ClientFXUtils.crucibleBubble(
                                clientLevel,
                                bpos.getX() + 0.5F + level.random.nextFloat()*1.5F,
                                (float)livingEntity.getBoundingBox().minY + level.random.nextFloat() * livingEntity.getEyeHeight(),
                                bpos.getX() + 0.5F + level.random.nextFloat()*1.5F,
                                1.0F, 0.7F, 0.9F);
                    }
                }
            }
        }

    }
}
