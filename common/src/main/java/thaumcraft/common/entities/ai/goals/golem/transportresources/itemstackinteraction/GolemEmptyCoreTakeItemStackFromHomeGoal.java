package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;

import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemEmptyCorePlaceItemStackToHomeGoal.findSomethingEmptyCore;

public class GolemEmptyCoreTakeItemStackFromHomeGoal extends GolemTakeItemStackFromHomeGoal{
    public GolemEmptyCoreTakeItemStackFromHomeGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected GolemHelper.ContainersWithRoomIterator containersWithRoomIterator = new GolemHelper.ContainersWithRoomIterator(golem);
    @Override
    protected boolean findSomething(ItemStack extracted) {
        return findSomethingEmptyCore(golem, extracted, containersWithRoomIterator);
    }
}
