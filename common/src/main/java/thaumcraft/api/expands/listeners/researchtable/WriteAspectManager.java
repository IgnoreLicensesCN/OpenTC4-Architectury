package thaumcraft.api.expands.listeners.researchtable;

import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.listeners.researchtable.consts.WriteAspectAfterListenerEnums;
import thaumcraft.common.lib.utils.HexCoord;

public class WriteAspectManager {
    public static final ListenerManager<WriteAspectBeforeListener> beforeManager = new ListenerManager<>();
    public static final ListenerManager<WriteAspectAfterListener> afterManager = new ListenerManager<>();
    public static void init(){
        for (var afterListener: WriteAspectAfterListenerEnums.values()){
            afterManager.registerListener(afterListener.listener);
        }
    }
    public static void beforeWriteAspect(
            Level atLevel,
            BlockPos tablePos,
            ItemStack writeToolStack,
            ItemStack noteStack,
            ServerPlayer player,
            Aspect aspect,
            HexCoord placedAt
    ) {
        for (var beforeListener:beforeManager.getListeners()){
            beforeListener.onEventTriggered(
                    atLevel,
                    tablePos,
                    writeToolStack,
                    noteStack,
                    player,
                    aspect,
                    placedAt
            );
        }
    }

    public static void afterWriteAspect(
            Level atLevel,
            BlockPos tablePos,
            ItemStack writeToolStack,
            ItemStack noteStack,
            ServerPlayer player,
            Aspect aspect,
            HexCoord placedAt
    ){
        for (var afterListener:afterManager.getListeners()){
            afterListener.onEventTriggered(
                    atLevel,
                    tablePos,
                    writeToolStack,
                    noteStack,
                    player,
                    aspect,
                    placedAt
            );
        }
    }
}
