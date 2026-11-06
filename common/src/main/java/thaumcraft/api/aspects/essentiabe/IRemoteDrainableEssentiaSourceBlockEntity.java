package thaumcraft.api.aspects.essentiabe;


import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.mixinaccessors.CoordLookupOwnerLevel;
import com.linearity.opentc4.utils.collectionlike.coordlookups.CoordLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSourceS2C;
import thaumcraft.common.tiles.abstracts.ICoordLookupOwnerBlockEntity;

import java.util.Set;

/**
 * @author Azanor
 * <p>
 * This interface is implemented by tile entites (or possibly anything else) like jars
 * so that they can act as an essentia source for blocks like the infusion altar.
 *
 * <p>yes i renamed from IAspectSource --IgnoreLicensesCN </p>
 */
public interface IRemoteDrainableEssentiaSourceBlockEntity
        /*extends IAspectContainerBlockEntity<Asp>*/ extends ICoordLookupOwnerBlockEntity<IRemoteDrainableEssentiaSourceBlockEntity> {
//    Map<Level, CoordLookup<IRemoteDrainableEssentiaSourceBlockEntity>> levelledRemoteDrainables
//            = new MapMaker().weakKeys().makeMap();
    CoordLookupOwnerLevel.CoordLookupTypeKey<IRemoteDrainableEssentiaSourceBlockEntity> COORD_LOOKUP_KEY_FOR_REMOTE_DRAINABLE = new CoordLookupOwnerLevel.CoordLookupTypeKey<>();

    @Override
    default @NotNull CoordLookup<IRemoteDrainableEssentiaSourceBlockEntity> getSelfLookupInLevel(Level level){
        for (var lookup:((CoordLookupOwnerLevel)level).opentc4$getCoordLookupsForType(COORD_LOOKUP_KEY_FOR_REMOTE_DRAINABLE)){
            return lookup;
        }
        throw new IllegalStateException("Where is my lookup?this should not happen!");
    };

    @Override
    default @NotNull IRemoteDrainableEssentiaSourceBlockEntity getStoreItemForLookup(){return this;};

    //check with method above first.
    //@return false if drain failed
    int drainEssentiaRemote(Aspect aspect, int amount, @Modifiable Set<IRemoteEssentiaDrainerBlockEntity> metDrainers);
    @Nullable Level getLevel();
    @NotNull BlockPos getBlockPos();
    //removed cooldown
    default void playDrainEffect(IRemoteEssentiaDrainerBlockEntity aspectDrainer, Aspect drainedAspect) {
        var drainerPos = aspectDrainer.getBlockPos();
        var drainedPos = this.getBlockPos();
        PacketFXEssentiaSourceS2C fxPacket = new PacketFXEssentiaSourceS2C(
                drainerPos.getX(), drainerPos.getY(), drainerPos.getZ(),
                (byte)(drainerPos.getX() - drainedPos.getX()),
                (byte)(drainerPos.getY() - drainedPos.getY()),
                (byte)(drainerPos.getZ() - drainedPos.getZ()),
                drainedAspect.getColor()
        );
        if (aspectDrainer.getLevel() instanceof ServerLevel serverLevel){
            fxPacket.sendToAllAround(serverLevel,aspectDrainer.getBlockPos(),32*32);
        }
    }

}
