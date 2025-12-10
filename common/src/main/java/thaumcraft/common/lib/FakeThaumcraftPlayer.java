package thaumcraft.common.lib;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

public class FakeThaumcraftPlayer extends ServerPlayer {

   public static class  FakeGameProfile extends GameProfile {
      public FakeGameProfile(String name) {
         super(UUID.nameUUIDFromBytes(("FakeGameProfile:" + name).getBytes(StandardCharsets.UTF_8)), name);
      }
   }

   public FakeThaumcraftPlayer(MinecraftServer server, ServerLevel world, String name) {
      super(server, world, new FakeGameProfile(name));
   }

   @Override
   public void sendSystemMessage(Component message) {
      // 不显示聊天信息
   }

//   @Override
//   public boolean hasPermissions(int level) {
//      return false; // 没有权限执行命令
//   }

   public ChunkPos getPlayerCoordinates() {
      // 返回默认位置
      return new ChunkPos(0, 0);
   }

   // 你可以根据需要再覆盖更多方法，比如打开 GUI、触发事件等
}
