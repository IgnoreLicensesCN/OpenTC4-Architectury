package thaumcraft.common.lib;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.Player;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class FakeThaumcraftPlayer extends Player {
   public FakeThaumcraftPlayer(World world, GameProfile name) {
      super(world, name);
   }

   public void addChatMessage(String s) {
   }

   public boolean canCommandSenderUseCommand(int i, String s) {
      return false;
   }

   public ChunkCoordinates getPlayerCoordinates() {
      return new ChunkCoordinates(0, 0, 0);
   }

   public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
   }

   public void addChatMessage(IChatComponent var1) {
   }
}
