package thaumcraft.api.wands;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public interface IWandSpellEventListenableItem {
    //let focus call it if anyone want."Need 'craft to repair' after using for 3 times?"
    void onWandSpellEvent(WandSpellEventType event, Player player, ItemStack wandCasting, BlockPos atBlockPos, Vec3 atVec3);

}
