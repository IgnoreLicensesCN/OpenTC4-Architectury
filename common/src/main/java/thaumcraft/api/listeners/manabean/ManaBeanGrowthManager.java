package thaumcraft.api.listeners.manabean;

import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.listeners.manabean.consts.ManaBeanGrowListeners;
import thaumcraft.api.listeners.manabean.listeners.ManaBeanGrowListener;

public class ManaBeanGrowthManager {
    public static final ListenerManager<ManaBeanGrowListener> growListeners = new ListenerManager<>();
    static {
        for (var listenerEnum: ManaBeanGrowListeners.values()) {
            growListeners.registerListener(listenerEnum.listener);
        }
    }
    public static void onGrowStageChanged(ManaBeanGrowContext context){
        growListeners.getListeners().forEach(listener -> listener.onGrowStageChanged(context));
    }
}
