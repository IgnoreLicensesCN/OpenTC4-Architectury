package thaumcraft.common.entities.ai.goals.golem.itemstackinteraction;

import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static thaumcraft.common.entities.ai.goals.golem.itemstackinteraction.GolemEmptyCorePlaceItemStackToHomeGoal.findSomethingEmptyCore;
import static thaumcraft.common.entities.golems.GolemHelper.*;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY;

public class GolemTakeItemStackFromHomeGoal extends AbstractGolemGoal {

    public GolemTakeItemStackFromHomeGoal(BaseGolemEntity golem) {
        super(golem);
    }
    protected GolemHelper.ContainersWithRoomIterator containersWithRoomIterator = new GolemHelper.ContainersWithRoomIterator(golem);
    public List<ItemStack> cache = new ArrayList<>();

    public boolean meetsFilter(ItemStack stack){
        return findSomethingEmptyCore(golem, stack, containersWithRoomIterator) && !isOnTimeOut(golem, stack);
    }
    @Override
    public boolean canUse() {
        if (!golem.getMainHandItem().isEmpty()){
            return false;
        }
        if (!(golem.tickCount % getGolemGoalDelay() == 0)){
            return false;
        }
        var homePos = golem.getRestrictCenter();
        var homeCenterX = homePos.getX() + 0.5;
        var homeCenterY = homePos.getY() + 0.5;
        var homeCenterZ = homePos.getZ() + 0.5;
        if (golem.distanceToSqr(homeCenterX,homeCenterY,homeCenterZ) > 5){
            return false;
        }
        var homeBE = getExistingBlockEntity(golem.level(),homePos);
        boolean fuzzy = fuzzyMatch();
        
        if (homeBE instanceof Container container){
            var dir = golem.homeDirection;
            cache.clear();
            for (var consideringStack:golem.inventory){
                if (consideringStack.isEmpty()){
                    continue;
                }
                if (ItemStack.isSameItemSameTags(consideringStack, consideringStack) && meetsFilter(consideringStack)) {
                    cache.add(consideringStack);
                } else if (fuzzy) {
                    var tags = getConsideringTagsForItem(consideringStack.getItem());
                    var tagsForConsidering = getConsideringTagsForItem(consideringStack.getItem());
                    if (!Collections.disjoint(tags, tagsForConsidering) && meetsFilter(consideringStack)){
                        cache.add(consideringStack);
                    }
                }
            }
            if (cache.isEmpty()){
                IntIterator iterator;
                if (container instanceof WorldlyContainer isidedinventory) {
                    int[] aint = isidedinventory.getSlotsForFace(dir);
                    iterator = new GolemHelper.IntArrayIterator(aint);
                } else {
                    iterator = new GolemHelper.IntRangeIterator(container.getContainerSize());
                }
                while (iterator.hasNext()) {
                    int i = iterator.nextInt();

                    var extracted = extractStackFromContainerWithTimeout(
                            golem,container,i,dir,false
                    );
                    if (!extracted.isEmpty()) {
                        if (findSomethingEmptyCore(golem,extracted,containersWithRoomIterator)){
                            extracted = extractStackFromContainerWithTimeout(
                                    golem,container,i,dir,true
                            );
                            this.golem.setItemInHand(InteractionHand.MAIN_HAND, extracted);
                            chestCooldown = 5;
                            golem.level().gameEvent(golem, GameEvent.CONTAINER_OPEN, homePos);
                            golem.level().gameEvent(golem, GameEvent.CONTAINER_CLOSE, homePos);
                            return true;
                        }
                    }
                }
                return false;
            }else {
                ItemStack result = ItemStack.EMPTY;
                for(ItemStack stack : cache) {
                    ItemStack needed = stack.copy();
                    needed.setCount(getGolemCarrySpace(golem,stack.getCount(),stack.getMaxStackSize()));
                    result = InventoryUtils.extractStack(
                                    container,
                                    needed,
                                    dir,
                                    golem.getItemStackMatcher(),
                                    true);
                    break;
                }
                cache.clear();
                if (!result.isEmpty()) {
                    this.golem.setItemInHand(InteractionHand.MAIN_HAND,result);
                    chestCooldown = 5;
                    golem.level().gameEvent(golem, GameEvent.CONTAINER_OPEN, homePos);
                    golem.level().gameEvent(golem, GameEvent.CONTAINER_CLOSE, homePos);
                    return true;
                }
                return false;
            }
        }

        return false;
    }

    protected boolean fuzzyMatch() {
        return golem.configurationProviders.contains(MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY)
                && MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY.provider.getBooleanFrom(golem);
    }

    protected int chestCooldown = 0;

    @Override
    public void tick() {
        super.tick();
        chestCooldown--;
    }

    @Override
    public boolean canContinueToUse() {
        return chestCooldown > 0;
    }
}
