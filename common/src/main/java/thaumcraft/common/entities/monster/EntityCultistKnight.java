package thaumcraft.common.entities.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;
import thaumcraft.common.entities.ai.combat.AICultistHurtByTarget;

public class EntityCultistKnight extends EntityCultist {
   public EntityCultistKnight(World p_i1745_1_) {
      super(p_i1745_1_);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityLivingBase.class, 1.0F, false));
      this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
      this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
      this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 0.8));
      this.tasks.addTask(7, new EntityAIWander(this, 0.8));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new AICultistHurtByTarget(this, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(36.0F);
   }

   protected void addRandomArmor() {
      this.setCurrentItemOrArmor(4, new ItemStack(ConfigItems.itemHelmetCultistPlate));
      this.setCurrentItemOrArmor(3, new ItemStack(ConfigItems.itemChestCultistPlate));
      this.setCurrentItemOrArmor(2, new ItemStack(ConfigItems.itemLegsCultistPlate));
      this.setCurrentItemOrArmor(1, new ItemStack(ConfigItems.itemBootsCultist));
      if (this.rand.nextFloat() < (this.worldObj.difficultySetting == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
         int i = this.rand.nextInt(5);
         if (i == 0) {
            this.setCurrentItemOrArmor(0, new ItemStack(ConfigItems.itemSwordVoid));
            this.setCurrentItemOrArmor(4, new ItemStack(ConfigItems.itemHelmetCultistRobe));
         } else {
            this.setCurrentItemOrArmor(0, new ItemStack(ConfigItems.itemSwordThaumium));
            if (this.rand.nextBoolean()) {
               this.setCurrentItemOrArmor(4, null);
            }
         }
      } else {
         this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
      }

   }

   protected void enchantEquipment() {
      float f = this.worldObj.func_147462_b(this.posX, this.posY, this.posZ);
      if (this.getHeldItem() != null && this.rand.nextFloat() < 0.25F * f) {
         EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItem(), (int)(5.0F + f * (float)this.rand.nextInt(18)));
      }

   }
}
