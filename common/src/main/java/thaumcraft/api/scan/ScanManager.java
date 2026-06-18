package thaumcraft.api.scan;

import com.linearity.opentc4.annotations.ShouldNotModify;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

    public static void onScanEntity(LivingEntity living, Entity entity){
        EntityScanManager.onScanEntity(living, entity);
    }
    public static void onScanBlockPos(LivingEntity living, BlockPos pos){
        BlockPosScanManager.onScanBlockPos(living, pos);
    }
    public static void onScanItemStack(LivingEntity living, ItemStack stack){
        ItemStackScanManager.onScanItemStack(living, stack);
    }

    public static class NodeScanManager{
        public static AspectList<Aspect> getNodeScanAspectsForNodeInfo(NodeInfo info){
            var nodeBase = info.nodeAspectsBase;
            AspectList<Aspect> result = new HashAspectList<>();
            nodeBase.forEach(
                    (aspect,amount) -> result.mergeWithHighest(aspect, Math.max(4, amount / 10))
            );
            info.nodeType.getScanAdditionalAspects().forEach(
                    result::mergeWithHighest
            );
            return result;
        }
        public static boolean onScanVisNode(LivingEntity living, NodeInfo nodeInfo){
            var info = ResearchAndScannedInfo.getFromLiving(living);
            if (info == null) return false;
            var nodeID = nodeInfo.nodeId;
            if (info.hasScannedForType(VIS_NODE,nodeID)){
                return false;
            }
            var aspectToAdd = getNodeScanAspectsForNodeInfo(nodeInfo);
            if (CommonScanManager.onMeetAspectsToScan(living, aspectToAdd)) {
                info.addScannedForTypeAndTrySyncToPlayer(living,VIS_NODE,nodeID);
                return true;
            }
            return true;
        }
    }
    //collapse in IDEA works well
    public static class CommonScanManager{
        //true if successfully added aspect(scanned) false not
        public static boolean onMeetAspectsToScan(LivingEntity living, AspectList<Aspect> aspects){
            if (aspects.isEmpty()){
                if (living instanceof ServerPlayer serverPlayer){
                    new PacketPlayerNotificationS2C(Component.translatable("tc.unknownobject")).sendTo(serverPlayer);
                }
                return false;
            }
            if (!scanAddAspects(living,aspects)){
                var needToDiscover = getOneOfAspectNeedToDiscover(living,aspects);
                var aspectNeeded = ALL_ASPECTS.get(needToDiscover);
                if (needToDiscover != null && aspectNeeded != null && living instanceof ServerPlayer serverPlayer){
                    new PacketPlayerNotificationS2C(Component.translatable("tc.discoveryerror",
                            aspectNeeded.getLocalizedDescription()
                    )).sendTo(serverPlayer);
                }
                return false;
            }
            return true;
        }

        //true if succeed false if cant
        public static boolean scanAddAspects(
                LivingEntity living,
                @ShouldNotModify AspectList<Aspect> aspectsToAdd){
            if (scanCanAddAspects(living, aspectsToAdd)){
                ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromLiving(living);
                if (info == null){
                    return false;
                }
                aspectsToAdd.forEach(((aspect, amount) -> {
                        if (livingCanDiscoverAspect(living, aspect)) {
                            info.addResearchAspectAndTrySyncToPlayer(aspect, 2,living);
                            if (living instanceof ServerPlayer serverPlayer) {
                                new PacketAspectDiscoveryS2C(aspect).sendTo(serverPlayer);
                            }
                        }
                    info.addResearchAspectAndTrySyncToPlayer(aspect, amount,living);
                }));
                return true;
            }
            return false;
        }

        //true if can false if cant
        public static boolean scanCanAddAspects(
                LivingEntity living,
                @ShouldNotModify AspectList<Aspect> aspectsToAdd){
            for (var aspect:aspectsToAdd.keySet()){
                if (livingHasAspect(living, aspect)){
                    continue;
                }
                if(livingCanDiscoverAspect(living, aspect)){
                    continue;
                }
                return false;
            }
            return true;
        }

        public static @Nullable AspectResourceLocation getOneOfAspectNeedToDiscover(
                LivingEntity living,
                AspectList<Aspect> aspectsToDiscover
        ){
            for (var aspect:aspectsToDiscover.keySet()){
                if (aspect instanceof IScanDiscoverableAspect discoverableAspect){
                    var toDiscover = discoverableAspect.getOneOfAspectRequiredToDiscover(living);
                    if (toDiscover != null){
                        return toDiscover;
                    }
                }
            }
            return null;
        }

        public static boolean livingHasAspect(LivingEntity living, Aspect aspect){
            ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromLiving(living);
            if (info == null){
                return false;
            }
            return info.hasResearchAspect(aspect);
        }

        //false if already unlocked
        public static boolean livingCanDiscoverAspect(LivingEntity living, Aspect aspect){
            ResearchAndScannedInfo info = ResearchAndScannedInfo.getFromLiving(living);
            if (info == null){
                return false;
            }
            if (info.hasResearchAspect(aspect)){
                return false;//already unlocked
            }
            if (aspect instanceof IScanDiscoverableAspect discoverableAspect){
                return discoverableAspect.canLivingDiscover(living);
            }
            return false;
        }
    }

}
