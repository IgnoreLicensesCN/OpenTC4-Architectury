package thaumcraft.common.multiparts.matchers;

import com.linearity.opentc4.recipeclean.blockmatch.multipartmatch.MultipartBlockMatcherPresents;
import net.minecraft.core.BlockPos;

public class FormedMultipartMatcherImpls {

    public static final IFormedMultipartMatcher INFERNAL_FURNACE_FORMED =
            new SimpleFormedMultipartMatcher(
                    MultipartBlockMatcherPresents.INFERNAL_FURNACE_MATCHERS_FORMED
                    ,new BlockPos(2,1,1)
            );
}
