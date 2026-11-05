package thaumcraft.common.entities.golems.configuration.providerimpl;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getOrCreateCachedObjectFrom;

public class GolemConfigurationProviderImpl<T> extends GolemConfigurationProvider<T> {
    protected final CompoundTagAccessor<T> compoundTagAccessor;
    protected final Supplier<@NotNull T> defaultValueSupplier;
    public GolemConfigurationProviderImpl(CompoundTagAccessor<T> compoundTagAccessor, Supplier<@NotNull T> defaultValueSupplier) {
        this.compoundTagAccessor = compoundTagAccessor;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    public static class ProviderImplToValueMap {
        private final Map<GolemConfigurationProviderImpl<?>,Object> internalMap = new IdentityHashMap<>();
        public <T> T get(GolemConfigurationProviderImpl<T> provider){
            T value = (T) internalMap.get(provider);
            if (value == null){
                value = provider.defaultValueSupplier.get();
                internalMap.put(provider,value);
            }
            return value;
        }
        public <T> void put(GolemConfigurationProviderImpl<T> provider, T value){
            internalMap.put(provider,value);
        }
    }
    public static class GolemConfigurationsFakeFields {
        public ProviderImplToValueMap configurationValueMap = new ProviderImplToValueMap();
    }

    @Override
    public T getFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class,GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    @Override
    public void setTo(BaseGolemEntity golem, T value) {
        var fakeField = opentc4$getOrCreateCachedObjectFrom(golem,GolemConfigurationsFakeFields.class,GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    @Override
    public T serializeFrom(CompoundTag tag) {
        return compoundTagAccessor.readFromCompoundTag(tag);
    }

    @Override
    public void deserializeTo(CompoundTag tag, T value) {
        compoundTagAccessor.writeToCompoundTag(tag, value);
    }
}
