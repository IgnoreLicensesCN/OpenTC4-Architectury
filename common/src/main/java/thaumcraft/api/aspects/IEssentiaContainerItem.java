package thaumcraft.api.aspects;

import net.minecraft.world.item.ItemStack;

/**
 * 
 * @author azanor
 * 
 * Used by wispy essences and essentia phials to hold their aspects. 
 * Useful for similar item containers that store their aspect information in nbt form so TC
 * automatically picks up the aspects they contain
 *
 */
public interface IEssentiaContainerItem {
	AspectList<Aspect> getAspects(ItemStack itemstack);
	void setAspects(ItemStack itemstack, AspectList<Aspect> aspects);
}

//Example implementation
/*  
	@Override
	public AspectList<Aspect>getAspects(ItemStack itemstack) {
		if (itemstack.hasTagCompound()) {
			AspectList<Aspect>aspects = new AspectList<>();
			aspects.load(itemstack.getTagCompound());
			return aspects.size()>0?aspects:null;
		}
		return null;
	}
	
	@Override
	public void setAspects(ItemStack itemstack, AspectList<Aspect>aspects) {
		if (!itemstack.hasTagCompound()) itemstack.setTagCompound(new CompoundTag());
		aspects.saveAdditional(itemstack.getTagCompound());
	}
*/