package thaumcraft.common.lib.research;

import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.linearity.opentc4.utils.StatCollector;
import tc4tweak.modules.generateItemHash.GenerateItemHash;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.research.IScanEventHandler;
import thaumcraft.api.research.ScanResult;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.network.playerdata.PacketAspectDiscoveryS2C;
import thaumcraft.common.lib.network.playerdata.PacketAspectPoolS2C;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.tiles.TileNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ScanManager implements IScanEventHandler {
   public ScanResult scanPhenomena(ItemStack stack, Level world, Player player) {
      return null;
   }

   public static int generateEntityHash(Entity entity) {
      // 基础类型 hash
      String rl = entity.getType().toString(); // 或 entity.getType().arch$registryName()
      String hash = /*rl == null ? "generic" :*/ rl;

      // 玩家用名字区分
      if (entity instanceof Player player) {
         hash = "player_" + player.getName().getString();
      }

      // Thaumcraft 自定义 NBT 匹配
      CompoundTag tc = new CompoundTag();
      entity.saveAdditional(tc);

      outer:
      for (ThaumcraftApi.EntityTags et : ThaumcraftApi.scanEntities) {
         if (!et.entityName.equals(hash) || et.nbts == null || et.nbts.length == 0) continue;

         // NBT 完全匹配
         for (ThaumcraftApi.EntityTagsNBT nbt : et.nbts) {
            if (!tc.contains(nbt.name)) continue outer;

            Object val = switch (tc.get(nbt.name)) {
               case null -> null;
               case net.minecraft.nbt.IntTag t -> t.getAsInt();
               case net.minecraft.nbt.LongTag t -> t.getAsLong();
               case net.minecraft.nbt.ByteTag t -> t.getAsByte();
               case net.minecraft.nbt.ShortTag t -> t.getAsShort();
               case net.minecraft.nbt.FloatTag t -> t.getAsFloat();
               case net.minecraft.nbt.DoubleTag t -> t.getAsDouble();
               case net.minecraft.nbt.StringTag t -> t.getAsString();
               case net.minecraft.nbt.CompoundTag t -> t;
               case net.minecraft.nbt.ListTag t -> t;
               default -> null;
            };

            if (!val.equals(nbt.value)) continue outer;
         }

         // 将 NBT 值拼接到 hash
         for (ThaumcraftApi.EntityTagsNBT nbt : et.nbts) {
            Tag nbtTag = tc.get(nbt.name);
            Object val;
            if (nbtTag == null) {
               val = null;
            }
            else {
               val = switch (nbtTag) {
                  case net.minecraft.nbt.IntTag t -> t.getAsInt();
                  case net.minecraft.nbt.LongTag t -> t.getAsLong();
                  case net.minecraft.nbt.ByteTag t -> t.getAsByte();
                  case net.minecraft.nbt.ShortTag t -> t.getAsShort();
                  case net.minecraft.nbt.FloatTag t -> t.getAsFloat();
                  case net.minecraft.nbt.DoubleTag t -> t.getAsDouble();
                  case net.minecraft.nbt.StringTag t -> t.getAsString();
                  case net.minecraft.nbt.CompoundTag t -> t;
                  case net.minecraft.nbt.ListTag t -> t;
                  default -> null;
               };
            }

            hash += nbt.name + val;
         }
      }


      // 实体状态处理
      if (entity instanceof LivingEntity le && le.isBaby()) {
         hash += "CHILD";
      }

      if (entity instanceof Creeper creeper) {
         if (creeper.isPowered()) hash += "POWERED";
         if (creeper.getSwellDir() > 0) hash += "FLASHING";
      }

      if (entity instanceof EntityGolemBase golem) {
         hash += golem.getGolemType().name();
      }

      // 最终 hash
      return hash.hashCode();
   }
//   private static int generateEntityHash(Entity entity) {
//      ResourceLocation resourceLocation = entity.getType().arch$registryName();
//      String hash = resourceLocation != null?resourceLocation.toString():null;//EntityList.getEntityString(entity);
//      if (hash == null) {
//         hash = "generic";
//      }
//
//      if (entity instanceof Player) {
//         hash = "player_" + entity.getName().getString();
//      }
//
//      label101:
//      for(ThaumcraftApi.EntityTags et : ThaumcraftApi.scanEntities) {
//         if (et.entityName.equals(hash) && et.nbts != null && et.nbts.length != 0) {
//            CompoundTag tc = new CompoundTag();
//            entity.saveAdditional(tc);
//
//            for(ThaumcraftApi.EntityTagsNBT nbt : et.nbts) {
//               if (!tc.contains(nbt.name)) {
//                  continue label101;
//               }
//
//               Object val = Utils.getNBTDataFromId(tc, tc.getTagType(nbt.name), nbt.name);
//               Class c = val.getClass();
//
//               try {
//                  if (!c.cast(val).equals(c.cast(nbt.value))) {
//                     continue label101;
//                  }
//               } catch (Exception var13) {
//                  continue label101;
//               }
//            }
//
//            for(ThaumcraftApi.EntityTagsNBT nbt : et.nbts) {
//               Object val = Utils.getNBTDataFromId(tc, tc.func_150299_b(nbt.name), nbt.name);
//               Class c = val.getClass();
//
//               try {
//                  hash = hash + nbt.name + c.cast(nbt.value);
//               } catch (Exception ignored) {
//               }
//            }
//         }
//      }
//
//      if (entity instanceof LivingEntity) {
//         LivingEntity le = (LivingEntity)entity;
//         if (le.isChild()) {
//            hash = hash + "CHILD";
//         }
//      }
//
//      if (entity instanceof EntityZombie && ((EntityZombie)entity).isVillager()) {
//         hash = hash + "VILLAGER";
//      }
//
//      if (entity instanceof EntityCreeper) {
//         if (((EntityCreeper)entity).getCreeperState() == 1) {
//            hash = hash + "FLASHING";
//         }
//
//         if (((EntityCreeper)entity).getPowered()) {
//            hash = hash + "POWERED";
//         }
//      }
//
//      if (entity instanceof EntityGolemBase) {
//         hash = hash + ((EntityGolemBase)entity).getGolemType().name();
//      }
//
//      return hash.hashCode();
//   }

   public static int generateItemHash(Item item) {
      return GenerateItemHash.generateItemHash(item);
//      ItemStack t = new ItemStack(item, 1, meta);
//
//      try {
//         if (t.isItemStackDamageable() || !t.getHasSubtypes()) {
//            meta = -1;
//         }
//      } catch (Exception ignored) {
//      }
//
//      if (ThaumcraftApi.groupedObjectTags.containsKey(Arrays.asList(item, meta))) {
//         meta = ((int[])ThaumcraftApi.groupedObjectTags.get(Arrays.asList(item, meta)))[0];
//      }
//
//      StringBuilder hash;
//      try {
//         GameRegistry.UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(item);
//         if (ui != null) {
//            hash = new StringBuilder(ui + ":" + meta);
//         } else {
//            hash = new StringBuilder(t.getUnlocalizedName() + ":" + meta);
//         }
//      } catch (Exception var14) {
//         hash = new StringBuilder("oops:" + meta);
//      }
//
//      if (!ThaumcraftApi.objectTags.containsKey(Arrays.asList(item, meta))) {
//         for(List l : ThaumcraftApi.objectTags.keySet()) {
//            String name = ((Item)l.get(0)).getUnlocalizedName();
//            if ((Item.itemRegistry.getObject(name) == item || Block.blockRegistry.getObject(name) == Block.getBlockFromItem(item)) && l.get(1) instanceof int[]) {
//               int[] range = (int[])l.get(1);
//               Arrays.sort(range);
//               if (Arrays.binarySearch(range, meta) >= 0) {
//                  GameRegistry.UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(item);
//                  if (ui != null) {
//                     hash = new StringBuilder(ui.toString());
//                  } else {
//                     hash = new StringBuilder("" + t.getUnlocalizedName());
//                  }
//
//                  for(int r : range) {
//                     hash.append(":").append(r);
//                  }
//
//                  return hash.toString().hashCode();
//               }
//            }
//         }
//
//         if (!ThaumcraftApi.objectTags.containsKey(Arrays.asList(item, -1)) && meta == -1) {
//            int index = 0;
//            boolean found = false;
//
//            do {
//               found = ThaumcraftApi.objectTags.containsKey(Arrays.asList(item, index));
//               ++index;
//            } while(index < 16 && !found);
//
//            if (found) {
//               GameRegistry.UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(item);
//               if (ui != null) {
//                  hash = new StringBuilder(ui + ":" + index);
//               } else {
//                  hash = new StringBuilder(t.getUnlocalizedName() + ":" + index);
//               }
//            }
//         }
//      }
//
//      return hash.toString().hashCode();
   }

   public static AspectList generateEntityAspects(Entity entity) {
      AspectList tags = null;
      String s = null;

      try {
         s = EntityList.getEntityString(entity);
      } catch (Throwable var11) {
         try {
            s = entity.getName().getString();
         } catch (Throwable ignored) {
         }
      }

      if (s == null) {
         s = "generic";
      }

      if (entity instanceof Player) {
         s = "player_" + entity.getName().getString();
         tags = new AspectList();
         tags.add(Aspect.MAN, 4);
         if (entity.getName().getString().equalsIgnoreCase("azanor")) {
            tags.add(Aspect.ELDRITCH, 20);
         } else if (entity.getName().getString().equalsIgnoreCase("direwolf20")) {
            tags.add(Aspect.BEAST, 20);
         } else if (entity.getName().getString().equalsIgnoreCase("pahimar")) {
            tags.add(Aspect.EXCHANGE, 20);
         } else {
            Random rand = new Random(s.hashCode());
            Aspect[] posa = Aspect.aspects.values().toArray(new Aspect[0]);
            tags.add(posa[rand.nextInt(posa.length)], 4);
            tags.add(posa[rand.nextInt(posa.length)], 4);
            tags.add(posa[rand.nextInt(posa.length)], 4);
         }
      } else {
         label73:
         for(ThaumcraftApi.EntityTags et : ThaumcraftApi.scanEntities) {
            if (et.entityName.equals(s)) {
               if (et.nbts != null && et.nbts.length != 0) {
                  CompoundTag tc = new CompoundTag();
                  entity.saveAdditional(tc);

                  for(ThaumcraftApi.EntityTagsNBT nbt : et.nbts) {
                     if (!tc.hasKey(nbt.name) || !Utils.getNBTDataFromId(tc, tc.func_150299_b(nbt.name), nbt.name).equals(nbt.value)) {
                        continue label73;
                     }
                  }

               }
                tags = et.aspects;
            }
         }
      }

      return tags;
   }

   private static AspectList generateNodeAspects(Level world, String node) {
      AspectList tags = new AspectList();
      ArrayList<Integer> loc = (ArrayList)TileNode.locations.get(node);
      if (loc != null && !loc.isEmpty()) {
         int dim = loc.get(0);
         int x = loc.get(1);
         int y = loc.get(2);
         int z = loc.get(3);
         if (dim == world.dimension()) {
            BlockEntity tnb = world.getBlockEntity(x, y, z);
            if (tnb instanceof INode) {
               AspectList ta = ((INode)tnb).getAspects();

               for(Aspect a : ta.getAspectsSorted()) {
                  tags.merge(a, Math.max(4, ta.getAmount(a) / 10));
               }

               switch (((INode)tnb).getNodeType()) {
                  case UNSTABLE:
                     tags.merge(Aspect.ENTROPY, 4);
                     break;
                  case HUNGRY:
                     tags.merge(Aspect.HUNGER, 4);
                     break;
                  case TAINTED:
                     tags.merge(Aspect.TAINT, 4);
                     break;
                  case PURE:
                     tags.merge(Aspect.HEAL, 2);
                     tags.add(Aspect.ORDER, 2);
                     break;
                  case DARK:
                     tags.merge(Aspect.DEATH, 2);
                     tags.add(Aspect.DARKNESS, 2);
               }
            }
         }
      }

      return tags.size() > 0 ? tags : null;
   }

   public static boolean isValidScanTarget(Player player, ScanResult scan, String prefix) {
      if (scan == null) {
         return false;
      } else if (prefix.equals("@") && !isValidScanTarget(player, scan, "#")) {
         return false;
      } else {
         if (scan.type == 1) {
            if (ThaumcraftApi.groupedObjectTags.containsKey(Arrays.asList(Item.getItemById(scan.id), scan.meta))) {
               scan.meta = ((int[])ThaumcraftApi.groupedObjectTags.get(Arrays.asList(Item.getItemById(scan.id), scan.meta)))[0];
            }

            List<String> list = Thaumcraft.getScannedObjects().get(player.getName().getString());
             return list == null || !list.contains(prefix + generateItemHash(Item.getItemById(scan.id), scan.meta));
         } else if (scan.type == 2) {
            if (scan.entity instanceof EntityItem) {
               EntityItem item = (EntityItem)scan.entity;
               ItemStack t = item.getEntityItem().copy();
               t.stackSize = 1;
               if (ThaumcraftApi.groupedObjectTags.containsKey(Arrays.asList(t.getItem(), t.getDamageValue()))) {
                  t.setItemDamage(((int[])ThaumcraftApi.groupedObjectTags.get(Arrays.asList(t.getItem(), t.getDamageValue())))[0]);
               }

               List<String> list = Thaumcraft.getScannedObjects().get(player.getName().getString());
                return list == null || !list.contains(prefix + generateItemHash(t.getItem(), t.getDamageValue()));
            } else {
               List<String> list = Thaumcraft.getScannedEntities().get(player.getName().getString());
                return list == null || !list.contains(prefix + generateEntityHash(scan.entity));
            }
         } else if (scan.type == 3) {
            List<String> list = Thaumcraft.getScannedPhenomena().get(player.getName().getString());
             return list == null || !list.contains(prefix + scan.phenomena);
         }

         return true;
      }
   }

   public static boolean hasBeenScanned(Player player, ScanResult scan) {
      if (scan.type == 1) {
         if (ThaumcraftApi.groupedObjectTags.containsKey(Arrays.asList(Item.getItemById(scan.id), scan.meta))) {
            scan.meta = ((int[])ThaumcraftApi.groupedObjectTags.get(Arrays.asList(Item.getItemById(scan.id), scan.meta)))[0];
         }

         List<String> list = Thaumcraft.getScannedObjects().get(player.getName().getString());
          return list != null && (list.contains("@" + generateItemHash(Item.getItemById(scan.id), scan.meta)) || list.contains("#" + generateItemHash(Item.getItemById(scan.id), scan.meta)));
      } else if (scan.type == 2) {
         if (scan.entity instanceof EntityItem) {
            EntityItem item = (EntityItem)scan.entity;
            ItemStack t = item.getEntityItem().copy();
            t.stackSize = 1;
            if (ThaumcraftApi.groupedObjectTags.containsKey(Arrays.asList(t.getItem(), t.getDamageValue()))) {
               t.setItemDamage(((int[])ThaumcraftApi.groupedObjectTags.get(Arrays.asList(t.getItem(), t.getDamageValue())))[0]);
            }

            List<String> list = Thaumcraft.getScannedObjects().get(player.getName().getString());
             return list != null && (list.contains("@" + generateItemHash(t.getItem(), t.getDamageValue())) || list.contains("#" + generateItemHash(t.getItem(), t.getDamageValue())));
         } else {
            List<String> list = Thaumcraft.getScannedEntities().get(player.getName().getString());
             return list != null && (list.contains("@" + generateEntityHash(scan.entity)) || list.contains("#" + generateEntityHash(scan.entity)));
         }
      } else if (scan.type == 3) {
         List<String> list = Thaumcraft.getScannedPhenomena().get(player.getName().getString());
          return list != null && (list.contains("@" + scan.phenomena) || list.contains("#" + scan.phenomena));
      }

      return false;
   }

   public static boolean completeScan(Player player, ScanResult scan, String prefix) {
      AspectList aspects = null;
      Thaumcraft var10000 = Thaumcraft.instance;
      PlayerKnowledge rp = Thaumcraft.playerKnowledge;
      boolean ret = false;
      boolean scannedByThaumometer = prefix.equals("#") && !isValidScanTarget(player, scan, "@");
      Object clue = null;
      if (scan.type == 1) {
         if (ThaumcraftApi.groupedObjectTags.containsKey(Arrays.asList(Item.getItemById(scan.id), scan.meta))) {
            scan.meta = ((int[])ThaumcraftApi.groupedObjectTags.get(Arrays.asList(Item.getItemById(scan.id), scan.meta)))[0];
         }

         aspects = ThaumcraftCraftingManager.getObjectTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta));
         aspects = ThaumcraftCraftingManager.getBonusTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta), aspects);
         if (aspects.size() == 0 && scan.id > 0) {
            aspects = ThaumcraftCraftingManager.getObjectTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta));
            aspects = ThaumcraftCraftingManager.getBonusTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta), aspects);
         }

         if (validScan(aspects, player)) {
            clue = new ItemStack(Item.getItemById(scan.id), 1, scan.meta);
            Thaumcraft.researchManager.completeScannedObject(player, prefix + generateItemHash(Item.getItemById(scan.id), scan.meta));
            ret = true;
         }
      } else if (scan.type == 2) {
         if (scan.entity instanceof EntityItem) {
            EntityItem item = (EntityItem)scan.entity;
            ItemStack t = item.getEntityItem().copy();
            t.stackSize = 1;
            if (ThaumcraftApi.groupedObjectTags.containsKey(Arrays.asList(t.getItem(), t.getDamageValue()))) {
               t.setItemDamage(((int[])ThaumcraftApi.groupedObjectTags.get(Arrays.asList(t.getItem(), t.getDamageValue())))[0]);
            }

            aspects = ThaumcraftCraftingManager.getObjectTags(t);
            aspects = ThaumcraftCraftingManager.getBonusTags(t, aspects);
            if (validScan(aspects, player)) {
               clue = item.getEntityItem();
               Thaumcraft.researchManager.completeScannedObject(player, prefix + generateItemHash(item.getEntityItem().getItem(), item.getEntityItem().getDamageValue()));
               ret = true;
            }
         } else {
            aspects = generateEntityAspects(scan.entity);
            if (validScan(aspects, player)) {
               clue = EntityList.getEntityString(scan.entity);
               Thaumcraft.researchManager.completeScannedEntity(player, prefix + generateEntityHash(scan.entity));
               ret = true;
            }
         }
      } else if (scan.type == 3 && scan.phenomena.startsWith("NODE")) {
         aspects = generateNodeAspects(player.level(), scan.phenomena.replace("NODE", ""));
         if (validScan(aspects, player)) {
            Thaumcraft.researchManager.completeScannedPhenomena(player, prefix + scan.phenomena);
            ret = true;
         }
      }

      if (Platform.getEnvironment() != Env.CLIENT && ret && aspects != null) {
         AspectList aspectsFinal = new AspectList();

         for(Aspect aspect : aspects.getAspectTypes()) {
            if (rp.hasDiscoveredParentAspects(player.getName().getString(), aspect)) {
               int amt = aspects.getAmount(aspect);
               if (scannedByThaumometer) {
                  amt = 0;
               }

               if (prefix.equals("#")) {
                  ++amt;
               }

               int a = checkAndSyncAspectKnowledge(player, aspect, amt);
               if (a > 0) {
                  aspectsFinal.merge(aspect, a);
               }
            }
         }

         if (clue != null) {
            ResearchManager.createClue(player.level(), player, clue, aspectsFinal);
         }
      }

      return ret;
   }

   public static int checkAndSyncAspectKnowledge(Player player, Aspect aspect, int amount) {
      Thaumcraft var10000 = Thaumcraft.instance;
      PlayerKnowledge rp = Thaumcraft.playerKnowledge;
      int save = 0;
      if (!rp.hasDiscoveredAspect(player.getName().getString(), aspect)) {
         new PacketAspectDiscoveryS2C(aspect.getTag()).sendTo((ServerPlayer)player);
         amount += 2;
         save = amount;
      }

      if (rp.getAspectPoolFor(player.getName().getString(), aspect) >= Config.aspectTotalCap) {
         amount = (int)Math.sqrt(amount);
      }

      if (amount > 1 && (float)rp.getAspectPoolFor(player.getName().getString(), aspect) >= (float)Config.aspectTotalCap * 1.25F) {
         amount = 1;
      }

      if (rp.addAspectPool(player.getName().getString(), aspect, (short)amount)) {
         new PacketAspectPoolS2C(aspect.getTag(), (short)amount, rp.getAspectPoolFor(player.getName().getString(), aspect)).sendTo((ServerPlayer)player);
         save = amount;
      }

      if (save > 0) {
         Thaumcraft.researchManager.completeAspect(player.getName().getString(), aspect, rp.getAspectPoolFor(player.getName().getString(), aspect));
      }

      return save;
   }

   public static boolean validScan(AspectList aspects, Player player) {
      Thaumcraft var10000 = Thaumcraft.instance;
      PlayerKnowledge rp = Thaumcraft.playerKnowledge;
      if (aspects != null && aspects.size() > 0) {
         for(Aspect aspect : aspects.getAspectTypes()) {
            if (aspect != null && !aspect.isPrimal() && !rp.hasDiscoveredParentAspects(player.getName().getString(), aspect)) {
               if (player.level().isClientSide()) {
                  for(Aspect parent : aspect.getComponents()) {
                     if (!rp.hasDiscoveredAspect(player.getName().getString(), parent)) {
                        PlayerNotifications.addNotification((StatCollector.translateToLocal("tc.discoveryerror")+StatCollector.translateToLocal("tc.aspect.help." + parent.getTag())));
                        break;
                     }
                  }
               }

               return false;
            }
         }

         return true;
      } else {
         if (player.level().isClientSide()) {
            PlayerNotifications.addNotification(StatCollector.translateToLocal("tc.unknownobject"));
         }

         return false;
      }
   }

   public static AspectList getScanAspects(ScanResult scan, Level world) {
      AspectList aspects = new AspectList();
      boolean ret = false;
      if (scan.type == 1) {
//         if (ThaumcraftApi.groupedObjectTags.containsKey(Arrays.asList(Item.getItemById(scan.id), scan.meta))) {
//            scan.meta = ((int[])ThaumcraftApi.groupedObjectTags.get(Arrays.asList(Item.getItemById(scan.id), scan.meta)))[0];
//         }

         aspects = ThaumcraftCraftingManager.getObjectTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta));
         aspects = ThaumcraftCraftingManager.getBonusTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta), aspects);
         if (aspects.size() == 0 && scan.id > 0) {
            aspects = ThaumcraftCraftingManager.getObjectTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta));
            aspects = ThaumcraftCraftingManager.getBonusTags(new ItemStack(Item.getItemById(scan.id), 1, scan.meta), aspects);
         }
      } else if (scan.type == 2) {
         if (scan.entity instanceof EntityItem
                 && ((EntityItem)scan.entity).getEntityItem() != null
         ) {
            EntityItem item = (EntityItem)scan.entity;
            ItemStack t = item.getEntityItem().copy();
            t.stackSize = 1;
            if (ThaumcraftApi.groupedObjectTags.containsKey(Arrays.asList(t.getItem(), t.getDamageValue()))) {
               t.setItemDamage(((int[])ThaumcraftApi.groupedObjectTags.get(Arrays.asList(t.getItem(), t.getDamageValue())))[0]);
            }

            aspects = ThaumcraftCraftingManager.getObjectTags(t);
            aspects = ThaumcraftCraftingManager.getBonusTags(t, aspects);
         } else {
            aspects = generateEntityAspects(scan.entity);
         }
      } else if (scan.type == 3 && scan.phenomena.startsWith("NODE")) {
         aspects = generateNodeAspects(world, scan.phenomena.replace("NODE", ""));
      }

      return aspects;
   }
}
