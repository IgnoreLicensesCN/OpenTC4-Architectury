package thaumcraft.common.items.abstracts;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.equip.ILivingEntityEquippedSlotAccess;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public interface IFlyingAbilityProviderWearing {
    class FlyingAbilityProviderCheck{

        public static final Map<Player, Set<ILivingEntityEquippedSlotAccess>> flyingEnabledPlayers = new MapMaker().weakKeys().makeMap();

        public static void letPlayerDown(Player player) {
            if (!player.isCreative() && !player.isSpectator()) {
                var abilities = player.getAbilities();
                abilities.mayfly = false;
                abilities.flying = false;
                if (player instanceof ServerPlayer serverPlayer){
                    serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(abilities));
                }
            }
        }
        public static void flyPlayer(Player player) {
            if (!player.isCreative() && !player.isSpectator()) {
                var abilities = player.getAbilities();
                abilities.mayfly = true;
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(abilities));
                }
            }
        }

        public static void unregisterFlyingProviderForPlayer(Player player, ILivingEntityEquippedSlotAccess slotAccess) {
            var slotaccesses = flyingEnabledPlayers.get(player);
            if (slotaccesses == null) {return;}
            slotaccesses.remove(slotAccess);
            if (slotaccesses.isEmpty()) {
                letPlayerDown(player);
            }
        }

        public static void registerFlyingProviderForPlayer(Player player, ILivingEntityEquippedSlotAccess slotAccess) {
            var providers = flyingEnabledPlayers.computeIfAbsent(player, k -> ConcurrentHashMap.newKeySet());
            providers.add(slotAccess);
            flyPlayer(player);
        }

        public static void checkFlyingProviderForPlayer(Player player) {
            var flyingProviderAtSlots = flyingEnabledPlayers.get(player);
            if (flyingProviderAtSlots != null && !flyingProviderAtSlots.isEmpty()) {
                boolean[] canFlyWithFlyingProvider = new boolean[]{false};
                List<ILivingEntityEquippedSlotAccess> accessesToRemove = new ArrayList<>(flyingProviderAtSlots.size());
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
                    letPlayerDown(player);
                }
            }
        }
    }

    //null if cant provide
    default boolean canProvideFlyingAbilityWhenEquipped(ItemStack selfStack, Player player){
        return true;
    };
}
