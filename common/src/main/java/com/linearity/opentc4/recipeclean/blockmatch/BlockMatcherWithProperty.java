package com.linearity.opentc4.recipeclean.blockmatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

public class BlockMatcherWithProperty extends AbstractBlockMatcher {
    public BlockMatcherWithProperty(Block block, PropertyMap<?> properties) {
        this.block = block;
        this.properties = properties;
    }

    public static class PropertyMap<T extends Comparable<T>> extends HashMap<Property<T>,T> {}
    private final Block block;
    private final PropertyMap<?> properties;

    @Override
    public boolean match(@Nullable Level atLevel, @NotNull BlockState state, @NotNull BlockPos pos) {
        if (state.getBlock() != block) {
            return false;
        }
        var propertiesInState = state.getProperties();
        if (propertiesInState.size() != properties.size()) {
            return false;
        }
        for (var property : propertiesInState){
            var value = properties.get(property);
            if (value == null) {
                return false;
            }
            var matchingPropertyValue = state.getValue(property);
            if (!Objects.equals(matchingPropertyValue,value)){
                return false;
            }
        }
        return true;
    }
}
