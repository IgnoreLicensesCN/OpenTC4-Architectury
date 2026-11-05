package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.LongTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderLongImpl extends GolemConfigurationProvider<Long> {
    protected final LongTagAccessor compoundTagAccessor;
    public GolemConfigurationProviderLongImpl(LongTagAccessor compoundTagAccessor) {
        super();
        this.compoundTagAccessor = compoundTagAccessor;
    }

    public static class ProviderImplToValueMap {
        private final Object2LongMap<GolemConfigurationProviderLongImpl> internalMap = new Object2LongOpenHashMap<>();
        public long get(GolemConfigurationProviderLongImpl provider){
            return internalMap.getLong(provider);
        }
        public void put(GolemConfigurationProviderLongImpl provider, long value){
            internalMap.put(provider,value);
        }
    }
    public static class GolemConfigurationsFakeFields {
        public ProviderImplToValueMap configurationValueMap = new ProviderImplToValueMap();
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Long getFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Long value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Long serializeFrom(CompoundTag tag) {
        return compoundTagAccessor.readLongFromCompoundTag(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Long value) {
        compoundTagAccessor.writeLongToCompoundTag(tag, value);
    }

    public long getLongFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    public void setLongTo(BaseGolemEntity golem, long value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    public long serializeLongFrom(CompoundTag tag) {
        return compoundTagAccessor.readLongFromCompoundTag(tag);
    }
    public void deserializeLongTo(CompoundTag tag,long value) {
        compoundTagAccessor.writeLongToCompoundTag(tag,value);
    }
}
