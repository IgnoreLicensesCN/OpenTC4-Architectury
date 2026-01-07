package thaumcraft.common.blocks.abstracts;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.multiparts.matchers.IFormedMultipartMatcher;
import thaumcraft.common.multiparts.matchers.MultipartMatchInfo;

public interface IMultipartComponentBlock {

    BlockPos findMultipartCheckerPosRelatedToSelf(Level level, BlockState state, BlockPos pos);
    BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos);
    IFormedMultipartMatcher getMultipartMatcher(Level level, BlockState state, BlockPos pos);
    void recoverToOriginalBlock(Level level, BlockState state, BlockPos pos);
    MultipartMatchInfo getMatchInfo(Level level, BlockState state, BlockPos pos);

    default void checkMultipart(Level level, BlockState blockState, BlockPos blockPos){
        if (Platform.getEnvironment() != Env.SERVER){return;}
        if (findMultipartCheckerPosRelatedToSelf(level,blockState,blockPos).equals(BlockPos.ZERO)) {
            var selfPosInMultipart = findSelfPosRelatedInMultipart(level,blockState,blockPos);
            var matchInfo = getMatchInfo(level,blockState,blockPos);
            var matcher = getMultipartMatcher(level,blockState,blockPos);
            var matched = matcher.match(level,blockPos,selfPosInMultipart,matchInfo);
            if (!matched) {
                matcher.recoverMultipart(level,blockPos,selfPosInMultipart,blockPos,matchInfo);
            }
        }
    };
}
