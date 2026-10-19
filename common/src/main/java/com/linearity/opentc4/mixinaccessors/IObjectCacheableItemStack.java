package com.linearity.opentc4.mixinaccessors;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * now look at this code
 * <pre>{@code
 * public class WandCastingItem extends Item
 * //ignores impls which should be here plz
 * {
 * //...
 *      @Override
 *     public List<ItemStack> getWandComponents(ItemStack componentOwnerStack) {
 *         int initCapacity = 2;
 *         if (this.canApplyFocus()){
 *             initCapacity += 1;
 *         }
 *         List<ItemStack> items = new ArrayList<>(initCapacity);
 *         var cap = getWandCapAsItemStack(componentOwnerStack);
 *         if (!cap.isEmpty()) {
 *             items.add(cap);
 *         }
 *         var rod = getWandRodAsItemStack(componentOwnerStack);
 *         if (!rod.isEmpty()) {
 *             items.add(rod);
 *         }
 *         if (this.canApplyFocus()){
 *             var focus = getFocusItemStack(componentOwnerStack);
 *             if (!focus.isEmpty()) {
 *                 items.add(focus);
 *             }
 *         }
 *         return Collections.unmodifiableList(items);
 *     }
 *     //...
 *  }
 *  }</pre>
 *  <p>we have "new ArrayList" here,which is not so fine.</p>
 *  <p>if we have some way like
 *  object pool(but when to recycle object?)
 *  or threadLocal(but when thread down we need to tell threadLocal pool to free object)
 *  we may reduce "new"</p>
 *  <p>luckily it's totally unmodifiable,so we can just put</p>
 *  <pre>{@code
 *  @Override
 * @ReturnValueUnstable
 *     public List<ItemStack> getWandComponents(@NotNull ItemStack componentOwnerStack) {
 *
 *         var cacheableItemStack = (IObjectCacheableItemStack)(Object)componentOwnerStack;
 *         var cached = cacheableItemStack.opentc4$getCachedObjectFromItemStack(WandCastingItemCachedObjectsForItemStack.class);
 *         if (cached == null){
 *             int initCapacity = 2;
 *             if (this.canApplyFocus()){
 *                 initCapacity += 1;
 *             }
 *             cached = new WandCastingItemCachedObjectsForItemStack(new ArrayList<>(initCapacity));
 *         }
 *
 *         var items = cached.components;
 *         items.clear();
 *         var cap = getWandCapAsItemStack(componentOwnerStack);
 *         if (!cap.isEmpty()) {
 *             items.add(cap);
 *         }
 *         var rod = getWandRodAsItemStack(componentOwnerStack);
 *         if (!rod.isEmpty()) {
 *             items.add(rod);
 *         }
 *         if (this.canApplyFocus()){
 *             var focus = getFocusItemStack(componentOwnerStack);
 *             if (!focus.isEmpty()) {
 *                 items.add(focus);
 *             }
 *         }
 *         return cached.componentsView;
 *     }
 *  }</pre>
 *  with zero "new"
 */
//TODO:[maybe wont finished]cut "new" with this
public interface IObjectCacheableItemStack {
    <T> @Nullable T opentc4$getCachedObjectFromItemStack(Class<T> clazz);
    <T> void opentc4$putCachedObjectIntoItemStack(Class<T> clazz,T object);
    default <T> T opentc4$getOrCreateCachedObjectFromItemStack(Class<T> clazz, Supplier<T> supplier){
        var cached = opentc4$getCachedObjectFromItemStack(clazz);
        if (cached == null) {
            cached = supplier.get();
            opentc4$putCachedObjectIntoItemStack(clazz, cached);
        }
        return cached;
    }
    static <T> T opentc4$getOrCreateCachedObjectFromItemStack(ItemStack stack, Class<T> clazz, Supplier<T> supplier) {
        return ((IObjectCacheableItemStack)(Object)stack).opentc4$getOrCreateCachedObjectFromItemStack(clazz, supplier);
    }
}
