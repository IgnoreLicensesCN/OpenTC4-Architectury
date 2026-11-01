package thaumcraft.common.entities.golems.configuration;

import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.resourcelocations.GolemConfigurationResourceLocation;

public abstract class GolemConfigurationProvider<T> {

    public abstract T getFrom(BaseGolemEntity golem);
    public abstract void setTo(BaseGolemEntity golem, T value);
    public abstract T serializeFrom(CompoundTag tag);
    public abstract void deserializeTo(CompoundTag tag,T value);
}
