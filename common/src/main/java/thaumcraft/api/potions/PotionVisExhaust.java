package thaumcraft.api.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.entities.ITaintedMob;

import java.util.Objects;

public class PotionVisExhaust extends MobEffect
{
    public static PotionVisExhaust instance = null; // will be instantiated at runtime
    private int statusIconIndex = -1;
    
    public PotionVisExhaust(int par1, boolean par2, int par3)
    {
    	super(par1,par2,par3);
    	setIconIndex(0, 0);
    }
    
    public static void init()
    {
    	instance.setPotionName("potion.visexhaust");
    	instance.setIconIndex(5, 1);
    	instance.setEffectiveness(0.25D);
    }
    
//	@Override
//	public boolean isBadEffect() {
//		return true;
//	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public int getStatusIconIndex() {
//		Minecraft.getMinecraft().renderEngine.bindTexture(rl);
//		return super.getStatusIconIndex();
//	}
	
	static final ResourceLocation rl = new ResourceLocation("thaumcraft","textures/misc/potions.png");//TODO:Split image and get


	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		//does nothing here but other listeners to change WandCastingItem costs
	}
    
    
}
