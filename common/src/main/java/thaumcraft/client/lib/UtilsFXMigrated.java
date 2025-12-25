package thaumcraft.client.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import thaumcraft.client.fx.migrated.particles.FXSparkle;
import thaumcraft.common.ClientFXUtils;

public class UtilsFXMigrated {
    public static void infusedStoneSparkle(Level world, int x, int y, int z, int colorValue) {
       if (world instanceof ClientLevel clientLevel) {
          int color = switch (colorValue) {
              case 1 -> 1;
              case 2 -> 4;
              case 3 -> 2;
              case 4 -> 3;
              case 5 -> 6;
              case 6 -> 5;
              default -> -1;
          };//wtf why anazor want this?

           for(int a = 0; a < ClientFXUtils.particleCount(3); ++a) {
             FXSparkle fx = new FXSparkle(clientLevel,
                     (float)x + world.getRandom().nextFloat(),
                     (float)y + world.getRandom().nextFloat(),
                     (float)z + world.getRandom().nextFloat(),
                     1.75F,
                     color == -1 ? world.getRandom().nextInt(5) : color,
                     3 + world.getRandom().nextInt(3));
             fx.setGravity(0.1F);
             Minecraft.getInstance().particleEngine.add(fx);
          }
       }
    }
}
