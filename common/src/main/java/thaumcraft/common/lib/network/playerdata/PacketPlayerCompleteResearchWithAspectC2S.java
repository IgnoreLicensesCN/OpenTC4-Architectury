package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IAspectUnlockable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class PacketPlayerCompleteResearchWithAspectC2S extends BaseC2SMessage {
    public static MessageType messageType;
    public static final String ID = Thaumcraft.MOD_ID + ":complete_research_with_aspect";
    private ResearchItemResourceLocation researchToCompleteWithAspect;
    @Override
    public MessageType getType() {
        return messageType;
    }
    public PacketPlayerCompleteResearchWithAspectC2S(){}
    public PacketPlayerCompleteResearchWithAspectC2S(ResearchItemResourceLocation researchToCompleteWithAspect) {
        this.researchToCompleteWithAspect = researchToCompleteWithAspect;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(researchToCompleteWithAspect);
    }

    public static PacketPlayerCompleteResearchWithAspectC2S decode(FriendlyByteBuf buf) {
        return new PacketPlayerCompleteResearchWithAspectC2S(new ResearchItemResourceLocation(buf.readResourceLocation()));
    }



    @Override
    public void handle(NetworkManager.PacketContext context) {
        var p = context.getPlayer();
        if (!(p instanceof ServerPlayer player)){return;}
        var playerName = player.getGameProfile().getName();
        var research = ResearchItem.getResearch(researchToCompleteWithAspect);
        if (research == null){return;}
        if (!(research instanceof IAspectUnlockable aspectUnlockable)){return;}
        if (!aspectUnlockable.canPlayerCompleteResearchWithAspect(playerName)){return;}
    }
}
