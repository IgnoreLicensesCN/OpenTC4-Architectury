package thaumcraft.common.multiparts.constructmatch;

import com.linearity.opentc4.VecTransformations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.abstracts.IMultipartComponentBlock;
import thaumcraft.common.multiparts.MultipartMatchInfo;
import thaumcraft.common.multiparts.formedmatch.IFormedMultipartMatcher;
import thaumcraft.common.multiparts.formedmatch.IFormedBlockMatcher;

/**
 * @param matchers [y][x][z] it seems to be left-handed sorry.
 */ //I didn't consider about dynamic-sized multipart like mekanism,
// maybe you need to override match methods and send "null" matchers into constructor args
public record SimpleFormedMultipartMatcher(
        IFormedBlockMatcher[][][] matchers) implements IFormedMultipartMatcher {

    @Override
    public boolean match(
            @NotNull Level level,
            BlockPos selfPosInWorld,
            BlockPos selfPosRelatedInMultipart, BlockPos transformBasePosRelatedInMultipart,
            MultipartMatchInfo info
    ) {
        return matchWithTransform(
                level, selfPosInWorld, transformBasePosRelatedInMultipart, selfPosRelatedInMultipart, info);
    }

    @Override
    public boolean matchNearbyBlocksAndSelf(
            Level level,
            BlockPos selfPosInWorld,
            BlockPos selfPosRelatedInMultipart, BlockPos transformBasePosRelatedInMultipart,
            MultipartMatchInfo info
    ) {
        for (BlockPos toCheckPosRelated : new BlockPos[]{
                selfPosRelatedInMultipart,
                selfPosRelatedInMultipart.above(),
                selfPosRelatedInMultipart.below(),
                selfPosRelatedInMultipart.north(),
                selfPosRelatedInMultipart.south(),
                selfPosRelatedInMultipart.west(),
                selfPosRelatedInMultipart.east()
        }) {
            int x = toCheckPosRelated.getX();
            int y = toCheckPosRelated.getY();
            int z = toCheckPosRelated.getZ();
            if (y < 0 || y >= matchers.length) {
                continue;
            }
            var atY = matchers[y];
            if (atY == null) {
                continue;
            }
            if (x < 0 || x >= atY.length) {
                continue;
            }
            var atXY = atY[x];
            if (atXY == null) {
                continue;
            }
            if (z < 0 || z >= atXY.length) {
                continue;
            }
            var singleBlockMatcher = atXY[z];
            if (singleBlockMatcher == null) {
                continue;
            }
            if (!singleBlockMatcher.match(
                    level,
                    level.getBlockState(selfPosInWorld),
                    selfPosInWorld,
                    transformBasePosRelatedInMultipart,
                    toCheckPosRelated,
                    info
            )) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void destroyMultipart(
            Level level,
            BlockPos selfWorldPos, BlockPos selfBasePosRelatedInMultipart, BlockPos transformBasePosInWorld,
            BlockPos transformBasePosInMultipart,
            MultipartMatchInfo info
    ) {
        var rot3D = info.usingRotation();
        var mirror3D = info.usingMirror();
        for (int y = 0; y < matchers.length; y++) {
            var atY = matchers[y];
            if (atY == null) continue;

            for (int x = 0; x < matchers.length; x++) {
                var atXY = atY[x];
                if (atXY == null) continue;

                for (int z = 0; z < atXY.length; z++) {
                    var matcher = atXY[z];
                    if (matcher == null) continue;

                    var selfRelatedPos = new BlockPos(x, y, z);
                    BlockPos worldPos = VecTransformations.transformRelatedPos(
                                    selfRelatedPos,
                                    transformBasePosInMultipart,
                                    rot3D,
                                    mirror3D
                            )
                            .offset(transformBasePosInWorld.offset(transformBasePosInMultipart.multiply(-1)));

                    var bState = level.getBlockState(worldPos);
                    if (
                            matcher.match(
                                    level,
                                    level.getBlockState(worldPos),
                                    worldPos,
                                    transformBasePosInMultipart,
                                    selfRelatedPos,
                                    info
                            )
                                    && bState.getBlock() instanceof IMultipartComponentBlock componentBlock
                                    && componentBlock.findTransformBasePosRelatedToSelf(level, bState, worldPos)
                                    .equals(transformBasePosInWorld)
                    ) {
                        componentBlock.onMultipartDestroyed(level, bState, worldPos);
                    }
                }
            }
        }
    }

    protected boolean matchWithTransform(
            Level level,
            BlockPos selfPosInWorld,
            BlockPos transformBasePosRelatedInMultipart,
            BlockPos selfPosRelatedInMultipart,
            MultipartMatchInfo info
    ) {
        if (!matchNearbyBlocksAndSelf(
                level, selfPosInWorld, selfPosRelatedInMultipart, transformBasePosRelatedInMultipart, info)) {
            return false;
        }
        for (int y = 0; y < matchers.length; y++) {
            var atY = matchers[y];
            if (atY == null) continue;

            for (int x = 0; x < matchers.length; x++) {
                var atXY = atY[x];
                if (atXY == null) continue;

                for (int z = 0; z < atXY.length; z++) {
                    var blockMatcher = atXY[z];
                    if (blockMatcher == null) continue;
                    BlockPos selfRelatedPos = new BlockPos(x, y, z);

                    BlockPos worldPos = VecTransformations.transformRelatedPos(
                            selfRelatedPos,
                            transformBasePosRelatedInMultipart,
                            info.usingRotation(),
                            info.usingMirror()
                    );

                    if (!blockMatcher.match(
                            level,
                            level.getBlockState(worldPos),
                            worldPos,
                            transformBasePosRelatedInMultipart,
                            selfRelatedPos,
                            info
                    )) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
