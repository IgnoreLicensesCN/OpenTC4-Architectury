package thaumcraft.common.blocks.abstracts;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.multiparts.formedmatch.IFormedMultipartMatcher;
import thaumcraft.common.multiparts.MultipartMatchInfo;

public interface IMultipartComponentBlock {

    @NotNull BlockPos findTransformBasePosRelatedInMultipart(Level level, BlockState state, BlockPos pos);
    default BlockPos findTransformBasePosRelatedToSelf(Level level, BlockState state, BlockPos pos){
        var selfPosRelated = findSelfPosRelatedInMultipart(level, state, pos);
        return findTransformBasePosRelatedInMultipart(level, state, pos)
                .offset(-selfPosRelated.getX(), -selfPosRelated.getY(), -selfPosRelated.getZ());
    };
    @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos);
    @NotNull IFormedMultipartMatcher getMultipartMatcher(Level level, BlockState state, BlockPos pos);
    void onMultipartDestroyed(Level level, BlockState state, BlockPos pos);
    @NotNull MultipartMatchInfo getMatchInfo(Level level, BlockState state, BlockPos pos);

    default void checkMultipart(Level level, BlockState blockState, BlockPos selfBlockPos){
        if (Platform.getEnvironment() != Env.SERVER){return;}
        if (findTransformBasePosRelatedToSelf(level,blockState,selfBlockPos).equals(BlockPos.ZERO)) {
            var transformPos = findTransformBasePosRelatedInMultipart(level, blockState, selfBlockPos);
            var transformWorldPos = findTransformBasePosRelatedInMultipart(level, blockState, selfBlockPos).offset(selfBlockPos);
            var selfPosInMultipart = findSelfPosRelatedInMultipart(level,blockState,selfBlockPos);
            var matchInfo = getMatchInfo(level,blockState,selfBlockPos);
            var matcher = getMultipartMatcher(level,blockState,selfBlockPos);
            var matched = matcher.match(level,selfBlockPos, selfPosInMultipart, transformPos, matchInfo);
            if (!matched) {
                matcher.destroyMultipart(level, selfBlockPos, selfPosInMultipart, transformWorldPos,transformPos,
                        matchInfo);
            }
        }
    };
}
