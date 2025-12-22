package com.linearity.opentc4.clientrenderapi;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

//randomDisplayTick if you want
public interface IClientRandomTickableBlock {
    void onClientRandomTick(BlockState blockState, ClientLevel level, BlockPos blockPos);
}
