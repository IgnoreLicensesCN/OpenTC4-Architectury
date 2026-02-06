package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IResearchNoteCreatable;
import thaumcraft.api.research.interfaces.IResearchable;
import thaumcraft.api.researchtable.IResearchTableAspectEditTool;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class PacketPlayerCreateResearchNoteC2S extends BaseC2SMessage {
    public static MessageType messageType;
    public static final String ID = Thaumcraft.MOD_ID + ":create_research_note";
    private ResearchItemResourceLocation researchToCreateNote;
    private int inventorySlotWithInkWell;
    @Override
    public MessageType getType() {
        return messageType;
    }
    public PacketPlayerCreateResearchNoteC2S() {}
    public PacketPlayerCreateResearchNoteC2S(ResearchItemResourceLocation researchToCreateNote, int inventorySlotWithInkWell) {
        this.researchToCreateNote = researchToCreateNote;
        this.inventorySlotWithInkWell = inventorySlotWithInkWell;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(researchToCreateNote);
        buf.writeInt(inventorySlotWithInkWell);
    }

    public static PacketPlayerCreateResearchNoteC2S decode(FriendlyByteBuf buf) {
        return new PacketPlayerCreateResearchNoteC2S(new ResearchItemResourceLocation(buf.readResourceLocation()),buf.readInt());
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (inventorySlotWithInkWell < 0){
            return;
        }
        var research = ResearchItem.getResearch(researchToCreateNote);
        var player = context.getPlayer();
        if (player instanceof ServerPlayer serverPlayer
                && research instanceof IResearchable researchable
                && research instanceof IResearchNoteCreatable researchNoteCreatable) {
            if (!researchNoteCreatable.canPlayerCreateResearchNote(player.getGameProfile().getName())){
                return;
            }
            if (researchable.canPlayerResearch(player.getGameProfile().getName())){
                var inv = player.getInventory();
                if (inv.getContainerSize() <= inventorySlotWithInkWell){
                    return;
                }
                var usingStack = inv.getItem(inventorySlotWithInkWell);
                if (!usingStack.isEmpty()){
                    if (usingStack.getItem() instanceof IResearchTableAspectEditTool editTool){
                        var level = context.getPlayer().level();
                        if (editTool.canCreateResearchNote(
                                        level,
                                        player,
                                        usingStack,
                                        research
                                )
                                .canCreate()){
                            editTool.createResearchNote(level,serverPlayer,usingStack,research);
                        }
                    }
                }
            }
        }
    }
}
