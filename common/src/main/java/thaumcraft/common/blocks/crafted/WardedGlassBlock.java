package thaumcraft.common.blocks.crafted;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.tiles.crafted.OwnedBlockEntity;

import static thaumcraft.api.ThaumcraftApiHelper.rayTraceIgnoringSource;

public class WardedGlassBlock extends GlassBlock implements IWandInteractableBlock, EntityBlock {
    public WardedGlassBlock(Properties properties) {
        super(properties);
    }
    public WardedGlassBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .strength(-1,999)
                        .sound(SoundType.STONE)
        );
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if (!(level instanceof ClientLevel clientLevel)) return;
        var lookVec = player.getLookAngle();

        var blockHitResult = rayTraceIgnoringSource
                (level,
                        player.getEyePosition(),
                        player.getEyePosition().add(lookVec.normalize().multiply(new Vec3(10,10,10))),
                        true
                );
        if (blockHitResult == null) {return;}
        var hitVec = blockHitResult.getLocation();

        
        float f = (float) (hitVec.x - blockPos.getX());
        float f1 = (float) (hitVec.y - blockPos.getY());
        float f2 = (float) (hitVec.z - blockPos.getZ());
        ClientFXUtils.blockWard(clientLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockHitResult.getDirection(), f, f1, f2);
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var clickedPos = useOnContext.getClickedPos();
        var blockState = level.getBlockState(clickedPos);
        var blockEntity = level.getBlockEntity(clickedPos);
        if (blockEntity instanceof OwnedBlockEntity owned){
            var player = useOnContext.getPlayer();
            if (player == null) return InteractionResult.PASS;
            var playerName = player.getGameProfile().getName();
            if (owned.owners.contains(playerName)){
                level.destroyBlock(clickedPos, true, player);
                level.playSound(player,clickedPos,SoundType.STONE.getBreakSound(), SoundSource.BLOCKS);
                if (level instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(
                            new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                            clickedPos.getX() + 0.5,
                            clickedPos.getY() + 0.5,
                            clickedPos.getZ() + 0.5,
                            20,     // 数量
                            0.3, 0.3, 0.3, // 扩散
                            0.1     // 速度
                    );
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }



    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new OwnedBlockEntity(blockPos,blockState);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, pos, blockState, livingEntity, itemStack);
        if (Platform.getEnvironment() == Env.SERVER) {
            var player = (livingEntity instanceof Player player0) ? player0:null;
            if (player != null) {
                var ownedBlockEntity = level.getBlockEntity(pos);
                if (ownedBlockEntity instanceof OwnedBlockEntity owned){
                    owned.owners.add(player.getGameProfile().getName());
                }else if (ownedBlockEntity == null){
                    var owned = new OwnedBlockEntity(pos,this.defaultBlockState());
                    owned.owners.add(player.getGameProfile().getName());
                    level.setBlockEntity(owned);
                }
            }
        }
    }
}
