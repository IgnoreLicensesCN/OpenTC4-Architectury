package thaumcraft.common.entities.monster.boss;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.damagesource.DamageSource;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;
import thaumcraft.common.entities.ai.combat.AILongRangeAttack;
import thaumcraft.common.entities.monster.EntityCultist;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockArcS2C;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkleS2C;
import thaumcraft.common.lib.network.fx.PacketFXSonic;
import thaumcraft.common.lib.utils.EntityUtils;

public class EntityEldritchWarden extends EntityThaumcraftBoss implements IRangedAttackMob, IEldritchMob {
   String[] titles = new String[]{"Aphoom-Zhah", "Basatan", "Chaugnar Faugn", "Mnomquah", "Nyogtha", "Oorn", "Shaikorth", "Rhan-Tegoth", "Rhogog", "Shudde M'ell", "Vulthoom", "Yag-Kosha", "Yibb-Tstll", "Zathog", "Zushakon"};
   boolean fieldFrenzy = false;
   int fieldFrenzyCounter = 0;
   boolean lastBlast = false;
   public float armLiftL = 0.0F;
   public float armLiftR = 0.0F;

   public EntityEldritchWarden(World p_i1745_1_) {
      super(p_i1745_1_);
      this.getNavigator().setBreakDoors(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new AILongRangeAttack(this, 3.0F, 1.0F, 20, 40, 24.0F));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityLivingBase.class, 1.1, false));
      this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.8));
      this.tasks.addTask(7, new EntityAIWander(this, 1.0F));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, Player.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, Player.class, 0, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCultist.class, 0, true));
      this.setSize(1.5F, 3.5F);
   }

   public void generateName() {
      int t = (int)this.getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue();
      if (t >= 0) {
         this.setCustomNameTag(String.format(Component.translatable("entity.Thaumcraft.EldritchWarden.name"), this.getTitle(), ChampionModifier.mods[t].getModNameLocalized()));
      }

   }

   private String getTitle() {
      return this.titles[this.getDataWatcher().getWatchableObjectByte(16)];
   }

   private void setTitle(int title) {
      this.dataWatcher.updateObject(16, (byte)title);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.33);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(10.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObject(16, (byte)0);
   }

   public void writeEntityToNBT(NBTTagCompound nbt) {
      super.writeEntityToNBT(nbt);
      nbt.setByte("title", this.getDataWatcher().getWatchableObjectByte(16));
   }

   public void readEntityFromNBT(NBTTagCompound nbt) {
      super.readEntityFromNBT(nbt);
      this.setTitle(nbt.getByte("title"));
   }

   public int getTotalArmorValue() {
      return super.getTotalArmorValue() + 4;
   }

   protected void updateAITasks() {
      if (this.fieldFrenzyCounter == 0) {
         super.updateAITasks();
      }

      if (this.hurtResistantTime <= 0 && this.ticksExisted % 25 == 0) {
         int bh = (int)(this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() * 0.66);
         if (this.getAbsorptionAmount() < (float)bh) {
            this.setAbsorptionAmount(this.getAbsorptionAmount() + 1.0F);
         }
      }

   }

   public void onUpdate() {
      if (this.getSpawnTimer() == 150) {
         this.level().setEntityState(this, (byte)18);
      }

      super.onUpdate();
      if ((Platform.getEnvironment() == Env.CLIENT)) {
         if (this.armLiftL > 0.0F) {
            this.armLiftL -= 0.05F;
         }

         if (this.armLiftR > 0.0F) {
            this.armLiftR -= 0.05F;
         }

         float x = (float)(this.posX + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F));
         float z = (float)(this.posZ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F));
         Thaumcraft.proxy.wispFXEG(this.level(), x, (float)(this.posY + (double)0.25F * (double)this.height), z, this);
         if (this.spawnTimer > 0) {
            float he = Math.max(1.0F, this.height * ((float)(150 - this.spawnTimer) / 150.0F));

            for(int a = 0; a < 33; ++a) {
               ClientFXUtils.smokeSpiral(this.level(), this.posX, this.boundingBox.minY + (double)(he / 2.0F), this.posZ, he, this.rand.nextInt(360), MathHelper.floor_double(this.boundingBox.minY) - 1, 2232623);
            }
         }
      }

   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ);

      for(int l = 0; l < 4; ++l) {
         i = MathHelper.floor_double(this.posX + (double)((float)(l % 2 * 2 - 1) * 0.25F));
         j = MathHelper.floor_double(this.posY);
         k = MathHelper.floor_double(this.posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
         if (this.level().isAirBlock(i, j, k)) {
            this.level().setBlock(i, j, k, ConfigBlocks.blockAiry, 11, 3);
         }
      }

      if (Platform.getEnvironment() != Env.CLIENT && this.fieldFrenzyCounter > 0) {
         if (this.fieldFrenzyCounter == 150) {
            this.teleportHome();
         }

         this.performFieldFrenzy();
      }

   }

   private void performFieldFrenzy() {
      if (this.fieldFrenzyCounter < 121 && this.fieldFrenzyCounter % 10 == 0) {
         this.level().setEntityState(this, (byte)17);
         double radius = (double)(150 - this.fieldFrenzyCounter) / (double)8.0F;
         int d = 1 + this.fieldFrenzyCounter / 8;
         int i = MathHelper.floor_double(this.posX);
         int j = MathHelper.floor_double(this.posY);
         int k = MathHelper.floor_double(this.posZ);

         for(int q = 0; q < 180 / d; ++q) {
            double radians = Math.toRadians(q * 2 * d);
            int deltaX = (int)(radius * Math.cos(radians));
            int deltaZ = (int)(radius * Math.sin(radians));
            if (this.level().isAirBlock(i + deltaX, j, k + deltaZ) && this.level().isBlockNormalCubeDefault(i + deltaX, j - 1, k + deltaZ, false)) {
               this.level().setBlock(i + deltaX, j, k + deltaZ, ConfigBlocks.blockAiry, 11, 3);
               this.level().scheduleBlockUpdate(i + deltaX, j, k + deltaZ, ConfigBlocks.blockAiry, 250 + this.rand.nextInt(150));
               if (this.rand.nextFloat() < 0.3F) {
                  PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockArcS2C(i + deltaX, j, k + deltaZ, this.getEntityId()), new NetworkRegistry.TargetPoint(this.level().dimension(), i + deltaX, j, k + deltaZ, 32.0F));
               } else {
                  PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkleS2C(i + deltaX, j, k + deltaZ, 8388736), new NetworkRegistry.TargetPoint(this.level().dimension(), i + deltaX, j, k + deltaZ, 32.0F));
               }
            }
         }

         this.level().playSoundEffect(this.posX, this.posY, this.posZ, "thaumcraft:zap", 1.0F, 0.9F + this.rand.nextFloat() * 0.1F);
      }

      --this.fieldFrenzyCounter;
   }

   protected void teleportHome() {
      EnderTeleportEvent event = new EnderTeleportEvent(this, this.getHomePosition().posX, this.getHomePosition().posY, this.getHomePosition().posZ, 0.0F);
      if (!MinecraftForge.EVENT_BUS.post(event)) {
         double d3 = this.posX;
         double d4 = this.posY;
         double d5 = this.posZ;
         this.posX = event.targetX;
         this.posY = event.targetY;
         this.posZ = event.targetZ;
         boolean flag = false;
         int i = MathHelper.floor_double(this.posX);
         int j = MathHelper.floor_double(this.posY);
         int k = MathHelper.floor_double(this.posZ);
         if (this.level().blockExists(i, j, k)) {
            boolean flag1 = false;
            int tries = 20;

            while(!flag1 && tries > 0) {
               Block block = this.level().getBlock(i, j - 1, k);
               Block block2 = this.level().getBlock(i, j, k);
               if (block.getMaterial().blocksMovement() && !block2.getMaterial().blocksMovement()) {
                  flag1 = true;
               } else {
                  i = MathHelper.floor_double(this.posX) + this.rand.nextInt(8) - this.rand.nextInt(8);
                  k = MathHelper.floor_double(this.posZ) + this.rand.nextInt(8) - this.rand.nextInt(8);
                  --tries;
               }
            }

            if (flag1) {
               this.setPosition((double)i + (double)0.5F, (double)j + 0.1, (double)k + (double)0.5F);
               if (this.level().getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
                  flag = true;
               }
            }
         }

         if (!flag) {
            this.setPosition(d3, d4, d5);
         } else {
            short short1 = 128;

            for(int l = 0; l < short1; ++l) {
               double d6 = (double)l / ((double)short1 - (double)1.0F);
               float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
               float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
               float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
               double d7 = d3 + (this.posX - d3) * d6 + (this.rand.nextDouble() - (double)0.5F) * (double)this.width * (double)2.0F;
               double d8 = d4 + (this.posY - d4) * d6 + this.rand.nextDouble() * (double)this.height;
               double d9 = d5 + (this.posZ - d5) * d6 + (this.rand.nextDouble() - (double)0.5F) * (double)this.width * (double)2.0F;
               this.level().spawnParticle("portal", d7, d8, d9, f, f1, f2);
            }

            this.level().playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
            this.playSound("mob.endermen.portal", 1.0F, 1.0F);
         }
      }
   }

   public boolean isEntityInvulnerable() {
      return this.fieldFrenzyCounter > 0 || super.isEntityInvulnerable();
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      if (!this.isEntityInvulnerable() && source != DamageSource.drown && source != DamageSource.wither) {
         boolean aef = super.attackEntityFrom(source, damage);
         if (Platform.getEnvironment() != Env.CLIENT && aef && !this.fieldFrenzy && this.getAbsorptionAmount() <= 0.0F) {
            this.fieldFrenzy = true;
            this.fieldFrenzyCounter = 150;
         }

         return aef;
      } else {
         return false;
      }
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
      this.spawnTimer = 150;
      this.setTitle(this.rand.nextInt(this.titles.length));
      this.setAbsorptionAmount((float)((double)this.getAbsorptionAmount() + this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() * 0.66));
      return super.onSpawnWithEgg(p_110161_1_);
   }

   public float getEyeHeight() {
      return 3.1F;
   }

   public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f) {
      if (this.rand.nextFloat() > 0.2F) {
         EntityEldritchOrb blast = new EntityEldritchOrb(this.level(), this);
         this.lastBlast = !this.lastBlast;
         this.level().setEntityState(this, (byte)(this.lastBlast ? 16 : 15));
         int rr = this.lastBlast ? 90 : 180;
         double xx = MathHelper.cos((this.rotationYaw + (float)rr) % 360.0F / 180.0F * (float)Math.PI) * 0.5F;
         double yy = 0.13;
         double zz = MathHelper.sin((this.rotationYaw + (float)rr) % 360.0F / 180.0F * (float)Math.PI) * 0.5F;
         blast.setPosition(blast.posX - xx, blast.posY - yy, blast.posZ - zz);
         double d0 = entitylivingbase.posX + entitylivingbase.motionX - this.posX;
         double d1 = entitylivingbase.posY - this.posY - (double)(entitylivingbase.height / 2.0F);
         double d2 = entitylivingbase.posZ + entitylivingbase.motionZ - this.posZ;
         blast.setThrowableHeading(d0, d1, d2, 1.0F, 2.0F);
         this.playSound("thaumcraft:egattack", 2.0F, 1.0F + this.rand.nextFloat() * 0.1F);
         this.level().spawnEntityInWorld(blast);
      } else if (this.canEntityBeSeen(entitylivingbase)) {
         PacketHandler.INSTANCE.sendToAllAround(new PacketFXSonic(this.getEntityId()), new NetworkRegistry.TargetPoint(this.level().dimension(), this.posX, this.posY, this.posZ, 32.0F));
         entitylivingbase.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * 1.5F, 0.1, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * 1.5F);

         try {
            entitylivingbase.addPotionEffect(new PotionEffect(Potion.wither.id, 400, 0));
            entitylivingbase.addPotionEffect(new PotionEffect(Potion.weakness.id, 400, 0));
         } catch (Exception ignored) {
         }

         if (entitylivingbase instanceof Player) {
            Thaumcraft.addWarpToPlayer((Player)entitylivingbase, 3 + this.level().rand.nextInt(3), true);
         }

         this.playSound("thaumcraft:egscreech", 4.0F, 1.0F + this.rand.nextFloat() * 0.1F);
      }

   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte p_70103_1_) {
      if (p_70103_1_ == 15) {
         this.armLiftL = 0.5F;
      } else if (p_70103_1_ == 16) {
         this.armLiftR = 0.5F;
      } else if (p_70103_1_ == 17) {
         this.armLiftL = 0.9F;
         this.armLiftR = 0.9F;
      } else if (p_70103_1_ == 18) {
         this.spawnTimer = 150;
      } else {
         super.handleHealthUpdate(p_70103_1_);
      }

   }

   protected void dropRareDrop(int fortune) {
      super.dropRareDrop(fortune);
   }

   public boolean canAttackClass(Class clazz) {
      return clazz != EntityEldritchGuardian.class && super.canAttackClass(clazz);
   }

   protected String getLivingSound() {
      return "thaumcraft:egidle";
   }

   protected String getDeathSound() {
      return "thaumcraft:egdeath";
   }

   public int getTalkInterval() {
      return 500;
   }
}
