package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderIntegerImpl extends GolemConfigurationProvider<Integer> {
    protected final IntTagAccessor compoundTagAccessor;
    public GolemConfigurationProviderIntegerImpl(IntTagAccessor compoundTagAccessor) {
        super();
        this.compoundTagAccessor = compoundTagAccessor;
    }

    public static class ProviderImplToValueMap {
        private final Object2IntMap<GolemConfigurationProviderIntegerImpl> internalMap = new Object2IntOpenHashMap<>();
        public int get(GolemConfigurationProviderIntegerImpl provider){
            return internalMap.getInt(provider);
        }
        public void put(GolemConfigurationProviderIntegerImpl provider, int value){
            internalMap.put(provider,value);
        }
    }
    public static class GolemConfigurationsFakeFields {
        public ProviderImplToValueMap configurationValueMap = new ProviderImplToValueMap();
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Integer getFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Integer value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Integer serializeFrom(CompoundTag tag) {
        return compoundTagAccessor.readIntFromCompoundTag(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Integer value) {
        compoundTagAccessor.writeIntToCompoundTag(tag, value);
    }
    
    public int getIntegerFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }
    
    public void setIntegerTo(BaseGolemEntity golem, int value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    public int serializeIntegerFrom(CompoundTag tag) {
        return compoundTagAccessor.readIntFromCompoundTag(tag);
    }
    public void deserializeIntegerTo(CompoundTag tag,int value) {
        compoundTagAccessor.writeIntToCompoundTag(tag,value);
    }
}
