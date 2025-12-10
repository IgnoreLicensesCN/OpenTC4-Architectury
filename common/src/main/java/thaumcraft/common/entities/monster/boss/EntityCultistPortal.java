package thaumcraft.common.entities.monster.boss;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.EntityCultist;
import thaumcraft.common.entities.monster.EntityCultistCleric;
import thaumcraft.common.entities.monster.EntityCultistKnight;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.world.WorldGenEldritchRing;
import thaumcraft.common.tiles.TileBanner;

import java.util.List;

public class EntityCultistPortal extends EntityMob implements IBossDisplayData {
   int stage = 0;
   int stagecounter = 200;
   public int pulse = 0;

   public EntityCultistPortal(Level par1World) {
      super(par1World);
      this.isImmuneToFire = true;
      this.experienceValue = 30;
      this.setSize(1.5F, 3.0F);
   }

   public int getTotalArmorValue() {
      return 5;
   }

   protected void entityInit() {
      super.entityInit();
   }

   public void writeEntityToNBT(NBTTagCompound nbt) {
      super.writeEntityToNBT(nbt);
      nbt.setInteger("stage", this.stage);
   }

   public void readEntityFromNBT(NBTTagCompound nbt) {
      super.readEntityFromNBT(nbt);
      this.stage = nbt.getInteger("stage");
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(500.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(0.0F);
      this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0F);
   }

   public float getShadowSize() {
      return 0.0F;
   }

   public boolean canBeCollidedWith() {
      return true;
   }

   public boolean canBePushed() {
      return false;
   }

   public void moveEntity(double par1, double par3, double par5) {
   }

   protected void updateEntityActionState() {
   }

   public boolean isInRangeToRenderDist(double par1) {
      return par1 < (double)4096.0F;
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   public void onUpdate() {
      super.onUpdate();
      if (Platform.getEnvironment() != Env.CLIENT) {
         if (this.stagecounter <= 0) {
            if (this.level().getClosestPlayerToEntity(this, 48.0F) != null) {
               this.level().setEntityState(this, (byte)16);
               switch (this.stage) {
                  case 0:
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                     this.stagecounter = 15 + this.rand.nextInt(10 - this.stage) - this.stage;
                     this.spawnMinions();
                     break;
                  case 12:
                     this.stagecounter = 50 + this.getTiming() * 2 + this.rand.nextInt(50);
                     this.spawnBoss();
                  default:
                     int t = this.getTiming();
                     this.stagecounter = t + this.rand.nextInt(5 + t / 3);
                     this.spawnMinions();
                     break;
               }

               ++this.stage;
            } else {
               this.stagecounter = 30 + this.rand.nextInt(30);
            }
         } else {
            --this.stagecounter;
            if (this.stagecounter == 160 && this.stage == 0) {
               this.level().setEntityState(this, (byte)16);

               for(int a = 2; a < 6; ++a) {
                  ForgeDirection dir = ForgeDirection.getOrientation(a);
                  this.level().setBlock((int)this.posX - dir.offsetX * 6, (int)this.posY, (int)this.posZ + dir.offsetZ * 6, ConfigBlocks.blockWoodenDevice, 8, 3);
                  TileEntity te = this.level().getTileEntity((int)this.posX - dir.offsetX * 6, (int)this.posY, (int)this.posZ + dir.offsetZ * 6);
                  if (te instanceof TileBanner) {

                     ((TileBanner)te).setFacing(WorldGenEldritchRing.bannerFaceFromDirection(a));
                     PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockArc((int)this.posX - dir.offsetX * 6, (int)this.posY, (int)this.posZ + dir.offsetZ * 6, this.getEntityId()), new NetworkRegistry.TargetPoint(this.level().dimension(), this.posX, this.posY, this.posZ, 32.0F));
                     this.playSound("thaumcraft:wandfail", 1.0F, 1.0F);
                  }
               }
            }

            if (this.stagecounter > 20 && this.stagecounter < 150 && this.stage == 0 && this.stagecounter % 13 == 0) {
               int a = (int)this.posX + this.rand.nextInt(5) - this.rand.nextInt(5);
               int b = (int)this.posZ + this.rand.nextInt(5) - this.rand.nextInt(5);
               if (a != (int)this.posX && b != (int)this.posZ && this.level().isAirBlock(a, (int)this.posY, b)) {
                  this.level().setEntityState(this, (byte)16);
                  float rr = this.level().rand.nextFloat();
                  int md = rr < 0.05F ? 2 : (rr < 0.2F ? 1 : 0);
                  this.level().setBlock(a, (int)this.posY, b, ConfigBlocks.blockLootCrate, md, 3);
                  PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockArc(a, (int)this.posY, b, this.getEntityId()), new NetworkRegistry.TargetPoint(this.level().dimension(), this.posX, this.posY, this.posZ, 32.0F));
                  this.playSound("thaumcraft:wandfail", 1.0F, 1.0F);
               }
            }
         }

         if (this.stage < 12) {
            this.heal(1.0F);
         }
      }

      if (this.pulse > 0) {
         --this.pulse;
      }

   }

   int getTiming() {
      List<Entity> l = EntityUtils.getEntitiesInRange(this.level(), this.posX, this.posY, this.posZ, this, EntityCultist.class, 32.0F);
      return l.size() * 20;
   }

   void spawnMinions() {
      EntityCultist cultist = null;
      if ((double)this.rand.nextFloat() > 0.33) {
         cultist = new EntityCultistKnight(this.level());
      } else {
         cultist = new EntityCultistCleric(this.level());
      }

      cultist.setPosition(this.posX + (double)this.rand.nextFloat() - (double)this.rand.nextFloat(), this.posY + (double)0.25F, this.posZ + (double)this.rand.nextFloat() - (double)this.rand.nextFloat());
      cultist.onSpawnWithEgg(null);
      cultist.spawnExplosionParticle();
      cultist.setHomeArea((int)this.posX, (int)this.posY, (int)this.posZ, 32);
      this.level().spawnEntityInWorld(cultist);
      cultist.playSound("thaumcraft:wandfail", 1.0F, 1.0F);
      if (this.stage > 12) {
         this.attackEntityFrom(DamageSource.outOfWorld, (float)(5 + this.rand.nextInt(5)));
      }

   }

   void spawnBoss() {
      EntityCultistLeader cultist = new EntityCultistLeader(this.level());
      cultist.setPosition(this.posX + (double)this.rand.nextFloat() - (double)this.rand.nextFloat(), this.posY + (double)0.25F, this.posZ + (double)this.rand.nextFloat() - (double)this.rand.nextFloat());
      cultist.onSpawnWithEgg(null);
      cultist.setHomeArea((int)this.posX, (int)this.posY, (int)this.posZ, 32);
      cultist.spawnExplosionParticle();
      this.level().spawnEntityInWorld(cultist);
      cultist.playSound("thaumcraft:wandfail", 1.0F, 1.0F);
   }

   public void onCollideWithPlayer(Player p) {
      if (this.getDistanceSqToEntity(p) < (double)3.0F && p.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this), 8.0F)) {
         this.playSound("thaumcraft:zap", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F + 1.0F);
      }

   }

   protected float getSoundVolume() {
      return 0.75F;
   }

   public int getTalkInterval() {
      return 540;
   }

   protected String getLivingSound() {
      return "thaumcraft:monolith";
   }

   protected String getHurtSound() {
      return "thaumcraft:zap";
   }

   protected String getDeathSound() {
      return "thaumcraft:shock";
   }

   protected Item getDropItem() {
       return super.getDropItem();
   }

   protected void dropFewItems(boolean flag, int fortune) {
      EntityUtils.entityDropSpecialItem(this, new ItemStack(ThaumcraftItems.PRIME_PEARL), this.height / 2.0F);
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte msg) {
      if (msg == 16) {
         this.pulse = 10;
         this.spawnExplosionParticle();
      } else {
         super.handleHealthUpdate(msg);
      }

   }

   public void addPotionEffect(PotionEffect p_70690_1_) {
   }

   protected void fall(float p_70069_1_) {
   }

   public void onDeath(DamageSource p_70645_1_) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         this.level().newExplosion(this, this.posX, this.posY, this.posZ, 2.0F, false, false);
      }

      super.onDeath(p_70645_1_);
   }
}
