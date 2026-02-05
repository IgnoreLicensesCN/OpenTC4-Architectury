package thaumcraft.api.expands.listeners.researchtable;

import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.expands.listeners.researchtable.consts.WriteAspectAfterListenerEnums;

public class WriteAspectManager {
    public static final ListenerManager<WriteAspectBeforeListener> beforeManager = new ListenerManager<>();
    public static final ListenerManager<WriteAspectAfterListener> afterManager = new ListenerManager<>();
    public static void init(){
        for (var afterListener: WriteAspectAfterListenerEnums.values()){
            afterManager.registerListener(afterListener.listener);
        }
    }
}
