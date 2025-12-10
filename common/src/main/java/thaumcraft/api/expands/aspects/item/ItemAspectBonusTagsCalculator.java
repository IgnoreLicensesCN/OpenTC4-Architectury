package thaumcraft.api.expands.aspects.item;

import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;
import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.expands.UnmodifiableAspectList;
import thaumcraft.api.expands.aspects.item.listeners.BonusTagForItemListener;

import static thaumcraft.api.expands.aspects.item.consts.BonusTagForItemListeners.*;

public class ItemAspectBonusTagsCalculator {

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
    public static AspectList getBonusTags(ItemStack itemstack, AspectList sourceTags) {
        AspectList aspects = new AspectList();
        Item item = itemstack.getItem();
        UnmodifiableAspectList sourceTagsView = new UnmodifiableAspectList(sourceTags);

        if (sourceTags != null) {
            for (Aspect tag : sourceTags.getAspectTypes()) {
                if (tag != null) {
                    aspects.add(tag, sourceTags.getAmount(tag));
                }
            }
        }

        for (BonusTagForItemListener listener : BonusTagForItemListenerManager.getListeners()) {
            listener.onItem(item,itemstack,sourceTagsView,aspects);
        }

        return ThaumcraftApiHelper.cullTags(aspects);
    }
}
