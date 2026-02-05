package thaumcraft.api.internal;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

@Deprecated(forRemoval = true,since = "our source code is visible to all")
public class DummyInternalMethodHandler implements IInternalMethodHandler {

	@Override
	public void generateVisEffect(ResourceKey<Level> dim, int x, int y, int z, int x2, int y2, int z2, int color) {
		
	}

	@Override
	public boolean isResearchComplete(String username, String researchkey) {
		return false;
	}
	
	@Override
	public boolean hasDiscoveredAspect(String username, Aspect aspect) {
		return false;
	}
	
	@Override
	public AspectList<Aspect>getDiscoveredAspects(String username) {
		return null;
	}

	@Override
	public ItemStack getStackInRowAndColumn(Object instance, int row, int column) {
		return null;
	}

	@Override
	public AspectList<Aspect>getObjectAspects(ItemStack is) {
		return null;
	}

	@Override
	public AspectList<Aspect>getBonusObjectTags(ItemStack is, AspectList<Aspect>ot) {
		return null;
	}

	@Override
	public AspectList<Aspect>generateTags(Item item, int meta) {
		return null;
	}

	@Override
	public boolean consumeVisFromWand(ItemStack wand, Player player,
			AspectList<Aspect>cost, boolean doit, boolean crafting) {
		return false;
	}

	@Override
	public boolean consumeVisFromWandCrafting(ItemStack wand,
			Player player, AspectList<Aspect>cost, boolean doit) {
		return false;
	}

	@Override
	public boolean consumeVisFromInventory(Player player, AspectList<Aspect>cost) {
		return false;
	}

	@Override
	public void addWarpToPlayer(Player player, int amount, boolean temporary) {
	}

	@Override
	public void addStickyWarpToPlayer(Player player, int amount) {
	}

	
	
}
