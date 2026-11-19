package thaumcraft.api.listeners.lamp.fertility.check;

import lombok.RequiredArgsConstructor;
import net.minecraft.world.entity.Entity;

@RequiredArgsConstructor
public class FertilityLampAffectiveContext {
    public final Entity entity;
    public boolean endCheck = false;
    public boolean recommendAffective = false;

}
