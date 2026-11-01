package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.ShortTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableEntity.opentc4$getOrCreateCachedObjectFromEntity;

public class GolemConfigurationProviderShortImpl extends GolemConfigurationProvider<Short> {
    protected final ShortTagAccessor compoundTagAccessor;
    public GolemConfigurationProviderShortImpl(ShortTagAccessor compoundTagAccessor) {
        super();
        this.compoundTagAccessor = compoundTagAccessor;
    }

    public static class ProviderImplToValueMap {
        private final Object2ShortMap<GolemConfigurationProviderShortImpl> internalMap = new Object2ShortOpenHashMap<>();
        public short get(GolemConfigurationProviderShortImpl provider){
            return internalMap.getShort(provider);
        }
        public void put(GolemConfigurationProviderShortImpl provider, short value){
            internalMap.put(provider,value);
        }
    }
    public static class GolemConfigurationsFakeFields {
        public ProviderImplToValueMap configurationValueMap = new ProviderImplToValueMap();
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Short getFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFromEntity(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Short value) {
        var fakeField = opentc4$getOrCreateCachedObjectFromEntity(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Short serializeFrom(CompoundTag tag) {
        return compoundTagAccessor.readShortFromCompoundTag(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Short value) {
        compoundTagAccessor.writeShortToCompoundTag(tag, value);
    }

    public short getShortFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFromEntity(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    public void setShortTo(BaseGolemEntity golem, short value) {
        var fakeField = opentc4$getOrCreateCachedObjectFromEntity(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    public short serializeShortFrom(CompoundTag tag) {
        return compoundTagAccessor.readShortFromCompoundTag(tag);
    }
    public void deserializeShortTo(CompoundTag tag,short value) {
        compoundTagAccessor.writeShortToCompoundTag(tag,value);
    }
}
