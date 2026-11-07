package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.DoubleTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderDoubleImpl extends GolemConfigurationProvider<Double> {
    protected final DoubleTagAccessor compoundTagAccessor;
    public GolemConfigurationProviderDoubleImpl(DoubleTagAccessor compoundTagAccessor) {
        super();
        this.compoundTagAccessor = compoundTagAccessor;
    }

    public static class ProviderImplToValueMap {
        private final Object2DoubleMap<GolemConfigurationProviderDoubleImpl> internalMap = new Object2DoubleOpenHashMap<>();
        public double get(GolemConfigurationProviderDoubleImpl provider){
            return internalMap.getDouble(provider);
        }
        public void put(GolemConfigurationProviderDoubleImpl provider, double value){
            internalMap.put(provider,value);
        }
    }
    public static class GolemConfigurationsFakeFields implements IObjectCacheableFakeFieldOwner.FakeFieldAttachable<BaseGolemEntity> {
        public ProviderImplToValueMap configurationValueMap = new ProviderImplToValueMap();
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Double getFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Double value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Double serializeFrom(CompoundTag tag) {
        return compoundTagAccessor.readDoubleFromCompoundTag(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Double value) {
        compoundTagAccessor.writeDoubleToCompoundTag(tag, value);
    }

    public double getDoubleFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    public void setDoubleTo(BaseGolemEntity golem, double value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    public double serializeDoubleFrom(CompoundTag tag) {
        return compoundTagAccessor.readDoubleFromCompoundTag(tag);
    }
    public void deserializeDoubleTo(CompoundTag tag,double value) {
        compoundTagAccessor.writeDoubleToCompoundTag(tag,value);
    }
}
