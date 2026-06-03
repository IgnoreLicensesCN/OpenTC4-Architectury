package thaumcraft.common.tiles.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.utils.EntityTypeTests.SERVER_PLAYER_TEST;
import static thaumcraft.common.researches.ThaumcraftResearches.ENTER_OUTER;

public class EldritchPortalBlockEntity extends TileThaumcraft {
    public EldritchPortalBlockEntity(BlockEntityType<? extends EldritchPortalBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EldritchPortalBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ELDRITCH_PORTAL,blockPos,blockState);
    }
    private int tickCount = 0;

    public int getTickCount() {
        return tickCount;
    }

    public void serverTick() {
        if (this.level == null) {
            return;
        }
        tickCount += 1;
        if (tickCount % 5 != 0) {
            return;
        }
        var pickedPlayerList = this.level.getEntities(
                SERVER_PLAYER_TEST,
                new AABB(getBlockPos()).inflate(0.5,1,0.5),
                e -> true
        );
        if (!pickedPlayerList.isEmpty()) {
            pickedPlayerList.forEach(player -> {
                if (!player.isOnPortalCooldown() && player.getVehicle() == null && player.getPassengers().isEmpty()) {

                    var server = player.getServer();
                    if (server == null) {
                        return;
                    }
                    var level = server.getLevel();//TODO:ResourceKey Outer
                    if (level == null) {
                        return;
                    }
                    player.teleportTo(
                            level,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            player.getXRot(),
                            player.getYRot()
                    );
                    if (!ENTER_OUTER.isPlayerCompletedResearch(player)) {
                        ENTER_OUTER.completeResearch(player);
                    }
                }
            });
        }
    }
    public void clientTick() {
        if (this.level == null) {
            return;
        }
        tickCount += 1;
        if (tickCount % 250 != 0) {
            return;
        }
        this.level.playSound(
                null,
                getBlockPos(),
                ThaumcraftSounds.EVIL_PORTAL,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );
    }
}
