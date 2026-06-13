package thaumcraft.api.scan;

import com.linearity.opentc4.annotations.ShouldNotModify;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspect.IScanDiscoverableAspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;
import thaumcraft.api.scan.block.BlockPosScanManager;
import thaumcraft.api.scan.entity.EntityScanManager;
import thaumcraft.api.scan.itemstack.ItemStackScanManager;
import thaumcraft.common.lib.NodeInfo;
import thaumcraft.common.lib.network.misc.PacketPlayerNotificationS2C;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketAspectDiscoveryS2C;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;

import static thaumcraft.api.aspects.Aspects.ALL_ASPECTS;
import static thaumcraft.api.scan.ThaumcraftScannedTypes.VIS_NODE;

//TODO:[maybe wont finished]listener for unlock aspect
public class ScanManager {

    public static void onPlayerScanEntity(ServerPlayer player, Entity entity){
        EntityScanManager.onPlayerScanEntity(player, entity);
    }
    public static void onPlayerScanBlockPos(ServerPlayer player, BlockPos pos){
        BlockPosScanManager.onPlayerScanBlockPos(player, pos);
    }
    public static void onPlayerScanItemStack(ServerPlayer player, ItemStack stack){
        ItemStackScanManager.onPlayerScanItemStack(player, stack);
    }

    public static class NodeScanManager{
        public static AspectList<Aspect> getNodeScanAspectsForNodeInfo(NodeInfo info){
            var nodeBase = info.nodeAspectsBase;
            AspectList<Aspect> result = new HashAspectList<>();
            nodeBase.forEach(
                    (aspect,amount) -> {
                        result.mergeWithHighest(aspect, Math.max(4, amount / 10));
                    }
            );
            info.nodeType.getScanAdditionalAspects().forEach(
                    result::mergeWithHighest
            );
            return result;
        }
        public static boolean onScanVisNode(ServerPlayer player,NodeInfo nodeInfo){
            var info = ResearchAndScannedInfo.getFromPlayer(player);
            var nodeID = nodeInfo.nodeId;
            if (info.hasScannedForType(VIS_NODE,nodeID)){
                return false;
            }
            var aspectToAdd = getNodeScanAspectsForNodeInfo(nodeInfo);
            if (CommonScanManager.onMeetAspectsToScan(player, aspectToAdd)) {
                info.addScannedForTypeAndSyncToPlayer(player,VIS_NODE,nodeID);
                return true;
            }
            return true;
        }
    }
    //collapse in IDEA works well
    public static class CommonScanManager{
        //true if successfully added aspect(scanned) false not
        public static boolean onMeetAspectsToScan(ServerPlayer player, AspectList<Aspect> aspects){
            if (aspects.isEmpty()){
                if (player instanceof ServerPlayer serverPlayer){
                    new PacketPlayerNotificationS2C(Component.translatable("tc.unknownobject")).sendTo(serverPlayer);
                }
                return false;
            }
            if (!scanAddAspects(player,aspects)){
                var needToDiscover = getOneOfAspectNeedToDiscover(player,aspects);
                var aspectNeeded = ALL_ASPECTS.get(needToDiscover);
                if (needToDiscover != null && aspectNeeded != null){
                    new PacketPlayerNotificationS2C(Component.translatable("tc.discoveryerror",
                            aspectNeeded.getLocalizedDescription()
                    ));
                }
                return false;
            }
            return true;
        }

        //true if succeed false if cant
        public static boolean scanAddAspects(
                ServerPlayer player,
                @ShouldNotModify AspectList<Aspect> aspectsToAdd){
            if (scanCanAddAspects(player, aspectsToAdd)){
                ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromPlayer(player);
                aspectsToAdd.forEach(((aspect, amount) -> {
                    if (player instanceof ServerPlayer serverPlayer){
                        if (playerCanDiscoverAspect(player, aspect)) {
                            new PacketAspectDiscoveryS2C(aspect).sendTo(serverPlayer);
                            info.addResearchAspectAndSyncToPlayer(aspect, 2,serverPlayer);
                        }
                    }
                    if (player instanceof ServerPlayer serverPlayer){
                        info.addResearchAspectAndSyncToPlayer(aspect, amount,serverPlayer);
                    }
                }));
                return true;
            }
            return false;
        }

        //true if can false if cant
        public static boolean scanCanAddAspects(
                Player player,
                @ShouldNotModify AspectList<Aspect> aspectsToAdd){
            for (var aspect:aspectsToAdd.keySet()){
                if (playerHasAspect(player, aspect)){
                    continue;
                }
                if(playerCanDiscoverAspect(player, aspect)){
                    continue;
                }
                return false;
            }
            return true;
        }

        public static @Nullable AspectResourceLocation getOneOfAspectNeedToDiscover(
                Player player,
                AspectList<Aspect> aspectsToDiscover
        ){
            for (var aspect:aspectsToDiscover.keySet()){
                if (aspect instanceof IScanDiscoverableAspect discoverableAspect){
                    var toDiscover = discoverableAspect.getOneOfAspectRequiredToDiscover(player);
                    if (toDiscover != null){
                        return toDiscover;
                    }
                }
            }
            return null;
        }

        public static boolean playerHasAspect(Player player, Aspect aspect){
            ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromPlayer(player);
            return info.hasResearchAspect(aspect);
        }

        //false if already unlocked
        public static boolean playerCanDiscoverAspect(Player player, Aspect aspect){
            ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromPlayer(player);
            if (info.hasResearchAspect(aspect)){
                return false;//already unlocked
            }
            if (aspect instanceof IScanDiscoverableAspect discoverableAspect){
                return discoverableAspect.canPlayerDiscover(player);
            }
            return false;
        }
    }

}
