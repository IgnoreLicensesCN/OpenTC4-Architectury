package thaumcraft.common.lib.network.misc;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.golems.ItemGolemBell;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;

public class PacketItemKeyC2S extends BaseC2SMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":item_key";

    public static MessageType messageType;

    private final ResourceKey<Level> dim;
    private final byte key;
    private final int hand; // 0 = OFF_HAND, 1 = MAIN_HAND

    public PacketItemKeyC2S(ResourceKey<Level> dim, int key, int hand) {
        this.dim = dim;
        this.key = (byte) key;
        this.hand = hand;
    }

    /* ---------------- 解码 / 编码 ---------------- */

    public static PacketItemKeyC2S decode(FriendlyByteBuf buf) {
        ResourceKey<Level> dim = buf.readResourceKey(Registries.DIMENSION);
        byte key = buf.readByte();
        int hand = buf.readInt();
        return new PacketItemKeyC2S(dim, key, hand);
    }

    public static void encode(PacketItemKeyC2S msg, FriendlyByteBuf buf) {
        buf.writeResourceKey(msg.dim);
        buf.writeByte(msg.key);
        buf.writeInt(msg.hand);
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        encode(this, buf);
    }

    /* ---------------- 服务端处理 ---------------- */

    @Override
    public void handle(NetworkManager.PacketContext ctx) {
        Player player = ctx.getPlayer();
        if (player == null || !(player instanceof ServerPlayer serverPlayer)) return;

        Level level = serverPlayer.server.getLevel(dim);
        if (level == null || level != player.level()) return;

        InteractionHand interactionHand =
                (hand == 0) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;

        ItemStack stack = player.getItemInHand(interactionHand);
        if (stack.isEmpty()) return;

        /* --------- 对应旧版逻辑 --------- */

        // key == 0 : Golem Bell 重置标记
        if (key == 0 && stack.getItem() instanceof ItemGolemBell) {
            ItemGolemBell.resetMarkers(stack, level, player);
            return;
        }

        // key == 1 : Wand 切换模式
        if (key == 1 && stack.getItem() instanceof WandCastingItem) {
            WandManager.toggleMisc(stack, level, player);
            return;
        }

//        // key == 1 : 元素铲切换朝向
//        if (key == 1 && stack.getItem() instanceof ElementalShovelItem shovel) {
//            byte b = ElementalShovelItem.getOrientation(stack);
//            ElementalShovelItem.setOrientation(stack, (byte) (b + 1));
//        }
//        anazor got something wrong
    }
}
