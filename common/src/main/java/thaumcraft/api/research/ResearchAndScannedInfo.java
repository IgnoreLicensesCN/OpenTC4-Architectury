package thaumcraft.api.research;

import com.linearity.opentc4.mixinaccessors.PlayerResearchAndScannedInfoAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;
import thaumcraft.common.lib.network.playerdata.syncdata.scan.PacketSyncAllScannedS2C;
import thaumcraft.common.lib.network.playerdata.syncdata.PacketSyncResearchAspectsS2C;
import thaumcraft.common.lib.network.playerdata.syncdata.PacketSyncResearchCompletedS2C;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketUpdateAspectS2C;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//i have to say this one is misunderstanding.
//It just like "Oh i need some string to record something"
// not all research need this,I can just say "you've picked PrimePearl >= 1(MC statics or whatever)"(or i may lookup items scanned for resource location) so research is unlocked
// (or some advancement?like twilight forest)
public class ResearchAndScannedInfo {
    public boolean playerScanning = false;
    @ApiStatus.Internal//idk if use public do you have other ideas than methods below?
    public final Collection<ResearchItemResourceLocation> completedResearches = ConcurrentHashMap.newKeySet();
    @ApiStatus.Internal
    public final Collection<ClueResourceLocation> completedClues = ConcurrentHashMap.newKeySet();
    @ApiStatus.Internal
    public /*client need ordered one to display so not final*/ @NotNull AspectList<Aspect> owningResearchAspect = new HashAspectList<>();
    {
        owningResearchAspect.addAll(INITIAL_ASPECTS);
    }
    public static final AspectList<Aspect> INITIAL_ASPECTS = new HashAspectList<>();
    static {
        INITIAL_ASPECTS.addAll(UnmodifiableAspectList.of(
                Aspects.EARTH,10,
                Aspects.WATER,10,
                Aspects.FIRE,10,
                Aspects.AIR,10,
                Aspects.ORDER,10,
                Aspects.ENTROPY,10
        ));
    }
    @ApiStatus.Internal
    public final Map<ScannedTypeResourceLocation, Set<ResourceLocation>> scannedThings = new ConcurrentHashMap<>();

    public boolean hasResearchID(ResearchItemResourceLocation researchID){
        return completedResearches.contains(researchID);
    }

    public void addResearchID(ResearchItemResourceLocation researchID){
        completedResearches.add(researchID);
    }
    public boolean hasClue(ClueResourceLocation clueID){
        return completedClues.contains(clueID);
    }
    public void addClue(ClueResourceLocation clueID){
        completedClues.add(clueID);
    }
    public boolean hasResearchAspect(Aspect aspect){
        return owningResearchAspect.containsKey(aspect);
    }
    public int getResearchAspect(Aspect aspect){
        return owningResearchAspect.get(aspect);
    }
    public void addResearchAspect(Aspect aspect,int amount){
        owningResearchAspect.addAll(aspect,amount);
    }
    public void addResearchAspectAndSyncToPlayer(Aspect aspect,int amount, ServerPlayer serverPlayer){
        this.addResearchAspect(aspect, amount);
        new PacketUpdateAspectS2C(aspect, amount, this.getResearchAspect(aspect)).sendTo(serverPlayer);
    }
    public void setResearchAspect(Aspect aspect,int amount){
        owningResearchAspect.set(aspect,amount);
    }
    public void addScannedForType(ScannedTypeResourceLocation scannedType,ResourceLocation thingsScanned){
        scannedThings.computeIfAbsent(scannedType,k -> ConcurrentHashMap.newKeySet()).add(thingsScanned);
    }
    public boolean hasScannedForType(ScannedTypeResourceLocation scannedType,ResourceLocation thingsToKnowIfScanned){
        var scannedSet = scannedThings.get(scannedType);
        if (scannedSet == null){
            return false;
        }
        return scannedSet.contains(thingsToKnowIfScanned);
    }


    public static ResearchAndScannedInfo getFromPlayer(Player player){
        return ((PlayerResearchAndScannedInfoAccessor)player).opentc4$getResearchAndScannedInfo();
    }
    public static void setForPlayer(Player player, ResearchAndScannedInfo researchAndScannedInfo){
        ((PlayerResearchAndScannedInfoAccessor)player).opentc4$setResearchAndScannedInfo(researchAndScannedInfo);
    }
    public void syncAllSendPacket(ServerPlayer player){
        syncResearchSendPacket(player);
        syncClueSendPacket(player);
        syncOwningResearchSendPacket(player);
        syncScannedSendPacket(player);
    }
    public void syncResearchSendPacket(ServerPlayer player){
        new PacketSyncResearchCompletedS2C(this).sendTo(player);
    }
    public void syncClueSendPacket(ServerPlayer player){
        new PacketSyncResearchCompletedS2C(this).sendTo(player);
    }
    public void syncOwningResearchSendPacket(ServerPlayer player){
        new PacketSyncResearchAspectsS2C(owningResearchAspect).sendTo(player);
    }
    public void syncScannedSendPacket(ServerPlayer player){
        new PacketSyncAllScannedS2C(scannedThings).sendTo(player);
    }
    public void syncResearchClientSide(Collection<ResearchItemResourceLocation> researchIDs){
        this.completedResearches.clear();
        this.completedResearches.addAll(researchIDs);
    }
    public void syncClueClientSide(Collection<ClueResourceLocation> clueIDs){
        this.completedClues.clear();
        this.completedClues.addAll(clueIDs);
    }
    public void syncResearchAspectClientSide(AspectList<Aspect> aspects){
        this.owningResearchAspect = aspects;
    }
    public void syncScannedClientSide(Map<ScannedTypeResourceLocation, Set<ResourceLocation>> things){
        this.scannedThings.clear();
        this.scannedThings.putAll(things);
    }
    public void syncScannedClientSide(ScannedTypeResourceLocation type,Set<ResourceLocation> things){
        this.scannedThings.remove(type);
        this.scannedThings.put(type,things);
    }

}
