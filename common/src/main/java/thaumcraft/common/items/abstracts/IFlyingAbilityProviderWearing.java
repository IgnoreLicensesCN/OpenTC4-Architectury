package thaumcraft.common.items.abstracts;

import com.google.common.collect.MapMaker;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.lib.resourcelocations.FlyingProviderTypeResourceLocation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.utils.bauble.BaubleUtils.forEachBauble;

public interface IFlyingAbilityProviderWearing {
    class FlyingAbilityProviderCheck{

        public static final Map<Player, Set<FlyingProviderTypeResourceLocation>> flyingEnabledPlayers = new MapMaker().weakKeys().makeMap();

        public static void unregisterFlyingProviderForPlayer(Player player, FlyingProviderTypeResourceLocation flyingProviderTypeResourceLocation) {
            var map = flyingEnabledPlayers.get(player);
            if (map == null) {return;}
            map.remove(flyingProviderTypeResourceLocation);
        }

        public static void registerFlyingProviderForPlayer(Player player, FlyingProviderTypeResourceLocation flyingProviderTypeResourceLocation) {
            var providers = flyingEnabledPlayers.computeIfAbsent(player, k -> ConcurrentHashMap.newKeySet());
            providers.add(flyingProviderTypeResourceLocation);
        }

        public static void checkFlyingProviderForPlayer(Player player) {
            var flyingProviderTypes = flyingEnabledPlayers.get(player);
            if (flyingProviderTypes != null && !flyingProviderTypes.isEmpty()) {
                boolean[] canFlyWithFlyingProvider = new boolean[]{false};
                for (var stack:player.getArmorSlots()){
                    if (stack.getItem() instanceof IFlyingAbilityProviderWearing flyingAbilityProvider) {
                        if (flyingAbilityProvider.canProvideFlyingAbilityWhenEquipped(stack,player)){
                            canFlyWithFlyingProvider[0] = true;
                            break;
                        }
                    }
                }
                if (!canFlyWithFlyingProvider[0]){
                    forEachBauble(player,IFlyingAbilityProviderWearing.class,(slot,stack,item) -> {
                        if (item.canProvideFlyingAbilityWhenEquipped(stack,player)){
                            canFlyWithFlyingProvider[0] = true;
                            return true;
                        }
                        return false;
                    });
                }
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
