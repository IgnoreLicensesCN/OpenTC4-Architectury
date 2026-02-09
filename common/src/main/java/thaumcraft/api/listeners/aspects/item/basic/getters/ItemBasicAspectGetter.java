package thaumcraft.api.listeners.aspects.item.basic.getters;

import com.linearity.opentc4.simpleutils.ListenerManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectGetListeners;
import thaumcraft.api.listeners.aspects.item.basic.additional.AddAdditionalBasicAspectContext;
import thaumcraft.api.listeners.aspects.item.basic.additional.consts.AddAdditionalBasicAspectListeners;
import thaumcraft.api.listeners.aspects.item.basic.additional.listeners.AddAdditionalBasicAspectListener;
import thaumcraft.api.listeners.aspects.item.basic.getters.listeners.ItemBasicAspectGetListener;
import thaumcraft.common.lib.network.gamedata.PacketSyncItemAspectsC2S;

import java.util.HashMap;
import java.util.Map;

public class ItemBasicAspectGetter {
    public static final ListenerManager<ItemBasicAspectGetListener> basicListeners = new ListenerManager<>();
    public static final ListenerManager<AddAdditionalBasicAspectListener> additionalListeners = new ListenerManager<>();
    static {
        for (var getListeners: ItemBasicAspectGetListeners.values()) {
            basicListeners.registerListener(getListeners.listener);
        }
        for (var addListeners: AddAdditionalBasicAspectListeners.values()) {
            additionalListeners.registerListener(addListeners.listener);
        }
    }
    private static final Map<Item,UnmodifiableAspectList<Aspect>> CACHE = new HashMap<>();
    public static final Map<Item,UnmodifiableAspectList<Aspect>> CLIENT_CACHE = new HashMap<>();
    //expose to outer to get basic aspects
    public static UnmodifiableAspectList<Aspect> getBasicAspects(@NotNull Item i) {
        if (Platform.getEnvironment() != Env.SERVER) {
            if (CLIENT_CACHE.isEmpty()){
                new PacketSyncItemAspectsC2S().sendToServer();
            }
            return CLIENT_CACHE.getOrDefault(i,UnmodifiableAspectList.EMPTY);
        }

        return CACHE.computeIfAbsent(i,item -> {
            var getContext = new ItemBasicAspectGetContext(item);
            for (var listener: basicListeners.getListeners()) {
                listener.getBasicAspects(getContext);
                if (getContext.doFinishGetting){
                    break;
                }
            }
            var calculated= getContext.result;
            var addContext = new AddAdditionalBasicAspectContext(item, calculated);
            for (var listener: additionalListeners.getListeners()) {
                listener.considerAddAdditional(addContext);
                if (addContext.doReturn){
                    break;
                }
            }
            if (!addContext.additionalAspects.isEmpty()){
                calculated = calculated.addAllAsNew(addContext.additionalAspects);
            }
            return calculated;
        });
    }
    public static void onDatapackReload(){
        CACHE.clear();
        BuiltInRegistries.ITEM.forEach(ItemBasicAspectGetter::getBasicAspects);
    }

}
