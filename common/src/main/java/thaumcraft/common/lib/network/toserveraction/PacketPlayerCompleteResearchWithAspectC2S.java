package thaumcraft.common.lib.network.toserveraction;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IAspectUnlockableResearch;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketUpdateAspectS2C;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketResearchCompleteS2C;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.researches.ResearchAndScannedInfo;

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
        var info = ResearchAndScannedInfo.getFromPlayer(player);
        if (aspectsCost.forEachWithBreak(
                (aspectTypeRequired,aspectsAmountCost) -> info.getResearchAspect(aspectTypeRequired) < aspectsAmountCost
        )){
            return;
        }
        aspectsCost.forEach((aspectTypeRequired,aspectsAmountCost) -> {
            info.addResearchAspect(aspectTypeRequired, (short) (-aspectsAmountCost));
            new PacketUpdateAspectS2C(
                    aspectTypeRequired.aspectKey,
                    (short) (-aspectsAmountCost),
                    info.getResearchAspect(aspectTypeRequired))
                    .sendTo(player);
        });
        new PacketResearchCompleteS2C(research.key).sendTo(player);
        research.completeResearch(player);
    }
}
