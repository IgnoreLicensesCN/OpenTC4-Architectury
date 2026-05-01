package thaumcraft.api.listeners.manabean;

import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.listeners.manabean.consts.ManaBeanEatListeners;
import thaumcraft.api.listeners.manabean.listeners.ManaBeanEatListener;

public class ManaBeanEatManager {
    public static final ListenerManager<ManaBeanEatListener> eatListeners = new ListenerManager<>();
    static {
        for (var listenerEnum: ManaBeanEatListeners.values()) {
            eatListeners.registerListener(listenerEnum.listener);
        }
    }
    public static void onEatManaBean(ManaBeanEatContext context){
        eatListeners.getListeners().forEach(listener -> listener.onEatManaBean(context));
    }
}
