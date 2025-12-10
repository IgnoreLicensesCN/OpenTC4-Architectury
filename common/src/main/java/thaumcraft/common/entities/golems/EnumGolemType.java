package thaumcraft.common.entities.golems;

import java.util.HashMap;
import java.util.Map;

public enum EnumGolemType {
   STRAW(10, 0, 0.38F, false, 1, 1, 75, 0),
   WOOD(20, 6, 0.35F, false, 1, 4, 75, 1),
   TALLOW(20, 9, 0.33F, false, 2, 8, 75, 2),
   CLAY(25, 9, 0.33F, true, 1, 8, 100, 2),
   FLESH(15, 6, 0.35F, false, 2, 4, 40, 1),
   STONE(30, 12, 0.32F, true, 1, 16, 100, 3),
   IRON(35, 15, 0.31F, true, 1, 32, 125, 4),
   THAUMIUM(40, 15, 0.32F, true, 2, 32, 100, 4);

   public final int health;
   public final int armor;
   public final float speed;
   public final boolean fireResist;
   public final int upgrades;
   public final int carry;
   public final int regenDelay;
   public final int strength;
   private static Map codeToTypeMapping;

   public static EnumGolemType getType(int i) {
      if (codeToTypeMapping == null) {
         initMapping();
      }

      return (EnumGolemType)codeToTypeMapping.get(i);
   }

   private static void initMapping() {
      codeToTypeMapping = new HashMap<>();

      for(EnumGolemType s : values()) {
         codeToTypeMapping.put(s.ordinal(), s);
      }

   }

   EnumGolemType(int health, int armor, float speed, boolean fireResist, int upgrades, int carry, int regenDelay, int strength) {
      this.health = health;
      this.armor = armor;
      this.speed = speed;
      this.fireResist = fireResist;
      this.upgrades = upgrades;
      this.carry = carry;
      this.regenDelay = regenDelay;
      this.strength = strength;
   }
}
