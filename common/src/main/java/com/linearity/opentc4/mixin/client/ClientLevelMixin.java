package com.linearity.opentc4.mixin.client;

import com.linearity.opentc4.mixinattachments.AvoidEntityMemoryLeakLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static thaumcraft.common.entities.ThaumcraftEntityEvents.avoidMemoryLeakOnRemoved;

@Mixin(ClientLevel.class)
public class ClientLevelMixin implements AvoidEntityMemoryLeakLevel {

    @Unique
    private final Queue<Entity> opentc4$entitiesToAvoidMemoryLeak = new ConcurrentLinkedQueue<>();

    @Override
    public void opentc4$scheduleClearNextTick(Entity entity) {
        opentc4$entitiesToAvoidMemoryLeak.add(entity);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void opentc4$clientLevelTick(CallbackInfo ci) {
        while (!opentc4$entitiesToAvoidMemoryLeak.isEmpty()) {
            var entity = opentc4$entitiesToAvoidMemoryLeak.remove();
            avoidMemoryLeakOnRemoved(entity);
        }
    }
}
