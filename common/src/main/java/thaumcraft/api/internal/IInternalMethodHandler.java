package thaumcraft.api.internal;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

@Deprecated(forRemoval = true,since = "open")
public interface IInternalMethodHandler {

	void generateVisEffect(ResourceKey<Level> dim, int x, int y, int z, int x2, int y2, int z2, int color);
	boolean isResearchComplete(String username, String researchkey);
	ItemStack getStackInRowAndColumn(Object instance, int row, int column);
	AspectList<Aspect>getObjectAspects(ItemStack is);
	AspectList<Aspect>getBonusObjectTags(ItemStack is, AspectList<Aspect>ot);
	AspectList<Aspect>generateTags(Item item, int meta);
	boolean consumeVisFromWand(ItemStack wand, Player player, AspectList<Aspect>cost, boolean doit, boolean crafting);
	boolean consumeVisFromWandCrafting(ItemStack wand, Player player, AspectList<Aspect>cost, boolean doit);
	boolean consumeVisFromInventory(Player player, AspectList<Aspect>cost);
	void addWarpToPlayer(Player player, int amount, boolean temporary);
	void addStickyWarpToPlayer(Player player, int amount);
	boolean hasDiscoveredAspect(String username, Aspect aspect);
	AspectList<Aspect>getDiscoveredAspects(String username);
	
}
