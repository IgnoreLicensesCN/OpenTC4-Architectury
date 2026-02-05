package thaumcraft.api.expands.listeners.researchtable.consts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.listeners.researchtable.WriteAspectAfterListener;
import thaumcraft.common.lib.utils.HexCoord;

public enum WriteAspectAfterListenerEnums {
    RESEARCH_EXPERTISE(new WriteAspectAfterListener(0) {
        @Override
        public void onEventTriggered(Level atLevel, BlockPos tablePos, ItemStack writeToolStack, ItemStack noteStack, ServerPlayer player, Aspect aspect, HexCoord placedAt) {

        }
    }),
    RESEARCH_MASTERY(new WriteAspectAfterListener(0) {
        @Override
        public void onEventTriggered(Level atLevel, BlockPos tablePos, ItemStack writeToolStack, ItemStack noteStack, ServerPlayer player, Aspect aspect, HexCoord placedAt) {

        }
    }),
    ;

    public final WriteAspectAfterListener listener;
    WriteAspectAfterListenerEnums(WriteAspectAfterListener listener) {
        this.listener = listener;
    }
}
