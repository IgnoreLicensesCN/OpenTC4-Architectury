package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.FloatTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderFloatImpl extends GolemConfigurationProvider<Float> {
    protected final FloatTagAccessor compoundTagAccessor;
    public GolemConfigurationProviderFloatImpl(FloatTagAccessor compoundTagAccessor) {
        super();
        this.compoundTagAccessor = compoundTagAccessor;
    }

    public static class ProviderImplToValueMap {
        private final Object2FloatMap<GolemConfigurationProviderFloatImpl> internalMap = new Object2FloatOpenHashMap<>();
        public float get(GolemConfigurationProviderFloatImpl provider){
            return internalMap.getFloat(provider);
        }
        public void put(GolemConfigurationProviderFloatImpl provider, float value){
            internalMap.put(provider,value);
        }
    }
    public static class GolemConfigurationsFakeFields implements IObjectCacheableFakeFieldOwner.FakeFieldAttachable<BaseGolemEntity> {
        public ProviderImplToValueMap configurationValueMap = new ProviderImplToValueMap();
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Float getFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Float value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Float serializeFrom(CompoundTag tag) {
        return compoundTagAccessor.readFloatFromCompoundTag(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Float value) {
        compoundTagAccessor.writeFloatToCompoundTag(tag, value);
    }

    public float getFloatFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    public void setFloatTo(BaseGolemEntity golem, float value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    public float serializeFloatFrom(CompoundTag tag) {
        return compoundTagAccessor.readFloatFromCompoundTag(tag);
    }
    public void deserializeFloatTo(CompoundTag tag,float value) {
        compoundTagAccessor.writeFloatToCompoundTag(tag,value);
    }
}
