package thaumcraft.common.lib.network.playerdata;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import tc4tweak.PacketCheck;
import thaumcraft.common.Thaumcraft;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.research.ResearchManager;
import com.linearity.opentc4.utils.StatCollector;

import java.util.List;
import java.util.Objects;

import static tc4tweak.PacketCheck.isSecondaryResearch;

public class PacketPlayerCompleteToServerC2S extends BaseC2SMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":player_complete";
    public static MessageType messageType;

    private String key;
    private ResourceKey<Level> dim;
    private String username;
    private byte type;

    // ------------------ 构造 ------------------

    public PacketPlayerCompleteToServerC2S() {}

    public PacketPlayerCompleteToServerC2S(String key, String username, ResourceKey<Level> dim, byte type) {
        this.key = key;
        this.username = username;
        this.dim = dim;
        this.type = type;
    }

    // ------------------ 编码/解码 ------------------

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(key);
        buf.writeResourceKey(dim);
        buf.writeUtf(username);
        buf.writeByte(type);
    }

    public static PacketPlayerCompleteToServerC2S decode(FriendlyByteBuf buf) {
        String key = buf.readUtf();
        ResourceKey<Level> dim = buf.readResourceKey(Registries.DIMENSION);
        String username = buf.readUtf();
        byte type = buf.readByte();
        return new PacketPlayerCompleteToServerC2S(key, username, dim, type);
    }

    // ------------------ 处理 ------------------

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        if (!sanityPlayerComplete(this, serverPlayer)) return;

        Level world = serverPlayer.getServer().getLevel(dim);
        if (world == null) return;


        Player target = null;
        for (Player p : world.players()) {
            if (Objects.equals(p.getName().getString(), username)) {
                target = p;
                break;
            }
        }
        if (target == null) return;

        ResearchItem research = research();
        if (research == null || ResearchManager.isResearchComplete(username, key)) return;

        if (!ResearchManager.doesPlayerHaveRequisites(username, key)) {
            target.displayClientMessage(Component.translatable("tc.researcherror"), true);
            return;
        }

        if (type != 0) {
            if (type == 1) {
                ResearchManager.createResearchNoteForPlayer(world, target, key);
            }
        } else {
            // 移除原有 aspects
            for (Aspect a : research.tags.getAspectTypes()) {
                Thaumcraft.playerKnowledge.addAspectPool(username, a, (short) (-research.tags.getAmount(a)));
                ResearchManager.scheduleSave(target.getName().getString());
                new PacketAspectPoolS2C(
                        a.getTag(),
                        (short) (-research.tags.getAmount(a)),
                        Thaumcraft.playerKnowledge.getAspectPoolFor(username, a))
                        .sendTo(serverPlayer);
            }

            // 完成研究
            new PacketResearchCompleteS2C(key).sendTo(serverPlayer);
            Thaumcraft.researchManager.completeResearch(target, key);

            // 完成兄弟研究
//         List<String> siblings = research.siblings;
            if (research.siblings != null) {
                for (String sibling : research.siblings) {
                    if (!ResearchManager.isResearchComplete(username, sibling) &&
                            ResearchManager.doesPlayerHaveRequisites(username, sibling)) {
                       new PacketResearchCompleteS2C(sibling).sendTo(serverPlayer);
                        Thaumcraft.researchManager.completeResearch(target, sibling);
                    }
                }
            }
        }

        //TODO:Sound
        world.playSound(null, target.blockPosition(), Thaumcraft.SOUNDS.LEARN, target.getSoundSource(), 0.75F, 1.0F);
    }

    // ------------------ 逻辑 ------------------

    public ResearchItem research() {
        return ResearchCategories.getResearch(key);
    }

    public byte type() {
        return type;
    }

    public static boolean sanityPlayerComplete(PacketPlayerCompleteToServerC2S packet, ServerPlayer playerEntity) {
        if (packet.type() != 0) return true;
        ResearchItem research = packet.research();
        if (research == null) return false;
        boolean secondary = isSecondaryResearch(research);
        if (secondary) {
            if (PacketCheck.hasAspect(playerEntity, research))
                return true;
        }
        OpenTC4.LOGGER.info(
                "Player {} sent suspicious packet to complete research {}@{}",
                playerEntity.getGameProfile(),
                research.key, research.category);
        return false;
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
