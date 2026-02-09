package thaumcraft.api.listeners.aspects.item.bonus;

import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;
import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.aspects.*;
import thaumcraft.api.listeners.aspects.item.bonus.listeners.BonusTagForItemListener;

import static thaumcraft.api.listeners.aspects.item.bonus.consts.BonusTagForItemListeners.*;

public class ItemBonusAspectCalculator {

    public static final ListenerManager<@NotNull BonusTagForItemListener> BonusTagForItemListenerManager = new ListenerManager<>();


    public static void init(){
        BonusTagForItemListenerManager.registerListener(DEFAULT_ON_ESSENTIA_CONTAINER);
        BonusTagForItemListenerManager.registerListener(DEFAULT_ON_ARMOR);
        BonusTagForItemListenerManager.registerListener(DEFAULT_ON_SWORD);
        BonusTagForItemListenerManager.registerListener(DEFAULT_ON_BOW);
        BonusTagForItemListenerManager.registerListener(DEFAULT_ON_PICKAXE);
        BonusTagForItemListenerManager.registerListener(DEFAULT_ON_TOOL);
        BonusTagForItemListenerManager.registerListener(DEFAULT_ON_SHEARS);
        BonusTagForItemListenerManager.registerListener(DEFAULT_ON_HOE);
        BonusTagForItemListenerManager.registerListener(DEFAULT_ENCHANTMENTS);
    }

    /**
     * onItem
     * -> onEnchantment
     * -> onItemStack
     * @param itemstack stack to get (aspect) bonus tags
     * @param sourceTags will be added to result(it won't be modified)
     * @return tags added with bonus
     */
    public static AspectList<Aspect> getBonusAspects(ItemStack itemstack, AspectList<Aspect> sourceTags) {
        AspectList<Aspect> aspects = new AspectList<>();
        Item item = itemstack.getItem();
        UnmodifiableAspectList<Aspect> sourceTagsView = new UnmodifiableAspectList<>(sourceTags);

        if (sourceTags != null) {
            for (Aspect tag : sourceTags.getAspectTypes()) {
                if (tag != null) {
                    aspects.addAll(tag, sourceTags.getAmount(tag));
                }
            }
        }

        for (BonusTagForItemListener listener : BonusTagForItemListenerManager.getListeners()) {
            listener.onItem(item,itemstack,sourceTagsView,aspects);
        }

        return cullTags(aspects);
    }

    //TODO:new api(maybe)
    public static AspectList<Aspect> cullTags(AspectList<Aspect> temp) {
        AspectList<Aspect> temp2 = new AspectList<>();
        for (Aspect tag : temp.getAspectTypes()) {
            if (tag != null)
                temp2.addAll(tag, temp.getAmount(tag));
        }
        while (temp2.size() > 6) {
            Aspect lowest = null;
            float low = Short.MAX_VALUE;
            for (var tag : temp2.getAspectTypes()) {
                if (tag == null) continue;
                float ta = temp2.getAmount(tag);
                if (tag instanceof PrimalAspect) {
                    ta *= .9f;
                } else if (tag instanceof CompoundAspect compoundAspect) {
                    var componentA = compoundAspect.components.aspectA();
                    var componentB = compoundAspect.components.aspectB();
                    if (componentA instanceof CompoundAspect compoundA) {
                        ta *= 1.1f;
                        if (!(compoundA.components.aspectA() instanceof PrimalAspect)) {
                            ta *= 1.05f;
                        }
                        if (!(compoundA.components.aspectB() instanceof PrimalAspect)) {
                            ta *= 1.05f;
                        }
                    }
                    if (componentB instanceof CompoundAspect compoundB) {
                        ta *= 1.1f;
                        if (!(compoundB.components.aspectA() instanceof PrimalAspect)) {
                            ta *= 1.05f;
                        }
                        if (!(compoundB.components.aspectB() instanceof PrimalAspect)) {
                            ta *= 1.05f;
                        }
                    }
                }

                if (ta < low) {
                    low = ta;
                    lowest = tag;
                }
            }
            temp2.remove(lowest);
        }
        return temp2;
    }
}
