package thaumcraft.api.listeners.aspects.item.basic.getters;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectGetListeners;
import thaumcraft.api.listeners.aspects.item.basic.additional.AddAdditionalBasicAspectContext;
import thaumcraft.api.listeners.aspects.item.basic.additional.consts.AddAdditionalBasicAspectListeners;
import thaumcraft.api.listeners.aspects.item.basic.additional.listeners.AddAdditionalBasicAspectListener;
import thaumcraft.api.listeners.aspects.item.basic.getters.listeners.ItemBasicAspectGetListener;
import thaumcraft.common.lib.network.gamedata.PacketSyncItemAspectsC2S;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
    private static final Map<Item,AspectList<Aspect>> CACHE = new HashMap<>();
    public static final Map<Item, AspectList<Aspect>> CLIENT_CACHE = new HashMap<>();
    public static final AtomicLong REQUESTED_ASPECT_LIST_AT = new AtomicLong(0);
    public static final long REQUEST_ASPECT_LIST_COOLDOWN = 5000;//milliseconds
    //expose to outer to get basic aspects
    public static AspectList<Aspect> getBasicAspects(@NotNull Item i,boolean isClientSide){
        return isClientSide ? getBasicAspectsClient(i) : getBasicAspectsServer(i);
    }
    public static AspectList<Aspect> getBasicAspectsClient(@NotNull Item i) {

        if (CLIENT_CACHE.isEmpty()
                && System.currentTimeMillis() - REQUESTED_ASPECT_LIST_AT.get() > REQUEST_ASPECT_LIST_COOLDOWN
        ) {
            REQUESTED_ASPECT_LIST_AT.set(System.currentTimeMillis());
            new PacketSyncItemAspectsC2S().sendToServer();
        }
        return CLIENT_CACHE.getOrDefault(i,UnmodifiableAspectList.EMPTY);
    }
    public static AspectList<Aspect> getBasicAspectsServer(@NotNull Item i) {
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
        BuiltInRegistries.ITEM.forEach(ItemBasicAspectGetter::getBasicAspectsServer);
    }

}
