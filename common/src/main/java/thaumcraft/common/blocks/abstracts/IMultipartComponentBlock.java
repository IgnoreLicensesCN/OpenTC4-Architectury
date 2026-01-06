package thaumcraft.common.blocks.abstracts;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.multiparts.matchers.AbstractMultipartMatcher;

public interface IMultipartComponentBlock {

    BlockPos findMultipartCheckerPosRelatedToSelf(Level level, BlockState state, BlockPos pos);
    BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos);
    AbstractMultipartMatcher getMultipartMatcher(Level level, BlockState state, BlockPos pos);
    void recoverToOriginalBlock(Level level, BlockState state, BlockPos pos);

    default void chechMultipart(Level level, BlockState blockState, BlockPos blockPos){
        if (Platform.getEnvironment() != Env.SERVER){return;}
        if (findMultipartCheckerPosRelatedToSelf(level,blockState,blockPos).equals(BlockPos.ZERO)) {
            var selfPosInMultipart = findSelfPosRelatedInMultipart(level,blockState,blockPos);
            var matcher = getMultipartMatcher(level,blockState,blockPos);
            var matched = matcher.match(level,blockPos,selfPosInMultipart);
            if (matched != null) {
                matcher.recoverMultipart(level,blockPos,selfPosInMultipart,blockPos,matched);
            }
        }
    };
}
