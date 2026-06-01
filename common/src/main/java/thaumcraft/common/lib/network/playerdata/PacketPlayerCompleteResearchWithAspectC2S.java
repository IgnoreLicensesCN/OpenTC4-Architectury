package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IAspectUnlockableResearch;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class PacketPlayerCompleteResearchWithAspectC2S extends BaseC2SMessage {
    public static MessageType messageType;
    public static final String ID = Thaumcraft.MOD_ID + ":complete_research_with_aspect";
    private final ResearchItemResourceLocation researchToCompleteWithAspect;
    @Override
    public MessageType getType() {
        return messageType;
    }
    public PacketPlayerCompleteResearchWithAspectC2S(ResearchItemResourceLocation researchToCompleteWithAspect) {
        this.researchToCompleteWithAspect = researchToCompleteWithAspect;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(researchToCompleteWithAspect);
    }

    public static PacketPlayerCompleteResearchWithAspectC2S decode(FriendlyByteBuf buf) {
        return new PacketPlayerCompleteResearchWithAspectC2S(ResearchItemResourceLocation.of(buf.readResourceLocation()));
    }



    @Override
    public void handle(NetworkManager.PacketContext context) {
        var p = context.getPlayer();
        if (!(p instanceof ServerPlayer player)){return;}
        var research = ResearchItem.getResearch(researchToCompleteWithAspect);
        if (research == null){return;}
        if (research.isPlayerCompletedResearch(player)){
            return;
        }
        if (!(research instanceof IAspectUnlockableResearch aspectUnlockable)){return;}
        if (!aspectUnlockable.canPlayerCompleteResearchWithAspect(player)){return;}
        var aspectsCost = aspectUnlockable.getAspectCost();
        var playerOwningAspect = Thaumcraft.playerKnowledge.getAspectsDiscovered(player);
        if (aspectsCost.forEachWithBreak(
                (aspectTypeRequired,aspectsAmountCost) -> playerOwningAspect.getOrDefault(aspectTypeRequired, 0) < aspectsAmountCost
        )){
            return;
        }
        aspectsCost.forEach((aspectTypeRequired,aspectsAmountCost) -> {
            Thaumcraft.playerKnowledge.addAspectPool(player, aspectTypeRequired, (short) (-aspectsAmountCost));
            ResearchManager.scheduleSave(player);
            new PacketAspectPoolS2C(
                    aspectTypeRequired.aspectKey,
                    (short) (-aspectsAmountCost),
                    Thaumcraft.playerKnowledge.getAspectPoolFor(player, aspectTypeRequired))
                    .sendTo(player);
        });
        new PacketResearchCompleteS2C(research.key).sendTo(player);
        Thaumcraft.researchManager.completeResearch(player, research.key);
    }
}
