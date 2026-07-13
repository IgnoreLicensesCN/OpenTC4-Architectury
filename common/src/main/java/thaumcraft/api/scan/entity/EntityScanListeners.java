package thaumcraft.api.scan.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.listeners.aspects.entity.basic.EntityBasicAspectGetters;
import thaumcraft.api.scan.ScanManager;
import thaumcraft.api.research.ResearchAndScannedInfo;

import static thaumcraft.api.listeners.aspects.entity.basic.EntityBasicAspectGetters.getSafeStringForResourceLocation;
import static thaumcraft.api.scan.ThaumcraftScannedTypes.ENTITY;
import static thaumcraft.api.scan.ThaumcraftScannedTypes.PLAYER;


public enum EntityScanListeners {
    NORMAL_ENTITY_SCAN(
            new EntityScanListener(0) {
                @Override
                public void onEntityScan(EntityScanContext context) {
                    if (scanEntityCommon(context.livingScanning,context.entity)){
                        context.shouldBreak = true;
                    }
                }
            }
    ),
    ITEM_ENTITY_SCAN(
            new EntityScanListener(100) {
                @Override
                public void onEntityScan(EntityScanContext context) {
                    if (context.entity instanceof ItemEntity itemEntity) {
                        var stack = itemEntity.getItem();
                        if (!stack.isEmpty()){
                            ScanManager.onScanItemStack(context.livingScanning,stack);
                        }
                    }
                }
            }
    ),
    PLAYER_SCAN(
            new EntityScanListener(1000) {
                @Override
                public void onEntityScan(EntityScanContext context) {
                    if (context.entity instanceof Player playerBeingScanned){
                        if (scanPlayer(context.livingScanning,playerBeingScanned)){
                            context.shouldBreak = true;
                        }
                    }
                }
            }
    )
    ;
    public final EntityScanListener listener;
    EntityScanListeners(EntityScanListener listener){
        this.listener = listener;
    }

    //true if completed a scan
    public static boolean scanEntityCommon(LivingEntity living, Entity entity){
        var info = ResearchAndScannedInfo.getFromLiving(living);
        if (info == null) {
            return false;
        }
        var entityType = entity.getType();
        var entityID = entityType.arch$registryName();
        if (entityID != null){
            if (info.hasScannedForType(ENTITY,entityID)){
                return false;
            }
            var entityBasicAspects = EntityBasicAspectGetters.getBasicAspectsForEntityType(entityType);
            if (ScanManager.CommonScanManager.onMeetAspectsToScan(living,entityBasicAspects)){
                info.addScannedForTypeAndTrySyncToPlayer(living,ENTITY,entityID);
                return true;
            }
        }
        return false;
    }
    public static boolean scanPlayer(LivingEntity living, Player playerBeingScanned){
        var info = ResearchAndScannedInfo.getFromLiving(living);
        if (info == null) {
            return false;
        }
        var playerName = getSafeStringForResourceLocation(playerBeingScanned.getGameProfile().getName());
        var resLoc = new ResourceLocation("pn",playerName);
        if (info.hasScannedForType(PLAYER,resLoc)){
            return false;
        }
        var playerBeingScannedAspects = EntityBasicAspectGetters.getAspectsForPlayer(playerBeingScanned);
        if (ScanManager.CommonScanManager.onMeetAspectsToScan(living,playerBeingScannedAspects)){
            info.addScannedForTypeAndTrySyncToPlayer(living,PLAYER,resLoc);
            return true;
        }
        return false;
    }
}
