package thaumcraft.common.lib.utils;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class InventoryUtils {
   public static @NotNull("null -> empty") ItemStack placeItemStackIntoInventory(ItemStack stack, Container inventory, Direction side, boolean doit) {
      ItemStack itemstack = stack.copy();
      ItemStack itemstack1 = insertStack(inventory, itemstack, side, doit);
      if (itemstack1 != null && itemstack1.getCount() != 0) {
         return itemstack1.copy();
      } else {
         if (doit) {
            inventory.setChanged();
         }

         return ItemStack.EMPTY;
      }
   }

   //TODO:Verify
   public static ItemStack insertStack(Container container, ItemStack stack, @Nullable Direction side, boolean simulate) {
      if (stack.isEmpty()) return ItemStack.EMPTY;

      if (container instanceof WorldlyContainer worldly && side != null) {
         int[] slots = worldly.getSlotsForFace(side);
         {
            for (int slot : slots) {
               if (canMerge(container.getItem(slot), stack)) {
                  stack = tryInsert(container, stack, slot, side, simulate);
                  if (stack.isEmpty()) return ItemStack.EMPTY;
               }
            }
            for (int slot : slots) {
               stack = tryInsert(container, stack, slot, side, simulate);
               if (stack.isEmpty()) return ItemStack.EMPTY;
            }
         }
          return stack;
      }
      int size = container.getContainerSize();
      {
         for (int slot = 0; slot < size; slot++) {
            if (canMerge(container.getItem(slot), stack)) {
               stack = tryInsert(container, stack, slot, side, simulate);
               if (stack.isEmpty()) return ItemStack.EMPTY;
            }
         }
         for (int slot = 0; slot < size; slot++) {
            stack = tryInsert(container, stack, slot, side, simulate);
            if (stack.isEmpty()) return ItemStack.EMPTY;
         }
      }

      return stack;

   }
   @Deprecated(forRemoval = true)
   public static boolean inventoryContains(Container inventory, ItemStack stack, Direction side, boolean useOre, boolean ignoreDamage, boolean ignoreNBT) {
      return !extractStack(inventory, stack, side, useOre, ignoreDamage, ignoreNBT, false).isEmpty();
   }
   @Deprecated(forRemoval = true)
   public static ItemStack extractStack(Container inventory, ItemStack target, Direction side, boolean useOre, boolean ignoreDamage, boolean ignoreNBT, boolean doit) {
      ItemStack outStack = ItemStack.EMPTY;

      int size = inventory.getContainerSize();
      for (int i = 0; i < size; i++) {
         if (target.isEmpty() || !outStack.isEmpty()) break;
         // 这里你可以在 attemptExtraction 内根据 Direction 决定是否允许操作
         outStack = attemptExtraction(inventory, target, i, side, useOre, ignoreDamage, ignoreNBT, doit);
      }

      return outStack.isEmpty() ? ItemStack.EMPTY : outStack.copy();
   }

   @Deprecated(forRemoval = true)
   public static ItemStack attemptExtraction(Container container, ItemStack stack, int slot,
                                             @Nullable Direction side,
                                             boolean useOre, boolean ignoreDamage,
                                             boolean ignoreNBT, boolean doExtract) {
      ItemStack slotStack = container.getItem(slot);
      if (slotStack.isEmpty() || stack.isEmpty()) return ItemStack.EMPTY;

      // 检查能否从容器提取
      if (!canExtractItemFromInventory(container, slotStack, slot, side)) return ItemStack.EMPTY;

      // 比较物品是否相等（你可以根据需要加 Ore / ignoreDamage / ignoreNBT 逻辑）
      if (!areItemStacksEqual(slotStack, stack, ignoreDamage, ignoreNBT)) return ItemStack.EMPTY;

      ItemStack outStack = slotStack.copy();
      int requested = stack.getCount();

      if (requested >= slotStack.getCount()) {
         // 全部提取
         if (doExtract) container.setItem(slot, ItemStack.EMPTY);
         outStack.setCount(slotStack.getCount());
      } else {
         // 部分提取
         if (doExtract) {
            slotStack.shrink(requested);
            container.setItem(slot, slotStack);
         }
         outStack.setCount(requested);
      }

      if (doExtract) container.setChanged();
      return outStack;
   }

   private static boolean canExtractItemFromInventory(Container container, ItemStack stack, int slot,
                                                      @Nullable Direction side) {
      if (side != null && container instanceof WorldlyContainer worldly) {
         return worldly.canTakeItemThroughFace(slot, stack, side);
      }
      return true;
   }

//   public static boolean canInsertItemToInventory(Container inventory, ItemStack stack1, int par2, int par3) {
//      return stack1 != null && inventory.isItemValidForSlot(par2, stack1) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).canInsertItem(par2, stack1, par3));
//   }

//   public static boolean canExtractItemFromInventory(Container inventory, ItemStack stack1, int par2, int par3) {
//      return stack1 != null && (!(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).canExtractItem(par2, stack1, par3));
//   }

   public static boolean compareMultipleItems(ItemStack c1, ItemStack[] c2) {
      if (c1.isEmpty() || c1.getCount() <= 0) return false;

      for (ItemStack is : c2) {
         if (is.isEmpty()) continue;

         // Item + NBT 比较
         if (ItemStack.isSameItemSameTags(c1, is)) {
            return true;
         }
      }

      return false;
   }


   public static boolean consumeInventoryItem(Player player, Item item, int damage) {
      for (int i = 0; i < player.getInventory().items.size(); i++) {
         ItemStack stack = player.getInventory().items.get(i);
         if (!stack.isEmpty() && stack.getItem() == item && stack.getDamageValue() == damage) {
            stack.shrink(1);
            if (stack.isEmpty()) {
               player.getInventory().items.set(i, ItemStack.EMPTY);
            }
            return true;
         }
      }
      return false;
   }

   public static void dropItems(Level world, BlockPos pos) {
      Random rand = new Random();
       BlockEntity BlockEntity = LevelBlockEntityAccessing.getExistingBlockEntity(world, pos);

      if (BlockEntity instanceof RandomizableContainerBlockEntity container) {
         int size = container.getContainerSize();

         for (int i = 0; i < size; i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
               float rx = rand.nextFloat() * 0.8F + 0.1F;
               float ry = rand.nextFloat() * 0.8F + 0.1F;
               float rz = rand.nextFloat() * 0.8F + 0.1F;

               ItemStack dropStack = stack.copy();
               ItemEntity entityItem = new ItemEntity(
                       world,
                       pos.getX() + rx,
                       pos.getY() + ry,
                       pos.getZ() + rz,
                       dropStack
               );

               float factor = 0.05F;
               entityItem.setDeltaMovement(
                       rand.nextGaussian() * factor,
                       rand.nextGaussian() * factor + 0.2F,
                       rand.nextGaussian() * factor
               );

               world.addFreshEntity(entityItem);
               container.setItem(i, ItemStack.EMPTY);
            }
         }
      }
   }

   public static void dropItemsAtEntity(Level world, BlockPos pos, Entity entity) {
       BlockEntity BlockEntity = LevelBlockEntityAccessing.getExistingBlockEntity(world, pos);

      if (BlockEntity instanceof RandomizableContainerBlockEntity container) {
         int size = container.getContainerSize();

         for (int i = 0; i < size; i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {

               // 创建掉落物
               double ex = entity.getX();
               double ey = entity.getY() + entity.getEyeHeight() / 2.0;
               double ez = entity.getZ();
               ItemEntity entityItem = new ItemEntity(world, ex, ey, ez, stack.copy());

               // 掉落
               world.addFreshEntity(entityItem);

               // 清空槽位
               container.setItem(i, ItemStack.EMPTY);
            }
         }
      }
   }

   public static int isWandInHotbarWithRoom(Aspect aspect, int amount, Player player) {
      for (int i = 0; i < 9; i++) { // 热键槽前 9 个
         ItemStack stack = player.getInventory().items.get(i);
         if (!stack.isEmpty() && stack.getItem() instanceof WandCastingItem wand) {
            // 调用原有魔杖逻辑判断容量
            if (wand.addCentiVis(stack, aspect, amount, false) < amount) {
               return i;
            }
         }
      }
      return -1;
   }

   public static final int OFFHAND_INDEX = Integer.MAX_VALUE;
   public static int isWandInHotbarOrOffhandWithRoom(Aspect aspect, int amount, Player player) {
      // 先复用热键检查
      int hotbarIndex = isWandInHotbarWithRoom(aspect, amount, player);
      if (hotbarIndex != -1) return hotbarIndex;

      // 检查副手
      ItemStack offhand = player.getInventory().offhand.get(0);
      if (!offhand.isEmpty() && offhand.getItem() instanceof WandCastingItem wand) {
         if (wand.addCentiVis(offhand, aspect, amount, false) < amount) {
            return OFFHAND_INDEX; // 自定义副手返回值
         }
      }

      return -1;
   }

   public static int isPlayerCarrying(Player player, ItemStack stack) {
      // 遍历主背包
      for (int i = 0; i < player.getInventory().items.size(); i++) {
         ItemStack s = player.getInventory().items.get(i);
         if (!s.isEmpty() && ItemStack.isSameItemSameTags(stack,s)) {
            return i; // 背包槽位
         }
      }

      // 检查副手
      for (int i = 0; i < player.getInventory().offhand.size(); i++) {
         ItemStack s = player.getInventory().offhand.get(i);
         if (!s.isEmpty() && ItemStack.isSameItemSameTags(stack,s)) {
            return 100; // 自定义返回值表示副手
         }
      }

      return -1; // 未携带
   }

   @Deprecated(forRemoval = true,since = "we have mojang's ItemStack#hurt")
   public static ItemStack damageItem(int damageAmount, ItemStack stack, Level world) {
      if (!stack.isDamageableItem()) return stack;

      if (damageAmount > 0) {
         int unbreaking = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
         int negated = 0;

         for (int i = 0; i < damageAmount && unbreaking > 0; i++) {
            if (negateDamage(stack, unbreaking, world.getRandom())) {
               negated++;
            }
         }

         damageAmount -= negated;
         if (damageAmount <= 0) return stack;
      }

      stack.setDamageValue(stack.getDamageValue() + damageAmount);

      if (stack.getDamageValue() > stack.getMaxDamage()) {
         stack.shrink(1); // 堆叠数量减少
         if (stack.getCount() < 0) stack.setCount(0);
         stack.setDamageValue(0); // 重置耐久
      }

      return stack;
   }

   //u know what?fortune is not used and this method is not used.
   //fk you azanor
   public static void dropItemsWithChance(Level world, double x, double y, double z, float chance, int fortune, List<ItemStack> items) {
      if (world.isClientSide()) return; // 只在服务器执行
      if (!world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) return;

      for (ItemStack stack : items) {
         if (stack.isEmpty() || stack.getCount() <= 0) continue;
         if (world.getRandom().nextFloat() > chance) continue;

         float f = 0.7F;
         double rx = world.getRandom().nextFloat() * f + (1.0F - f) * 0.5;
         double ry = world.getRandom().nextFloat() * f + (1.0F - f) * 0.5;
         double rz = world.getRandom().nextFloat() * f + (1.0F - f) * 0.5;

         ItemEntity entity = new ItemEntity(world, x + rx, y + ry, z + rz, stack.copy());
         entity.setPickUpDelay(10);
         world.addFreshEntity(entity);
      }
   }

   public static ChestBlockEntity getDoubleChest(Level world, ChestBlockEntity chest) {
      BlockState state = chest.getBlockState();
      if (!(state.getBlock() instanceof ChestBlock)) return null;

      ChestType type = state.getValue(ChestBlock.TYPE);
      if (type == ChestType.SINGLE) return null;

      Direction facing = state.getValue(ChestBlock.FACING);
      BlockPos pos = chest.getBlockPos();

      BlockPos otherPos = switch (type) {
         case LEFT -> pos.relative(facing.getClockWise(), 1);       // 左半箱另一半在右边
         case RIGHT -> pos.relative(facing.getCounterClockWise(), 1); // 右半箱另一半在左边
         default -> pos;
      };

      if (LevelBlockEntityAccessing.getExistingBlockEntity(world, otherPos) instanceof ChestBlockEntity otherChest) {
         return otherChest;
      }
      return null;
   }
   public static boolean canMerge(ItemStack existing, ItemStack incoming) {
      return !existing.isEmpty()
              && ItemStack.isSameItemSameTags(existing, incoming)
              && existing.getCount() < existing.getMaxStackSize();
   }

   public static ItemStack tryInsert(Container container, ItemStack stack, int slot,
                                      @Nullable Direction side, boolean simulate) {

      if (stack.isEmpty()) return ItemStack.EMPTY;

      ItemStack existing = container.getItem(slot);

      // WorldlyContainer 面向检查
      if (container instanceof WorldlyContainer worldly) {
         if (side != null && !worldly.canPlaceItemThroughFace(slot, stack, side)) {
            return stack;
         }
      }

      // 1) 空槽
      if (existing.isEmpty()) {
         int move = Math.min(stack.getCount(), stack.getMaxStackSize());
         if (!simulate) {
            ItemStack newStack = stack.copyWithCount(move);
            container.setItem(slot, newStack);
            container.setChanged();
         }
         return stack.copyWithCount(stack.getCount() - move);
      }

      // 2) 同类型叠加
      if (ItemStack.isSameItemSameTags(existing, stack)) {
         int canAdd = existing.getMaxStackSize() - existing.getCount();
         int move = Math.min(canAdd, stack.getCount());
         if (move <= 0) return stack;

         if (!simulate) {
            existing.grow(move);
            container.setItem(slot, existing);
            container.setChanged();
         }
         return stack.copyWithCount(stack.getCount() - move);
      }

      return stack;
   }
   public static boolean canInsertItemToInventory(Container container, ItemStack stack, int slot, @Nullable Direction side) {
      if (side != null && container instanceof WorldlyContainer worldly) {
         return worldly.canPlaceItemThroughFace(slot, stack, side);
      }
      return true;
   }
   public static boolean negateDamage(ItemStack stack, int level, RandomSource rand) {
      if (stack.getItem() instanceof ArmorItem && rand.nextFloat() < 0.6F) {
         return false;
      }
      return rand.nextInt(level + 1) > 0;
   }




   public static List<ItemStack> stackOfAmount(ItemStack stack, int amount) {
      List<ItemStack> list = new ArrayList<>(Math.ceilDiv(stack.getMaxStackSize(), amount));
      while (amount > stack.getMaxStackSize()) {
         list.add(stack.copyWithCount(stack.getMaxStackSize()));
         amount -= stack.getMaxStackSize();
      }
      list.add(stack.copyWithCount(amount));
      return list;
   }
}
