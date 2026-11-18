package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.LongTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import java.util.function.Supplier;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderLongImpl extends GolemConfigurationProvider<Long> {
    public final long defaultValue;
    protected final LongTagAccessor compoundTagAccessor;

    public GolemConfigurationProviderLongImpl(String accessorName) {
        this(new LongTagAccessor(accessorName));
    }

    public GolemConfigurationProviderLongImpl(String accessorName,long defaultValue) {
        this(new LongTagAccessor(accessorName),defaultValue);
    }

    public GolemConfigurationProviderLongImpl(LongTagAccessor compoundTagAccessor) {
        this(compoundTagAccessor,0L);
    }

    public GolemConfigurationProviderLongImpl(LongTagAccessor compoundTagAccessor,long defaultValue) {
        this.compoundTagAccessor = compoundTagAccessor;
        var valueMap = new Object2LongOpenHashMap<GolemConfigurationProviderLongImpl>();
        valueMap.defaultReturnValue(defaultValue);
        this.fakeFieldsSupplier = () -> new GolemConfigurationsFakeFields(new ProviderImplToValueMap(valueMap));
        this.defaultValue = defaultValue;
    }

    public static class ProviderImplToValueMap {
        private final Object2LongMap<GolemConfigurationProviderLongImpl> internalMap;

        public ProviderImplToValueMap() {
            this.internalMap = new Object2LongOpenHashMap<>();
        }

        public ProviderImplToValueMap(Object2LongMap<GolemConfigurationProviderLongImpl> internalMap) {
            this.internalMap = internalMap;
        }

        public long get(GolemConfigurationProviderLongImpl provider){
            return internalMap.getLong(provider);
        }

        public void put(GolemConfigurationProviderLongImpl provider, long value){
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
    public Long getFrom(BaseGolemEntity golem) {
        return getLongFrom(golem);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Long value) {
        setLongTo(golem,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Long serializeFrom(CompoundTag tag) {
        return serializeLongFrom(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Long value) {
        deserializeLongTo(tag,value);
    }

    public long getLongFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, fakeFieldsSupplier);
        return fakeField.configurationValueMap.get(this);
    }

    public void setLongTo(BaseGolemEntity golem, long value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, fakeFieldsSupplier);
        fakeField.configurationValueMap.put(this,value);
    }

    public long serializeLongFrom(CompoundTag tag) {
        return compoundTagAccessor.readLongFromCompoundTag(tag);
    }
    public void deserializeLongTo(CompoundTag tag,long value) {
        compoundTagAccessor.writeLongToCompoundTag(tag,value);
    }

    protected Supplier<GolemConfigurationsFakeFields> fakeFieldsSupplier = GolemConfigurationsFakeFields::new;
}
