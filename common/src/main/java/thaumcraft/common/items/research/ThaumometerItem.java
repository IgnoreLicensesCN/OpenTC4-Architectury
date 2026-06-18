package thaumcraft.common.items.research;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.lib.network.toserveraction.scan.PacketScannedBlockPosC2S;
import thaumcraft.common.lib.network.toserveraction.scan.PacketScannedEntityC2S;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.api.research.ResearchAndScannedInfo;

import java.util.concurrent.atomic.AtomicInteger;

import static thaumcraft.api.listeners.aspects.entity.basic.EntityBasicAspectGetters.getSafeStringForResourceLocation;
import static thaumcraft.api.scan.ThaumcraftScannedTypes.PLAYER;
import static thaumcraft.common.ThaumcraftSounds.CAMERA_TICKS;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.REFLECTS_PLAYER;

//TODO:[maybe wont finished]inventory scan
public class ThaumometerItem extends Item {
    public ThaumometerItem(Properties properties) {
        super(properties);
    }

    public ThaumometerItem() {
        this(new Properties().stacksTo(1));
    }


    protected @Nullable Entity getPointedEntity(ItemStack stack, Level world, Player p, int count) {
        return EntityUtils.getPointedEntity(p, 0.5F, 10.0F, 0.0F, true);
    }

    protected @Nullable BlockPos getScanningBlockPos(ItemStack stack, Level world, Player p, int count) {
        HitResult mop = EntityUtils.getHitResultFromPlayer(p.level(), p, true);
        if (mop instanceof BlockHitResult blockHitResult) {
            return blockHitResult.getBlockPos();
        }
        return null;
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int useRemainingCount) {
        if (livingEntity instanceof Player player && level.isClientSide) {
            var count = counter.decrementAndGet();
            var localPlayer = Minecraft.getInstance().player;
            if (localPlayer == player) {
                if (count % 2 == 0) {
                    player.level().playSound(
                            player,
                            player.blockPosition(),
                            CAMERA_TICKS,
                            SoundSource.PLAYERS,
                            0.2F,
                            0.45F + player.level().getRandom().nextFloat() * 0.1F
                    );
                }
                var scanningEntity = getPointedEntity(itemStack, level, player, useRemainingCount);
                BlockPos scanningBlockPos = null;
                if (scanningEntity != null) {
                    ClientFXUtils.blockRunes(level,
                            scanningEntity.getX() - (double) 0.5F,
                            scanningEntity.getY() + (double) (scanningEntity.getEyeHeight() / 2.0F),
                            scanningEntity.getZ() - (double) 0.5F, 0.3F + level.getRandom().nextFloat() * 0.7F,
                            0.0F,
                            0.3F + level.getRandom().nextFloat() * 0.7F,
                            (int) (scanningEntity.getBoundingBox().maxY - scanningEntity.getBoundingBox().minY * 15.0F),
                            0.03F);
                } else {
                    scanningBlockPos = getScanningBlockPos(itemStack, level, player, useRemainingCount);
                    if (scanningBlockPos != null) {
                        if (level.getBlockState(scanningBlockPos).is(REFLECTS_PLAYER)) {
                            ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromLiving(player);
                            var playerName = getSafeStringForResourceLocation(player.getGameProfile().getName());
                            var resLoc = new ResourceLocation("pn",playerName);
                            if(!info.hasScannedForType(PLAYER,resLoc)){
                                ClientFXUtils.blockRunes(level,
                                        player.getX() - (double) 0.5F,
                                        player.getY() + (double) (player.getEyeHeight() / 2.0F),
                                        player.getZ() - (double) 0.5F, 0.3F + level.getRandom().nextFloat() * 0.7F,
                                        0.0F,
                                        0.3F + level.getRandom().nextFloat() * 0.7F,
                                        (int) (player.getBoundingBox().maxY - player.getBoundingBox().minY * 15.0F),
                                        0.03F);

                            }
                        }
                        ClientFXUtils.blockRunes(
                                level,
                                scanningBlockPos.getX(),
                                scanningBlockPos.getY() + 0.25,
                                scanningBlockPos.getZ(),
                                0.3F + level.getRandom().nextFloat() * 0.7F,
                                0.0F,
                                0.3F + level.getRandom().nextFloat() * 0.7F,
                                15,
                                0.03F);
                    }
                }
                if (count <= 5) {
                    counter.set(25);
                    if (scanningEntity != null) {
                        new PacketScannedEntityC2S(scanningEntity.getId()).sendToServer();
                    } else {
                        if (scanningBlockPos != null) {

                            //do scan yourself in TC4 without other mods now!
                            if (level.getBlockState(scanningBlockPos).is(REFLECTS_PLAYER)) {
                                ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromLiving(player);
                                var playerName = getSafeStringForResourceLocation(player.getGameProfile().getName());
                                var resLoc = new ResourceLocation("pn",playerName);
                                if(!info.hasScannedForType(PLAYER,resLoc)){
                                    new PacketScannedEntityC2S(player.getId()).sendToServer();
                                }
                            }

                            new PacketScannedBlockPosC2S(scanningBlockPos).sendToServer();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        super.releaseUsing(itemStack, level, livingEntity, i);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}
