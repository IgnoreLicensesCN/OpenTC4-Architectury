package tc4tweak;

import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.tiles.TileResearchTable;

public class PacketCheck {
    public static final Marker securityMarker = MarkerManager.getMarker("SuspiciousPackets");

    public static boolean hasAspect(ServerPlayer playerEntity, ResearchItem research) {
        return research.tags.getAspects().entrySet().stream().noneMatch(e ->
                e.getValue() != null
                        && !hasAspect(playerEntity, e.getKey(), e.getValue()));
    }

    public static boolean hasAspect(TileResearchTable table, ServerPlayer player, Aspect aspect) {
        return hasAspect(player, aspect, 0) || table.bonusAspects.getAmount(aspect) > 0;
    }

    public static boolean hasAspect(ServerPlayer player, Aspect aspect, int threshold) {
        return Thaumcraft.playerKnowledge.getAspectPoolFor(player.getName().getString(), aspect) >= threshold;
    }

    public static boolean isSecondaryResearch(ResearchItem research) {
        return research.tags != null && research.tags.size() > 0
                && (Config.researchDifficulty == -1 || Config.researchDifficulty == 0 && research.isSecondary());
    }
}
