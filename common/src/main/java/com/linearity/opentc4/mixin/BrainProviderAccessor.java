package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Collection;

@Mixin(Brain.Provider.class)
public interface BrainProviderAccessor {
    @Accessor("memoryTypes")
    Collection<? extends MemoryModuleType<?>> opentc4$getMemoryTypes();
    @Accessor("memoryTypes")
    void opentc4$setMemoryTypes(Collection<? extends MemoryModuleType<?>> memoryTypes);
    @Accessor("sensorTypes")
    Collection<? extends SensorType<?>> opentc4$getSensorTypes();
    @Accessor("sensorTypes")
    void opentc4$setSensorTypes(Collection<?> sensorTypes);
}
