package thaumcraft.common.lib.network.toserveraction.scan;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.Thaumcraft;

import static thaumcraft.api.scan.ScanManager.onPlayerScanItemStack;

//oh it's for extension?or for TODO:[maybe wont finished]inventory scan
public class PacketScannedItemStackC2S extends BaseC2SMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":scanned_stack_2s";
    public static MessageType messageType;

    @Override
    public MessageType getType() {
        return messageType;
    }

    private final ItemStack stack;
    public PacketScannedItemStackC2S(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }
    public static PacketScannedItemStackC2S read(FriendlyByteBuf buf) {
        return new PacketScannedItemStackC2S(buf.readItem());
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            var player = context.getPlayer();
            if (!(player instanceof ServerPlayer serverPlayer)) {return;}
            if (stack == null || stack.isEmpty()) {return;}

            onPlayerScanItemStack(serverPlayer, stack);
        });
    }
}
