package thaumcraft.common.entities.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityMindSpider extends EntitySpider {
   private int lifeSpan = Integer.MAX_VALUE;

   public EntityMindSpider(World par1World) {
      super(par1World);
      this.setSize(0.3F, 0.3F);
      this.experienceValue = 1;
   }

   protected int getExperiencePoints(EntityPlayer p_70693_1_) {
      return this.isHarmless() ? 0 : super.getExperiencePoints(p_70693_1_);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(22, (byte) 0);
      this.dataWatcher.addObject(23, "");
   }

   public String getViewer() {
      return this.dataWatcher.getWatchableObjectString(23);
   }

   public void setViewer(String player) {
      this.dataWatcher.updateObject(23, String.valueOf(player));
   }

   public boolean isHarmless() {
      return this.dataWatcher.getWatchableObjectByte(22) != 0;
   }

   public void setHarmless(boolean h) {
      if (h) {
         this.lifeSpan = 1200;
      }

      this.dataWatcher.updateObject(22, (byte)(h ? 1 : 0));
   }

   protected float getSoundPitch() {
      return 0.7F;
   }

   protected Entity findPlayerToAttack() {
      double d0 = 12.0F;
      return this.worldObj.getClosestVulnerablePlayerToEntity(this, d0);
   }

   @SideOnly(Side.CLIENT)
   public float spiderScaleAmount() {
      return 0.3F;
   }

   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote && this.ticksExisted > this.lifeSpan) {
         this.setDead();
      }

   }

   public float getShadowSize() {
      return this.isHarmless() ? 0.0F : 0.1F;
   }

   protected Item getDropItem() {
      return Item.getItemById(0);
   }

   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
   }

   public boolean doesEntityNotTriggerPressurePlate() {
      return true;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
      if (!this.isHarmless()) {
         super.attackEntity(p_70785_1_, p_70785_2_);
      }
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.dataWatcher.updateObject(22, par1NBTTagCompound.getByte("harmless"));
      this.dataWatcher.updateObject(23, String.valueOf(par1NBTTagCompound.getString("viewer")));
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setByte("harmless", this.dataWatcher.getWatchableObjectByte(22));
      par1NBTTagCompound.setString("viewer", this.dataWatcher.getWatchableObjectString(23));
   }
}
