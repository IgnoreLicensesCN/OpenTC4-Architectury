package com.linearity.opentc4.mixinattachments;

import net.minecraft.world.entity.Entity;

//maybe i should clear goals once entity removed.
//but if i clear once #remove,
// GoalSelector goals iterator may still in some foreach loop
// --ConcurrentModificationException or NullPointerException will happen.
//well...do it next tick.
public interface AvoidEntityMemoryLeakLevel {
    void opentc4$scheduleClearNextTick(Entity entity);
}
