package thaumcraft.common.entities.golems.configuration.providerimpl.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.ByteTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import static com.linearity.opentc4.mixinaccessors.IObjectCacheableEntity.opentc4$getOrCreateCachedObjectFromEntity;

public class GolemConfigurationProviderByteImpl extends GolemConfigurationProvider<Byte> {
    protected final ByteTagAccessor compoundTagAccessor;
    public GolemConfigurationProviderByteImpl(ByteTagAccessor compoundTagAccessor) {
        super();
        this.compoundTagAccessor = compoundTagAccessor;
    }

    public static class ProviderImplToValueMap {
        private final Object2ByteMap<GolemConfigurationProviderByteImpl> internalMap = new Object2ByteOpenHashMap<>();
        public byte get(GolemConfigurationProviderByteImpl provider){
            return internalMap.getByte(provider);
        }
        public void put(GolemConfigurationProviderByteImpl provider, byte value){
            internalMap.put(provider,value);
        }
    }
    public static class GolemConfigurationsFakeFields {
        public ProviderImplToValueMap configurationValueMap = new ProviderImplToValueMap();
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Byte getFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFromEntity(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void setTo(BaseGolemEntity golem, Byte value) {
        var fakeField = opentc4$getOrCreateCachedObjectFromEntity(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Byte serializeFrom(CompoundTag tag) {
        return compoundTagAccessor.readByteFromCompoundTag(tag);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void deserializeTo(CompoundTag tag, Byte value) {
        compoundTagAccessor.writeByteToCompoundTag(tag, value);
    }
    
    public byte getByteFrom(BaseGolemEntity golem) {
        var fakeField = opentc4$getOrCreateCachedObjectFromEntity(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        return fakeField.configurationValueMap.get(this);
    }
    
    public void setByteTo(BaseGolemEntity golem, byte value) {
        var fakeField = opentc4$getOrCreateCachedObjectFromEntity(golem,GolemConfigurationsFakeFields.class, GolemConfigurationsFakeFields::new);
        fakeField.configurationValueMap.put(this,value);
    }

    public byte serializeByteFrom(CompoundTag tag) {
        return compoundTagAccessor.readByteFromCompoundTag(tag);
    }
    public void deserializeByteTo(CompoundTag tag,byte value) {
        compoundTagAccessor.writeByteToCompoundTag(tag,value);
    }
}
