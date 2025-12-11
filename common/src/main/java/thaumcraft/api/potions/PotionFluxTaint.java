package thaumcraft.api.potions;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;

import java.util.Objects;

public class PotionFluxTaint extends MobEffect
{
    public static PotionFluxTaint instance = null; // will be instantiated at runtime
    private int statusIconIndex = -1;
    
    public PotionFluxTaint(int par1, boolean par2, int par3)
    {
    	super(par1,par2,par3);
    	setIconIndex(0, 0);
    }
    
    public static void init()
    {
    	instance.setPotionName("potion.fluxtaint");
    	instance.setIconIndex(3, 1);
    	instance.setEffectiveness(0.25D);
    }

//	@Override
//	public boolean isBadEffect() {
//		return true;
//	}//MobEffectCategory.HARMFUL

//	@Override
//	@SideOnly(Side.CLIENT)
//	public int getStatusIconIndex() {
//		Minecraft.getMinecraft().renderEngine.bindTexture(rl);
//		return super.getStatusIconIndex();
//	}
	
	static final ResourceLocation rl = new ResourceLocation(Thaumcraft.MOD_ID,"textures/misc/potions.png");//TODO:Split and get effect icon
	public static final ResourceKey<MobEffect> FLUX_TAINT_KEY = ResourceKey.create(Registries.MOB_EFFECT, Objects.requireNonNull(ResourceLocation.tryParse("thaumcraft:flux_taint")));

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		boolean undeadFlag = Objects.equals(entity.getMobType(), MobType.UNDEAD);
		if (entity instanceof ITaintedMob) {
			entity.heal(1.0F);
		} else if (!undeadFlag && !(entity instanceof Player)) {
			entity.hurt(DamageSourceThaumcraft.getDamageSource(entity.level(),DamageSourceThaumcraft.TAINT), 1.0F);
		} else if (!undeadFlag && (entity.getMaxHealth() > 1.0F || (entity instanceof Player))) {
			entity.hurt(DamageSourceThaumcraft.getDamageSource(entity.level(),DamageSourceThaumcraft.TAINT), 1.0F);
		}
	}
//	@Override
//	public void performEffect(LivingEntity target, int par2) {
//		if (target instanceof ITaintedMob) {
//			target.heal(1);
//		} else
//		if (!target.isEntityUndead() && !(target instanceof Player))
//        {
//			target.attackEntityFrom(DamageSourceThaumcraft.taint, 1);
//        }
//		else
//		if (!target.isEntityUndead() && (target.getMaxHealth() > 1 || (target instanceof Player)))
//        {
//			target.attackEntityFrom(DamageSourceThaumcraft.taint, 1);
//        }
//	}
//

//	public boolean isReady(int par1, int par2)
//    {
//		int k = 40 >> par2;
//        return k == 0 || par1 % k == 0;
//    }
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		int k = 40 >> amplifier;
		return k > 0 && duration % k == 0;
	}
}
