package thaumcraft.api.listeners.infusion.instabilityevent;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;

public class InfusionInstabilityEventManager {
    public static final ListenerManager<InfusionInstabilityEventListener> INFUSION_INSTABILITY_EVENT_MANAGER = new ListenerManager<>();
    static {
        for (var listenerEnum:InfusionInstabilityEventListeners.values()){
            INFUSION_INSTABILITY_EVENT_MANAGER.registerListener(listenerEnum.listener);
        }
    }

    public static void pickAndTriggerEvent(InfusionInstabilityEventListener.InfusionInstabilityEventContext infusionInstabilityEventContext){
        INFUSION_INSTABILITY_EVENT_MANAGER.getListeners().forEach(
                listener -> listener.onInstabilityEvent(infusionInstabilityEventContext)
        );
    }
}
