package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.node.NodeBlockEntity;
import thaumcraft.common.tiles.node.SilverWoodKnotNodeBlockEntity;

import java.util.List;

public class SilverWoodKnotBlock extends RotatedPillarBlock {

    public static final SoundType KNOT_SOUND = new SoundType(
            1.0F, // volume
            1.0F, // pitch
            ThaumcraftSounds.CRAFT_FAIL, // break
            SoundEvents.WOOD_STEP, // step
            SoundEvents.WOOD_PLACE, // place
            SoundEvents.WOOD_HIT, // hit
            SoundEvents.WOOD_FALL  // fall
    );
    public SilverWoodKnotBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(blockState ->
                        blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y
                                ? MapColor.COLOR_LIGHT_GRAY
                                : MapColor.TERRACOTTA_WHITE
                )
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(KNOT_SOUND)
                .randomTicks()
                .lightLevel(s -> 7)
                .ignitedByLava());
    }


    @Override
    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean isMoving
    ) {
        super.onRemove(state, level, pos, newState, isMoving);
        if (level instanceof ClientLevel clientLevel && state.getBlock() != newState.getBlock()) {
            var x = pos.getX();
            var y = pos.getY();
            var z = pos.getZ();
            // 粒子
            ClientFXUtils.burst(clientLevel, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, 1.0F);
        }
        if (level instanceof ServerLevel serverLevel) {
            //TODO:wispEssences
        }
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide() && level.random.nextBoolean()) {
            UtilsFX.infusedStoneSparkle(level, blockPos.getX(),blockPos.getY(),blockPos.getZ(), 0);
        }
//        super.spawnDestroyParticles(level, player, blockPos, blockState);
    }
    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        var bEntity = serverLevel.getBlockEntity(blockPos);
        if (bEntity instanceof SilverWoodKnotNodeBlockEntity node){
            node.serverTickByBlockHandle();
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        var bEntity = level.getBlockEntity(blockPos);
        if (bEntity instanceof SilverWoodKnotNodeBlockEntity node){
            node.clientTickByBlockHandle();
        }
    }
}
