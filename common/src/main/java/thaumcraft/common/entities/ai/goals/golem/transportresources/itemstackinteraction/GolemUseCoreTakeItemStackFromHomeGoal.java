package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction;

import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemUseCorePlaceItemStackToHomeGoal.findSomethingUseCore;

public class GolemUseCoreTakeItemStackFromHomeGoal extends GolemTakeItemStackFromHomeGoal{
    public GolemUseCoreTakeItemStackFromHomeGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected ByteSet colorsCache = new ByteArraySet(17);
    @Override
    protected boolean findSomething(ItemStack extracted) {
        return findSomethingUseCore(golem, extracted, colorsCache);
    }
}
