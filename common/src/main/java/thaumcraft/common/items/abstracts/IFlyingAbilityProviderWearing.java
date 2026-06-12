package thaumcraft.common.items.abstracts;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.equip.IPlayerEquippedSlotAccess;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public interface IFlyingAbilityProviderWearing {
    class FlyingAbilityProviderCheck{

        public static final Map<Player, Set<IPlayerEquippedSlotAccess>> flyingEnabledPlayers = new MapMaker().weakKeys().makeMap();

        public static void unregisterFlyingProviderForPlayer(Player player, IPlayerEquippedSlotAccess slotAccess) {
            var map = flyingEnabledPlayers.get(player);
            if (map == null) {return;}
            map.remove(slotAccess);
        }

        public static void registerFlyingProviderForPlayer(Player player, IPlayerEquippedSlotAccess slotAccess) {
            var providers = flyingEnabledPlayers.computeIfAbsent(player, k -> ConcurrentHashMap.newKeySet());
            providers.add(slotAccess);
        }

        public static void checkFlyingProviderForPlayer(Player player) {
            var flyingProviderAtSlots = flyingEnabledPlayers.get(player);
            if (flyingProviderAtSlots != null && !flyingProviderAtSlots.isEmpty()) {
                boolean[] canFlyWithFlyingProvider = new boolean[]{false};
                List<IPlayerEquippedSlotAccess> accessesToRemove = new ArrayList<>(flyingProviderAtSlots.size());
                for (var slotAccess : flyingProviderAtSlots) {
                    var stack = slotAccess.getEquippedStack(player);
                    if (stack.isEmpty() || !(stack.getItem() instanceof IFlyingAbilityProviderWearing flyingAbilityProvider)) {
                        accessesToRemove.add(slotAccess);
                    }else if (flyingAbilityProvider.canProvideFlyingAbilityWhenEquipped(stack,player)) {
                        canFlyWithFlyingProvider[0] = true;
                    }
                }
                accessesToRemove.forEach(flyingProviderAtSlots::remove);
                if (!canFlyWithFlyingProvider[0]){
                    flyingEnabledPlayers.remove(player);
                    var abilities = player.getAbilities();
                    abilities.mayfly = false;
                    abilities.flying = false;
                    if (player instanceof ServerPlayer serverPlayer){
                        serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(abilities));
                    }
                }
            }
        }
    }

    //null if cant provide
    default boolean canProvideFlyingAbilityWhenEquipped(ItemStack selfStack, Player player){
        return true;
    };
}
