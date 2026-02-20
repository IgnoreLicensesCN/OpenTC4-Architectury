package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.multiparts.formedmatch.IFormedMultipartMatcher;
import thaumcraft.common.multiparts.MultipartMatchInfo;

public interface IMultipartComponentBlock {

    @NotNull BlockPos findTransformBasePosRelatedInMultipart(Level level, BlockState state, BlockPos pos);
    default BlockPos findTransformBasePosRelatedToSelf(Level level, BlockState state, BlockPos pos){
        var selfPosRelated = findSelfPosRelatedInMultipart(level, state, pos);
        return findTransformBasePosRelatedInMultipart(level, state, pos).subtract(selfPosRelated);
    };
    default BlockPos findTransformBasePosInWorld(Level level, BlockState state, BlockPos pos){
        return findTransformBasePosRelatedToSelf(level,state,pos).offset(pos);
    };
    @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos);
    @NotNull IFormedMultipartMatcher getMultipartMatcher(Level level, BlockState state, BlockPos pos);
    //↓usually recover to multipart component block,but you can also spawn a blaze instead
    void onMultipartDestroyed(Level level, BlockState state, BlockPos pos);
    @NotNull MultipartMatchInfo getMatchInfo(Level level, BlockState state, BlockPos pos);

    default void checkMultipart(Level level, BlockState selfState, BlockPos selfBlockPos){
        if (!(level instanceof ServerLevel)){return;}
//        if (basePosRelated.equals(BlockPos.ZERO)) {
            var transformPos = findTransformBasePosRelatedInMultipart(level, selfState, selfBlockPos);
            var transformWorldPos = findTransformBasePosRelatedInMultipart(level, selfState, selfBlockPos).offset(selfBlockPos);
            var selfPosInMultipart = findSelfPosRelatedInMultipart(level,selfState,selfBlockPos);
            var matchInfo = getMatchInfo(level,selfState,selfBlockPos);
            var matcher = getMultipartMatcher(level,selfState,selfBlockPos);
            var matched = matcher.match(level,selfBlockPos, selfPosInMultipart, transformPos, matchInfo);
            if (!matched) {
                matcher.destroyMultipart(level, selfBlockPos, selfPosInMultipart, transformWorldPos,transformPos,
                        matchInfo);
            }
//        }
    };
}
