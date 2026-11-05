package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.BooleanTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderBooleanImpl extends GolemConfigurationProvider<Boolean> {
    protected final BooleanTagAccessor compoundTagAccessor;
    public GolemConfigurationProviderBooleanImpl(BooleanTagAccessor compoundTagAccessor) {
        this.compoundTagAccessor = compoundTagAccessor;
    }
    public GolemConfigurationProviderBooleanImpl(String accessorName) {
        this(new BooleanTagAccessor(accessorName));
    }

    public static class ProviderImplToValueMap {
        private final Object2BooleanMap<GolemConfigurationProviderBooleanImpl> internalMap = new Object2BooleanOpenHashMap<>();
        public boolean get(GolemConfigurationProviderBooleanImpl provider){
            return internalMap.getBoolean(provider);
        }
        public void put(GolemConfigurationProviderBooleanImpl provider, boolean value){
            internalMap.put(provider,value);
        }
    }
    public static class GolemConfigurationsFakeFields {
        public ProviderImplToValueMap configurationValueMap = new ProviderImplToValueMap();
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Boolean getFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Boolean value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Boolean serializeFrom(CompoundTag tag) {
        return compoundTagAccessor.readBooleanFromCompoundTag(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Boolean value) {
        compoundTagAccessor.writeBooleanToCompoundTag(tag, value);
    }

    public boolean getBooleanFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    public void setBooleanTo(BaseGolemEntity golem, boolean value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    public boolean serializeBooleanFrom(CompoundTag tag) {
        return compoundTagAccessor.readBooleanFromCompoundTag(tag);
    }
    public void deserializeBooleanTo(CompoundTag tag,boolean value) {
        compoundTagAccessor.writeBooleanToCompoundTag(tag,value);
    }
}
