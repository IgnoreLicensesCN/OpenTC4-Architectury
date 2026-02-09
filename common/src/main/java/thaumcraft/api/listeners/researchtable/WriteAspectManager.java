package thaumcraft.api.listeners.researchtable;

import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.listeners.researchtable.consts.RemoveAspectAfterListenerEnums;
import thaumcraft.api.listeners.researchtable.consts.RemoveAspectBeforeListenerEnums;
import thaumcraft.api.listeners.researchtable.consts.WriteAspectAfterListenerEnums;
import thaumcraft.api.listeners.researchtable.consts.WriteAspectBeforeListenerEnums;
import thaumcraft.api.listeners.researchtable.listeners.RemoveAspectAfterListener;
import thaumcraft.api.listeners.researchtable.listeners.RemoveAspectBeforeListener;
import thaumcraft.api.listeners.researchtable.listeners.WriteAspectAfterListener;
import thaumcraft.api.listeners.researchtable.listeners.WriteAspectBeforeListener;

public class WriteAspectManager {
    public static final ListenerManager<RemoveAspectBeforeListener> beforeRemoveManager = new ListenerManager<>();
    public static final ListenerManager<RemoveAspectAfterListener> afterRemoveManager = new ListenerManager<>();
    public static final ListenerManager<WriteAspectBeforeListener> beforeWriteManager = new ListenerManager<>();
    public static final ListenerManager<WriteAspectAfterListener> afterWriteManager = new ListenerManager<>();
    public static void init(){
        for (var beforeListener : WriteAspectBeforeListenerEnums.values()) {
            beforeWriteManager.registerListener(beforeListener.listener);
        }
        for (var afterListener: WriteAspectAfterListenerEnums.values()){
            afterWriteManager.registerListener(afterListener.listener);
        }
        for (var beforeListener : RemoveAspectBeforeListenerEnums.values()) {
            beforeRemoveManager.registerListener(beforeListener.listener);
        }
        for (var afterListener : RemoveAspectAfterListenerEnums.values()) {
            afterRemoveManager.registerListener(afterListener.listener);
        }
    }
    public static void beforeWriteAspect(WriteAspectContext context) {
        for (var beforeListener: beforeWriteManager.getListeners()){
            beforeListener.onEventTriggered(context);
        }
    }

    public static void afterWriteAspect(WriteAspectContext context){
        for (var afterListener: afterWriteManager.getListeners()){
            afterListener.onEventTriggered(context);
        }
    }

    public static void beforeRemoveAspect(RemoveAspectContext context) {
        for (var beforeListener: beforeRemoveManager.getListeners()){
            beforeListener.onEventTriggered(context);
        }
    }
    public static void afterRemoveAspect(RemoveAspectContext context){
        for (var afterListener: afterRemoveManager.getListeners()){
            afterListener.onEventTriggered(context);
        }
    }
}
