package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.DoubleTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import java.util.function.Supplier;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderDoubleImpl extends GolemConfigurationProvider<Double> {
    public final double defaultValue;
    protected final DoubleTagAccessor compoundTagAccessor;

    public GolemConfigurationProviderDoubleImpl(String accessorName) {
        this(new DoubleTagAccessor(accessorName));
    }

    public GolemConfigurationProviderDoubleImpl(String accessorName,double defaultValue) {
        this(new DoubleTagAccessor(accessorName),defaultValue);
    }

    public GolemConfigurationProviderDoubleImpl(DoubleTagAccessor compoundTagAccessor) {
        this(compoundTagAccessor,0.);
    }

    public GolemConfigurationProviderDoubleImpl(DoubleTagAccessor compoundTagAccessor,double defaultValue) {
        this.compoundTagAccessor = compoundTagAccessor;
        var valueMap = new Object2DoubleOpenHashMap<GolemConfigurationProviderDoubleImpl>();
        valueMap.defaultReturnValue(defaultValue);
        this.fakeFieldsSupplier = () -> new GolemConfigurationsFakeFields(new ProviderImplToValueMap(valueMap));
        this.defaultValue = defaultValue;
    }

    public static class ProviderImplToValueMap {
        private final Object2DoubleMap<GolemConfigurationProviderDoubleImpl> internalMap;

        public ProviderImplToValueMap() {
            this.internalMap = new Object2DoubleOpenHashMap<>();
        }

        public ProviderImplToValueMap(Object2DoubleMap<GolemConfigurationProviderDoubleImpl> internalMap) {
            this.internalMap = internalMap;
        }

        public double get(GolemConfigurationProviderDoubleImpl provider){
            return internalMap.getDouble(provider);
        }

        public void put(GolemConfigurationProviderDoubleImpl provider, double value){
            internalMap.put(provider,value);
        }
    }
    public static class GolemConfigurationsFakeFields implements IObjectCacheableFakeFieldOwner.FakeFieldAttachable<BaseGolemEntity> {
        public final ProviderImplToValueMap configurationValueMap;
        public GolemConfigurationsFakeFields(ProviderImplToValueMap configurationValueMap) {
            this.configurationValueMap = configurationValueMap;
        }

        public GolemConfigurationsFakeFields(){
            this.configurationValueMap = new ProviderImplToValueMap();
        }
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Double getFrom(BaseGolemEntity golem) {
        return getDoubleFrom(golem);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Double value) {
        setDoubleTo(golem,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Double serializeFrom(CompoundTag tag) {
        return serializeDoubleFrom(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Double value) {
        deserializeDoubleTo(tag,value);
    }

    public double getDoubleFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, fakeFieldsSupplier);
        return fakeField.configurationValueMap.get(this);
    }

    public void setDoubleTo(BaseGolemEntity golem, double value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, fakeFieldsSupplier);
        fakeField.configurationValueMap.put(this,value);
    }

    public double serializeDoubleFrom(CompoundTag tag) {
        return compoundTagAccessor.readDoubleFromCompoundTag(tag);
    }
    public void deserializeDoubleTo(CompoundTag tag,double value) {
        compoundTagAccessor.writeDoubleToCompoundTag(tag,value);
    }

    protected Supplier<GolemConfigurationsFakeFields> fakeFieldsSupplier = GolemConfigurationsFakeFields::new;
}
