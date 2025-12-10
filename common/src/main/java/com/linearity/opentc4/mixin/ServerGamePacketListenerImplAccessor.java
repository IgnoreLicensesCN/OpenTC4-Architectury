package com.linearity.opentc4.mixin;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(ServerGamePacketListenerImpl.class)
public interface ServerGamePacketListenerImplAccessor {
    @Accessor("aboveGroundTickCount")
    int opentc4$getAboveGroundTickCount();

    @Accessor("aboveGroundTickCount")
    void opentc4$setAboveGroundTickCount(int aboveGroundTickCount);
}
