package thaumcraft.common.lib.fakeplayer;

import com.google.common.collect.MapMaker;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class FakeThaumcraftPlayer extends ServerPlayer {

   public static class FakeThaumcraftPlayerFactory {
//      private static final GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
      // Map fromAspectVisList all active fake player usernames to their entities
      private static final Map<GameProfile, FakeThaumcraftPlayer> fakeThaumcraftPlayers = new MapMaker().weakValues().makeMap();
//      private static FakeThaumcraftPlayer MINECRAFT_PLAYER = null;
//
//      public static FakeThaumcraftPlayer getMinecraft(ServerLevel world)
//      {
//         if (MINECRAFT_PLAYER == null)
//         {
//            MINECRAFT_PLAYER = FakeThaumcraftPlayerFactory.get(world,  MINECRAFT);
//         }
//         return MINECRAFT_PLAYER;
//      }

      /**
       * Get a fake player with a given username,
       * Mods should either hold weak references to the return value, or listen for a
       * WorldEvent.Unload and kill all references to prevent worlds staying in memory.
       */
      public static FakeThaumcraftPlayer get(ServerLevel world, GameProfile profile){
          return fakeThaumcraftPlayers.computeIfAbsent(profile, prof -> new FakeThaumcraftPlayer(world, prof));
      }

      /**
       * Get a fake player with a given username,
       * Mods should either hold weak references to the return value, or listen for a 
       * WorldEvent.Unload and kill all references to prevent worlds staying in memory.
       */
      public static FakeThaumcraftPlayer get(ServerLevel world, String username)
      {
         return get(world,new FakeGameProfile(username));
      }

      //TODO:Use this
      public static void unloadWorld(ServerLevel world)
      {
          fakeThaumcraftPlayers.entrySet()
                  .removeIf(
                          entry -> entry.getValue()
                          .level() == world);
      }
   }


   public static class FakeGameProfile extends GameProfile {
      public FakeGameProfile(String name) {
         super(UUID.nameUUIDFromBytes(("FakeGameProfile:" + name).getBytes(StandardCharsets.UTF_8)), name);
      }
   }

   protected FakeThaumcraftPlayer(ServerLevel world, GameProfile profile){
      this(world.getServer(),world,profile);
   }
   protected FakeThaumcraftPlayer(MinecraftServer server, ServerLevel world, GameProfile profile) {
      super(server, world, profile);
   }


//   @Override
//   public boolean hasPermissions(int level) {
//      return false; // 没有权限执行命令
//   }

   public ChunkPos getPlayerCoordinates() {
      // 返回默认位置
      return new ChunkPos(0, 0);
   }

   @Override
   public void sendChatMessage(OutgoingChatMessage outgoingChatMessage, boolean bl, ChatType.Bound bound) {}

   @Override
   public void sendSystemMessage(Component message) {}
   @Override
   public void sendSystemMessage(Component component, boolean bl) {}
   @Override
   public void openItemGui(ItemStack itemStack, InteractionHand interactionHand) {}

    @Override
    public void sendTexturePack(String string, String string2, boolean bl, @Nullable Component component) {
    }
}
