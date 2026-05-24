package thaumcraft.common.blocks.crafted.loot;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

@UtilityLikeAbstraction(reason = "I'm just lazy to define state")
public abstract class DefaultAbstractLootBlock extends AbstractLootBlock {
    public static final IntegerProperty DEFAULT_RARITY_PROPERTY = IntegerProperty.create("rarity", 0, Rarity.values().length);
    @Override
    public int getRarityFromState(BlockState state) {
        return state.getValue(DEFAULT_RARITY_PROPERTY);
    }
    public DefaultAbstractLootBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(DEFAULT_RARITY_PROPERTY,0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DEFAULT_RARITY_PROPERTY);
    }
}
