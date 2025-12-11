package thaumcraft.common.lib.events;


import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.Keys;
import thaumcraft.common.entities.golems.ItemGolemBell;
import thaumcraft.common.items.armor.Hover;
import thaumcraft.common.items.armor.ItemHoverHarness;
import thaumcraft.common.items.wands.WandCastingItem;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;
import thaumcraft.common.lib.network.misc.PacketItemKeyC2S;
import thaumcraft.common.lib.network.misc.PacketItemKeyToServer;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class KeyHandler {
   public KeyMapping keyF = Keys.keyF;
   public KeyMapping keyH = Keys.keyH;
   public KeyMapping keyG = Keys.keyG;
   private boolean keyPressedF = false;
   private boolean keyPressedH = false;
   private boolean keyPressedG = false;
   public static boolean radialActive = false;
   public static boolean radialLock = false;
   public static long lastPressF = 0L;
   public static long lastPressH = 0L;
   public static long lastPressG = 0L;

   public KeyHandler() {
      platformUtils.registerKeyBinding(keyF);
      platformUtils.registerKeyBinding(keyH);
      platformUtils.registerKeyBinding(keyG);
      platformUtils.registerClientTickStartEvent(() -> {
         {
            Minecraft mc = Minecraft.getInstance();
            if (this.keyF.isDown()) {

               if (mc.isWindowActive() && mc.screen == null) {
                  Player player = mc.player;
                  if (player != null) {
                     if (!this.keyPressedF) {
                        lastPressF = System.currentTimeMillis();
                        radialLock = false;
                     }

                     ItemStack holdItemStack = player.getMainHandItem();
                     Item holdItem = holdItemStack.getItem();
                     if (!radialLock && !holdItemStack.isEmpty() && holdItem instanceof WandCastingItem && !((WandCastingItem)holdItem).isSceptre(holdItemStack)) {
                        if (player.isCrouching()) {
                           PacketHandler.INSTANCE.sendToServer(new PacketFocusChangeToServer(player, "REMOVE"));
                        } else {
                           radialActive = true;
                        }
                     } else if (!holdItemStack.isEmpty() && holdItem instanceof ItemGolemBell && !this.keyPressedF) {
                        PacketHandler.INSTANCE.sendToServer(new PacketItemKeyToServer(player, 0));
                     }else {
                        holdItemStack = player.getOffhandItem();
                        if (!radialLock && !holdItemStack.isEmpty() && holdItem instanceof WandCastingItem && !((WandCastingItem)holdItem).isSceptre(holdItemStack)) {
                           if (player.isCrouching()) {
                              PacketHandler.INSTANCE.sendToServer(new PacketFocusChangeToServer(player, "REMOVE"));
                           } else {
                              radialActive = true;
                           }
                        } else if (!holdItemStack.isEmpty() && holdItem instanceof ItemGolemBell && !this.keyPressedF) {
                           PacketHandler.INSTANCE.sendToServer(new PacketItemKeyToServer(player, 0));
                        }
                     }
                  }

                  this.keyPressedF = true;
               }
            } else {
               radialActive = false;
               if (this.keyPressedF) {
                  lastPressF = System.currentTimeMillis();
               }

               this.keyPressedF = false;
            }

            if (this.keyH.isDown()) {
               if (mc.isWindowActive() && mc.screen == null) {
                  Player player = mc.player;
                  if (player != null) {
                     if (!this.keyPressedH) {
                        lastPressH = System.currentTimeMillis();
                     }
                     ItemStack chestArmor = player.getItemBySlot(EquipmentSlot.CHEST);

                     if (chestArmor.getItem() instanceof ItemHoverHarness && !this.keyPressedH) {
                        Hover.toggleHover(player, player.getEntityId(), chestArmor);
                     }
                  }

                  this.keyPressedH = true;
               }
            } else {
               if (this.keyPressedH) {
                  lastPressH = System.currentTimeMillis();
               }

               this.keyPressedH = false;
            }

            if (this.keyG.isDown()) {
               if (mc.isWindowActive() && mc.screen == null) {
                  Player player = mc.player;
                  if (player != null && !this.keyPressedG) {
                     lastPressG = System.currentTimeMillis();
                     new PacketItemKeyC2S(player.level().dimension(),1,player.getMainHandItem().isEmpty()?0:1).sendToServer();
//                     PacketHandler.INSTANCE.sendToServer(new PacketItemKeyToServer(player, 1));
                  }

                  this.keyPressedG = true;
               }
            } else {
               if (this.keyPressedG) {
                  lastPressG = System.currentTimeMillis();
               }

               this.keyPressedG = false;
            }
         }
      });
   }

}
