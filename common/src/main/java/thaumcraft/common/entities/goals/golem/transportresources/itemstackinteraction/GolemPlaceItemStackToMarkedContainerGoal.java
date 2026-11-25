package thaumcraft.common.entities.goals.golem.transportresources.itemstackinteraction;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.gameevent.GameEvent;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.lib.utils.InventoryUtils;


public class GolemPlaceItemStackToMarkedContainerGoal extends GolemDoSomethingToMarkedContainerGoal {
    protected int cooldown = 0;
    public GolemPlaceItemStackToMarkedContainerGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public void start() {
        super.start();
        if (usingMarker == null){
            return;
        }
        var usingBE = usingBlockEntity.get();
        if (usingBE instanceof Container container){
            golem.setItemInHand(
                    InteractionHand.MAIN_HAND,
                    InventoryUtils.placeItemStackIntoInventory(golem.getMainHandItem(), container, usingMarker.getSide(), true)
            );
            golem.level().gameEvent(golem, GameEvent.CONTAINER_OPEN, usingMarker.getPos());
            golem.playSound(SoundEvents.CHEST_OPEN, 1.0F, 1.0F);
        }
    }

    @Override
    public void stop() {
        super.stop();
        golem.playSound(SoundEvents.CHEST_CLOSE, 1.0F, 1.0F);
        if (usingMarker != null){
            golem.level().gameEvent(golem, GameEvent.CONTAINER_CLOSE, usingMarker.getPos());
        }
    }
}
