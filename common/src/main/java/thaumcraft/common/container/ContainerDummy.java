package thaumcraft.common.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.Container;

public class ContainerDummy extends Container {
   public boolean canInteractWith(Player var1) {
      return false;
   }
}
