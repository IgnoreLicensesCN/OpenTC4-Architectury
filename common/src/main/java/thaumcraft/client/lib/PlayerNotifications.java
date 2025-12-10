package thaumcraft.client.lib;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.config.Config;

public class PlayerNotifications {
   public static ArrayList<Notification> notificationList = new ArrayList<>();
   public static ArrayList<AspectNotification> aspectList = new ArrayList<>();

   public static void addNotification(String text) {
      addNotification(text, null, 16777215);
   }

   public static void addAspectNotification(Aspect aspect) {
      long time = System.nanoTime() / 1000000L + (long)Minecraft.getMinecraft().theWorld.rand.nextInt(1000);
      float x = 0.4F + Minecraft.getMinecraft().theWorld.rand.nextFloat() * 0.2F;
      float y = 0.4F + Minecraft.getMinecraft().theWorld.rand.nextFloat() * 0.2F;
      aspectList.add(new AspectNotification(aspect, x, y, time, time + 1500L));
   }

   public static void addNotification(String text, Aspect aspect) {
      addNotification(text, aspect.getImage(), aspect.getColor());
   }

   public static void addNotification(String text, ResourceLocation image) {
      addNotification(text, image, 16777215);
   }

   public static void addNotification(String text, ResourceLocation image, int color) {
      long time = System.nanoTime() / 1000000L;
      long timeBonus = notificationList.isEmpty() ? (long)(Config.notificationDelay / 2) : 0L;
      notificationList.add(new Notification(text, image, time + (long)Config.notificationDelay + timeBonus, time + (long)(Config.notificationDelay / 4), color));
   }

   public static ArrayList getListAndUpdate(long time) {
      ArrayList<Notification> temp = new ArrayList<>();
      boolean first = true;

      for(Notification li : notificationList) {
         if (li.expire >= time) {
            if (!first) {
               temp.add(new Notification(li.text, li.image, time + (long)Config.notificationDelay, li.created, li.color));
            } else {
               temp.add(li);
            }
         }

         first = false;
      }

      notificationList = temp;
      return temp;
   }

   public static ArrayList getAspectListAndUpdate(long time) {
      ArrayList<AspectNotification> temp = new ArrayList<>();

      for(AspectNotification li : aspectList) {
         if (li.expire >= time) {
            temp.add(li);
         }
      }

      aspectList = temp;
      return temp;
   }

   public static class Notification {
      public String text;
      public ResourceLocation image;
      public long expire;
      public long created;
      public int color;

      public Notification(String text, ResourceLocation image, long expire, long created, int color) {
         this.text = text;
         this.image = image;
         this.expire = expire;
         this.created = created;
         this.color = color;
      }
   }

   public static class AspectNotification {
      public Aspect aspect;
      public float startX;
      public float startY;
      public long expire;
      public long created;

      public AspectNotification(Aspect aspect, float startX, float startY, long created, long expire) {
         this.aspect = aspect;
         this.startX = startX;
         this.startY = startY;
         this.expire = expire;
         this.created = created;
      }
   }
}
