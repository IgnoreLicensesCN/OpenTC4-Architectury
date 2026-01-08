package thaumcraft.common.multiparts.constructmatch;

import com.linearity.opentc4.recipeclean.blockmatch.IBlockMatcher;
import net.minecraft.core.BlockPos;

import static com.linearity.opentc4.recipeclean.blockmatch.BlockMatcherPresents.*;

public class MultipartMatcherImpls {
    public static final IMultipartConstructMatcher INFERNAL_FURNACE_CONSTRUCT_MATCHER =
            SimpleMultipartConstructMatcher.of(
                    new IBlockMatcher[][][]//[y][x][z]
                            {
                                    {
                                            {NETHER_BRICKS_MATCHER,OBSIDIAN_MATCHER,NETHER_BRICKS_MATCHER},//y=0,x=0
                                            {OBSIDIAN_MATCHER,OBSIDIAN_MATCHER,OBSIDIAN_MATCHER},//y=0,x=1
                                            {NETHER_BRICKS_MATCHER,OBSIDIAN_MATCHER,NETHER_BRICKS_MATCHER},//y=0,x=2
                                    },//y=0
                                    {
                                            {NETHER_BRICKS_MATCHER,OBSIDIAN_MATCHER,NETHER_BRICKS_MATCHER},//y=1,x=0
                                            {OBSIDIAN_MATCHER, LAVA_SOURCE_MATCHER,OBSIDIAN_MATCHER},//y=1,x=1
                                            {NETHER_BRICKS_MATCHER,IRON_BARS_MATCHER,NETHER_BRICKS_MATCHER},//y=1,x=2
                                    },//y=1,
                                    {
                                            {NETHER_BRICKS_MATCHER,OBSIDIAN_MATCHER,NETHER_BRICKS_MATCHER},//y=2,x=0
                                            {OBSIDIAN_MATCHER,null,OBSIDIAN_MATCHER},//y=2,x=1
                                            {NETHER_BRICKS_MATCHER,OBSIDIAN_MATCHER,NETHER_BRICKS_MATCHER},//y=2,x=2
                                    },//y=2
                            },new BlockPos(2,1,1),
                    false,true,false,
                    false,false,false
            );
}
