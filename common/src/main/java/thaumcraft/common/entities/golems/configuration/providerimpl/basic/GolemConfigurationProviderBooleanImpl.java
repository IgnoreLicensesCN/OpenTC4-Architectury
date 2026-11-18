package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.BooleanTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import java.util.function.Supplier;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderBooleanImpl extends GolemConfigurationProvider<Boolean> {
    public final boolean defaultValue;
    protected final BooleanTagAccessor compoundTagAccessor;

    public GolemConfigurationProviderBooleanImpl(String accessorName) {
        this(new BooleanTagAccessor(accessorName));
    }

    public GolemConfigurationProviderBooleanImpl(String accessorName,boolean defaultValue) {
        this(new BooleanTagAccessor(accessorName),defaultValue);
    }

    public GolemConfigurationProviderBooleanImpl(BooleanTagAccessor compoundTagAccessor) {
        this(compoundTagAccessor,false);
    }

    public GolemConfigurationProviderBooleanImpl(BooleanTagAccessor compoundTagAccessor,boolean defaultValue) {
        this.compoundTagAccessor = compoundTagAccessor;
        var valueMap = new Object2BooleanOpenHashMap<GolemConfigurationProviderBooleanImpl>();
        valueMap.defaultReturnValue(defaultValue);
        this.fakeFieldsSupplier = () -> new GolemConfigurationsFakeFields(new ProviderImplToValueMap(valueMap));
        this.defaultValue = defaultValue;
    }

    public static class ProviderImplToValueMap {
        private final Object2BooleanMap<GolemConfigurationProviderBooleanImpl> internalMap;

        public ProviderImplToValueMap() {
            this.internalMap = new Object2BooleanOpenHashMap<>();
        }

        public ProviderImplToValueMap(Object2BooleanMap<GolemConfigurationProviderBooleanImpl> internalMap) {
            this.internalMap = internalMap;
        }

        public boolean get(GolemConfigurationProviderBooleanImpl provider){
            return internalMap.getBoolean(provider);
        }

        public void put(GolemConfigurationProviderBooleanImpl provider, boolean value){
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
    public Boolean getFrom(BaseGolemEntity golem) {
        return getBooleanFrom(golem);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Boolean value) {
        setBooleanTo(golem,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Boolean serializeFrom(CompoundTag tag) {
        return serializeBooleanFrom(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Boolean value) {
        deserializeBooleanTo(tag,value);
    }

    public boolean getBooleanFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, fakeFieldsSupplier);
        return fakeField.configurationValueMap.get(this);
    }

    public void setBooleanTo(BaseGolemEntity golem, boolean value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, fakeFieldsSupplier);
        fakeField.configurationValueMap.put(this,value);
    }

    public boolean serializeBooleanFrom(CompoundTag tag) {
        return compoundTagAccessor.readBooleanFromCompoundTag(tag);
    }
    public void deserializeBooleanTo(CompoundTag tag,boolean value) {
        compoundTagAccessor.writeBooleanToCompoundTag(tag,value);
    }

    protected Supplier<GolemConfigurationsFakeFields> fakeFieldsSupplier = GolemConfigurationsFakeFields::new;
}
