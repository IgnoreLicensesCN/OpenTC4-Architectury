package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.FloatTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import java.util.function.Supplier;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderFloatImpl extends GolemConfigurationProvider<Float> {
    public final float defaultValue;
    protected final FloatTagAccessor compoundTagAccessor;

    public GolemConfigurationProviderFloatImpl(String accessorName) {
        this(new FloatTagAccessor(accessorName));
    }

    public GolemConfigurationProviderFloatImpl(String accessorName,float defaultValue) {
        this(new FloatTagAccessor(accessorName),defaultValue);
    }

    public GolemConfigurationProviderFloatImpl(FloatTagAccessor compoundTagAccessor) {
        this(compoundTagAccessor,0F);
    }

    public GolemConfigurationProviderFloatImpl(FloatTagAccessor compoundTagAccessor,float defaultValue) {
        this.compoundTagAccessor = compoundTagAccessor;
        var valueMap = new Object2FloatOpenHashMap<GolemConfigurationProviderFloatImpl>();
        valueMap.defaultReturnValue(defaultValue);
        this.fakeFieldsSupplier = () -> new GolemConfigurationsFakeFields(new ProviderImplToValueMap(valueMap));
        this.defaultValue = defaultValue;
    }

    public static class ProviderImplToValueMap {
        private final Object2FloatMap<GolemConfigurationProviderFloatImpl> internalMap;

        public ProviderImplToValueMap() {
            this.internalMap = new Object2FloatOpenHashMap<>();
        }

        public ProviderImplToValueMap(Object2FloatMap<GolemConfigurationProviderFloatImpl> internalMap) {
            this.internalMap = internalMap;
        }

        public float get(GolemConfigurationProviderFloatImpl provider){
            return internalMap.getFloat(provider);
        }

        public void put(GolemConfigurationProviderFloatImpl provider, float value){
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
    public Float getFrom(BaseGolemEntity golem) {
        return getFloatFrom(golem);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Float value) {
        setFloatTo(golem,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Float serializeFrom(CompoundTag tag) {
        return serializeFloatFrom(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Float value) {
        deserializeFloatTo(tag,value);
    }

    public float getFloatFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, fakeFieldsSupplier);
        return fakeField.configurationValueMap.get(this);
    }

    public void setFloatTo(BaseGolemEntity golem, float value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, fakeFieldsSupplier);
        fakeField.configurationValueMap.put(this,value);
    }

    public float serializeFloatFrom(CompoundTag tag) {
        return compoundTagAccessor.readFloatFromCompoundTag(tag);
    }
    public void deserializeFloatTo(CompoundTag tag,float value) {
        compoundTagAccessor.writeFloatToCompoundTag(tag,value);
    }

    protected Supplier<GolemConfigurationsFakeFields> fakeFieldsSupplier = GolemConfigurationsFakeFields::new;
}
