package thaumcraft.common.tiles.eldritch;

import com.linearity.annotations.StoleFrom;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Optional;
import java.util.Set;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.ENTITY_TEST;
import static net.minecraft.world.level.Level.OVERWORLD;
import static thaumcraft.api.research.ThaumcraftResearches.ENTER_OUTER;
import static thaumcraft.common.blocks.ThaumcraftBlocks.PoiTypeRegistry.POI_ELDRITCH_PORTAL;
import static thaumcraft.common.lib.world.structure.MazeStructure.MAZE_MAX_CHUNK_SIZE;

public class EldritchPortalBlockEntity extends TileThaumcraft {
    public EldritchPortalBlockEntity(BlockEntityType<? extends EldritchPortalBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EldritchPortalBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.ELDRITCH_PORTAL(),blockPos,blockState);
    }
    protected int tickCount = System.identityHashCode(this) & 63;

    public int getTickCount() {
        return tickCount;
    }

    public ResourceKey<Level> dimensionFrom() {
        return OVERWORLD;//TODO:[maybe wont finished]config this
    }
    public ResourceKey<Level> dimensionTo() {
        //TODO:ResourceKey Outer
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
                ENTITY_TEST,
                new AABB(getBlockPos()).inflate(0.5,1,0.5),
                e -> true
        );
        if (!pickedPlayerList.isEmpty()) {
            pickedPlayerList.forEach(entity -> {
                if (!entity.isOnPortalCooldown() && entity.getVehicle() == null && entity.getPassengers().isEmpty()) {

                    var server = entity.getServer();
                    if (server == null) {
                        return;
                    }
                    var level = server.getLevel(entity.level().dimension() == dimensionTo() ? dimensionFrom() : dimensionTo());
                    if (level == null) {
                        return;
                    }
                    var portal = findPortalAround(
                            level,
                            POI_ELDRITCH_PORTAL.getKey(),
                            entity.blockPosition(),
                            MAZE_MAX_CHUNK_SIZE * 16,
                            level.getWorldBorder()
                    ).orElse(null);
                    if (portal != null) {
                        var pos = portal.minCorner.getCenter();
                        entity.teleportTo(
                                level,
                                pos.x + entity.getRandomX(2),
                                pos.y,
                                pos.z + entity.getRandomX(2),
                                Set.of(),
                                entity.getXRot(),
                                entity.getYRot()
                        );
                        if (entity instanceof LivingEntity living){
                            if (!ENTER_OUTER.isLivingEntityCompletedResearch(living)) {
                                ENTER_OUTER.completeResearchFor(living);
                            }
                        }
                    }
                }
            });
        }
    }
    @StoleFrom("net.minecraft.world.level.portal.PortalForcer#findPortalAround")
    public static Optional<BlockUtil.FoundRectangle> findPortalAround(
            ServerLevel serverLevel,
            ResourceKey<PoiType> type,
            BlockPos blockPos,
            int range,
            WorldBorder worldBorder) {

        PoiManager poiManager = serverLevel.getPoiManager();
//        int i = bl ? 16 : 128;
        poiManager.ensureLoadedAndValid(serverLevel, blockPos, range);
        Optional<PoiRecord> optional = poiManager.getInSquare(holder -> holder.is(type),
                        blockPos, range, PoiManager.Occupancy.ANY)
                .filter(poiRecord -> worldBorder.isWithinBounds(poiRecord.getPos()))

                .sorted(
                        (poiA,poiB) -> {
                            var distCompare = Double.compare(
                                    poiA.getPos().distSqr(blockPos),
                                    poiB.getPos().distSqr(blockPos)
                            );
                            if (distCompare == 0) {
                                return Integer.compare(poiA.getPos().getY(), poiB.getPos().getY());
                            }
                            return distCompare;
                        }
                )
                .filter(poiRecord -> serverLevel.getBlockState(poiRecord.getPos()).hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
                .findFirst();
        return optional.map(
                poiRecord -> {
                    BlockPos blockPosx = poiRecord.getPos();
                    serverLevel.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(blockPosx), 3, blockPosx);
                    BlockState blockState = serverLevel.getBlockState(blockPosx);
                    return BlockUtil.getLargestRectangleAround(
                            blockPosx,
                            blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS),
                            21,
                            Direction.Axis.Y,
                            21,
                            blockPosxx -> serverLevel.getBlockState(blockPosxx) == blockState
                    );
                }
        );
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
