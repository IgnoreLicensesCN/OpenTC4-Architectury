package thaumcraft.api.listeners.lamp.fertility.check;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class FertilityLampAffectiveContext {
    public final Entity entity;
    public boolean endCheck = false;
    public boolean recommendAffective = false;

    public FertilityLampAffectiveContext(Entity entity) {
        this.entity = entity;
    }
}
