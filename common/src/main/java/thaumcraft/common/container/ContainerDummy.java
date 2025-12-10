package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerDummy extends Container {
   public boolean canInteractWith(EntityPlayer var1) {
      return false;
   }
}
