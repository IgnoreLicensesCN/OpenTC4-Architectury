package thaumcraft.common.lib.utils;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.Player;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tc4tweak.ConfigurationHandler;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.entities.monster.mods.ChampionModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityUtils {


    public static class AttributeModifierTweaked extends AttributeModifier{

        public AttributeModifierTweaked(UUID p_i1606_1_, String p_i1606_2_, double p_i1606_3_, int p_i1606_5_) {
            super(p_i1606_1_, p_i1606_2_, p_i1606_3_, p_i1606_5_);
        }
        @Override
        public double getAmount() {
            return ConfigurationHandler.INSTANCE.getChampionModValue(getID(), super.getAmount());
        }
    }

   public static final IAttribute CHAMPION_MOD = (new RangedAttribute("tc.mobmod", -2.0F, -2.0F, 100.0F)).setDescription("Champion modifier").setShouldWatch(true);
   public static final AttributeModifierTweaked CHAMPION_HEALTH = new AttributeModifierTweaked(UUID.fromString("a62bef38-48cc-42a6-ac5e-ef913841c4fd"), "Champion health buff", 30.0F, 0);
   public static final AttributeModifierTweaked CHAMPION_DAMAGE = new AttributeModifierTweaked(UUID.fromString("a340d2db-d881-4c25-ac62-f0ad14cd63b0"), "Champion damage buff", 2.0F, 2);
   public static final AttributeModifierTweaked BOLDBUFF = new AttributeModifierTweaked(UUID.fromString("4b1edd33-caa9-47ae-a702-d86c05701037"), "Bold speed boost", 0.3, 1);
   public static final AttributeModifierTweaked MIGHTYBUFF = new AttributeModifierTweaked(UUID.fromString("7163897f-07f5-49b3-9ce4-b74beb83d2d3"), "Mighty damage boost", 3.0F, 2);
   public static final AttributeModifierTweaked[] HPBUFF = new AttributeModifierTweaked[]{
           new AttributeModifierTweaked(UUID.fromString("54d621c1-dd4d-4b43-8bd2-5531c8875797"), "HEALTH BUFF 1", 50.0F, 0),
           new AttributeModifierTweaked(UUID.fromString("f51257dc-b7fa-4f7a-92d7-75d68e8592c4"), "HEALTH BUFF 2", 50.0F, 0),
           new AttributeModifierTweaked(UUID.fromString("3d6b2e42-4141-4364-b76d-0e8664bbd0bb"), "HEALTH BUFF 3", 50.0F, 0),
           new AttributeModifierTweaked(UUID.fromString("02c97a08-801c-4131-afa2-1427a6151934"), "HEALTH BUFF 4", 50.0F, 0),
           new AttributeModifierTweaked(UUID.fromString("0f354f6a-33c5-40be-93be-81b1338567f1"), "HEALTH BUFF 5", 50.0F, 0)};
   public static final AttributeModifierTweaked[] DMGBUFF = new AttributeModifierTweaked[]{
           new AttributeModifierTweaked(UUID.fromString("534f8c57-929a-48cf-bbd6-0fd851030748"), "DAMAGE BUFF 1", 0.5F, 0),
           new AttributeModifierTweaked(UUID.fromString("d317a76e-0e7c-4c61-acfd-9fa286053b32"), "DAMAGE BUFF 2", 0.5F, 0),
           new AttributeModifierTweaked(UUID.fromString("ff462d63-26a2-4363-830e-143ed97e2a4f"), "DAMAGE BUFF 3", 0.5F, 0),
           new AttributeModifierTweaked(UUID.fromString("cf1eb39e-0c67-495f-887c-0d3080828d2f"), "DAMAGE BUFF 4", 0.5F, 0),
           new AttributeModifierTweaked(UUID.fromString("3cfab9da-2701-43d8-ac07-885f16fa4117"), "DAMAGE BUFF 5", 0.5F, 0)};

   public static Entity getPointedEntity(World world, Entity Player, double minrange, double range, float padding) {
      return getPointedEntity(world, Player, minrange, range, padding, false);
   }

   public static Entity getPointedEntity(World world, Entity Player, double minrange, double range, float padding, boolean nonCollide) {
      Entity pointedEntity = null;
      Vec3 vec3d = Vec3.createVectorHelper(Player.posX, Player.posY + (double)Player.getEyeHeight(), Player.posZ);
      Vec3 vec3d1 = Player.getLookVec();
      Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * range, vec3d1.yCoord * range, vec3d1.zCoord * range);
      List list = world.getEntitiesWithinAABBExcludingEntity(Player, Player.boundingBox.addCoord(vec3d1.xCoord * range, vec3d1.yCoord * range, vec3d1.zCoord * range).expand(padding, padding, padding));
      double d2 = 0.0F;

       for (Object o : list) {
           Entity entity = (Entity) o;
           if (!((double) entity.getDistanceToEntity(Player) < minrange) && (entity.canBeCollidedWith() || nonCollide) && world.func_147447_a(Vec3.createVectorHelper(Player.posX, Player.posY + (double) Player.getEyeHeight(), Player.posZ), Vec3.createVectorHelper(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ), false, true, false) == null) {
               float f2 = Math.max(0.8F, entity.getCollisionBorderSize());
               AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
               MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
               if (axisalignedbb.isVecInside(vec3d)) {
                   if ((double) 0.0F < d2 || d2 == (double) 0.0F) {
                       pointedEntity = entity;
                       d2 = 0.0F;
                   }
               } else if (movingobjectposition != null) {
                   double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
                   if (d3 < d2 || d2 == (double) 0.0F) {
                       pointedEntity = entity;
                       d2 = d3;
                   }
               }
           }
       }

      return pointedEntity;
   }

   public static Entity getPointedEntity(World world, Player Player, double range, Class clazz) {
      Entity pointedEntity = null;
      Vec3 vec3d = Vec3.createVectorHelper(Player.posX, Player.posY + (double)Player.getEyeHeight(), Player.posZ);
      Vec3 vec3d1 = Player.getLookVec();
      Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * range, vec3d1.yCoord * range, vec3d1.zCoord * range);
      float f1 = 1.1F;
      List list = world.getEntitiesWithinAABBExcludingEntity(Player, Player.boundingBox.addCoord(vec3d1.xCoord * range, vec3d1.yCoord * range, vec3d1.zCoord * range).expand(f1, f1, f1));
      double d2 = 0.0F;

       for (Object o : list) {
           Entity entity = (Entity) o;
           if (entity.canBeCollidedWith() && world.func_147447_a(Vec3.createVectorHelper(Player.posX, Player.posY + (double) Player.getEyeHeight(), Player.posZ), Vec3.createVectorHelper(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ), false, true, false) == null && !clazz.isInstance(entity)) {
               float f2 = Math.max(0.8F, entity.getCollisionBorderSize());
               AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
               MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
               if (axisalignedbb.isVecInside(vec3d)) {
                   if ((double) 0.0F < d2 || d2 == (double) 0.0F) {
                       pointedEntity = entity;
                       d2 = 0.0F;
                   }
               } else if (movingobjectposition != null) {
                   double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
                   if (d3 < d2 || d2 == (double) 0.0F) {
                       pointedEntity = entity;
                       d2 = d3;
                   }
               }
           }
       }

      return pointedEntity;
   }

   public static boolean canEntityBeSeen(Entity entity, TileEntity te) {
      return te.getWorldObj().rayTraceBlocks(Vec3.createVectorHelper((double)te.xCoord + (double)0.5F, (double)te.yCoord + (double)1.25F, (double)te.zCoord + (double)0.5F), Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ), false) == null;
   }

   public static boolean canEntityBeSeen(Entity entity, double x, double y, double z) {
      return entity.worldObj.rayTraceBlocks(Vec3.createVectorHelper(x, y, z), Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ), false) == null;
   }

   public static boolean canEntityBeSeen(Entity entity, Entity entity2) {
      return entity.worldObj.rayTraceBlocks(Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ), Vec3.createVectorHelper(entity2.posX, entity2.posY, entity2.posZ), false) == null;
   }

   public static void setRecentlyHit(EntityLivingBase ent, int hit) {
      try {
         ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, ent, hit, "recentlyHit", "field_70718_bc");
      } catch (Exception ignored) {
      }

   }

   public static int getRecentlyHit(EntityLivingBase ent) {
      try {
         return ReflectionHelper.getPrivateValue(EntityLivingBase.class, ent, new String[]{"recentlyHit", "field_70718_bc"});
      } catch (Exception var2) {
         return 0;
      }
   }

   public static MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, Player par2Player, boolean par3) {
      float f = 1.0F;
      float f1 = par2Player.prevRotationPitch + (par2Player.rotationPitch - par2Player.prevRotationPitch) * f;
      float f2 = par2Player.prevRotationYaw + (par2Player.rotationYaw - par2Player.prevRotationYaw) * f;
      double d0 = par2Player.prevPosX + (par2Player.posX - par2Player.prevPosX) * (double)f;
      double d1 = par2Player.prevPosY + (par2Player.posY - par2Player.prevPosY) * (double)f + (double)(par1World.isRemote ? par2Player.getEyeHeight() - par2Player.getDefaultEyeHeight() : par2Player.getEyeHeight());
      double d2 = par2Player.prevPosZ + (par2Player.posZ - par2Player.prevPosZ) * (double)f;
      Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
      float f3 = MathHelper.cos(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f4 = MathHelper.sin(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f5 = -MathHelper.cos(-f1 * ((float)Math.PI / 180F));
      float f6 = MathHelper.sin(-f1 * ((float)Math.PI / 180F));
      float f7 = f4 * f5;
      float f8 = f3 * f5;
      double d3 = 5.0F;
      if (par2Player instanceof ServerPlayer) {
         d3 = ((ServerPlayer)par2Player).theItemInWorldManager.getBlockReachDistance();
      }

      Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
      return par1World.func_147447_a(vec3, vec31, par3, !par3, false);
   }

   public static ArrayList<Entity> getEntitiesInRange(World world, double x, double y, double z, Entity entity, Class<?> clazz, double range) {
      ArrayList<Entity> out = new ArrayList<>();
      List<Entity> list = (List<Entity>)world.getEntitiesWithinAABB(clazz, AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(range, range, range));
      if (!list.isEmpty()) {
         for(Entity e : list) {
             if (entity == null || entity.getEntityId() != e.getEntityId()) {
               out.add(e);
            }
         }
      }

      return out;
   }

   public static boolean isVisibleTo(float fov, Entity ent, Entity ent2, float range) {
      double[] x = new double[]{ent2.posX, ent2.boundingBox.minY + (double)(ent2.height / 2.0F), ent2.posZ};
      double[] t = new double[]{ent.posX, ent.boundingBox.minY + (double)ent.getEyeHeight(), ent.posZ};
      Vec3 q = ent.getLookVec();
      q.xCoord *= range;
      q.yCoord *= range;
      q.zCoord *= range;
      Vec3 l = q.addVector(ent.posX, ent.boundingBox.minY + (double)ent.getEyeHeight(), ent.posZ);
      double[] b = new double[]{l.xCoord, l.yCoord, l.zCoord};
      return Utils.isLyingInCone(x, t, b, fov);
   }

   public static boolean isVisibleTo(float fov, Entity ent, double xx, double yy, double zz, float range) {
      double[] x = new double[]{xx, yy, zz};
      double[] t = new double[]{ent.posX, ent.boundingBox.minY + (double)ent.getEyeHeight(), ent.posZ};
      Vec3 q = ent.getLookVec();
      q.xCoord *= range;
      q.yCoord *= range;
      q.zCoord *= range;
      Vec3 l = q.addVector(ent.posX, ent.boundingBox.minY + (double)ent.getEyeHeight(), ent.posZ);
      double[] b = new double[]{l.xCoord, l.yCoord, l.zCoord};
      return Utils.isLyingInCone(x, t, b, fov);
   }

   public static EntityItem entityDropSpecialItem(Entity entity, ItemStack stack, float dropheight) {
      if (stack.stackSize != 0 && stack.getItem() != null) {
         EntitySpecialItem entityitem = new EntitySpecialItem(entity.worldObj, entity.posX, entity.posY + (double)dropheight, entity.posZ, stack);
         entityitem.delayBeforeCanPickup = 10;
         entityitem.motionY = 0.1F;
         entityitem.motionX = 0.0F;
         entityitem.motionZ = 0.0F;
         if (entity.captureDrops) {
            entity.capturedDrops.add(entityitem);
         } else {
            entity.worldObj.spawnEntityInWorld(entityitem);
         }

         return entityitem;
      } else {
         return null;
      }
   }

   public static void makeChampion(EntityMob entity, boolean persist) {
      int type = entity.worldObj.rand.nextInt(ChampionModifier.mods.length);
      if (entity instanceof EntityCreeper) {
         type = 0;
      }

      IAttributeInstance modai = entity.getEntityAttribute(CHAMPION_MOD);
      modai.removeModifier(ChampionModifier.mods[type].attributeMod);
      modai.applyModifier(ChampionModifier.mods[type].attributeMod);
      if (!(entity instanceof EntityThaumcraftBoss)) {
         IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.maxHealth);
         iattributeinstance.removeModifier(CHAMPION_HEALTH);
         iattributeinstance.applyModifier(CHAMPION_HEALTH);
         IAttributeInstance iattributeinstance2 = entity.getEntityAttribute(SharedMonsterAttributes.attackDamage);
         iattributeinstance2.removeModifier(CHAMPION_DAMAGE);
         iattributeinstance2.applyModifier(CHAMPION_DAMAGE);
         entity.heal(25.0F);
         entity.setCustomNameTag(ChampionModifier.mods[type].getModNameLocalized() + " " + entity.getCommandSenderName());
      } else {
         ((EntityThaumcraftBoss)entity).generateName();
      }

      if (persist) {
         entity.func_110163_bv();
      }

      switch (type) {
         case 0:
            IAttributeInstance sai = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            sai.removeModifier(BOLDBUFF);
            sai.applyModifier(BOLDBUFF);
            break;
         case 3:
            IAttributeInstance mai = entity.getEntityAttribute(SharedMonsterAttributes.attackDamage);
            mai.removeModifier(MIGHTYBUFF);
            mai.applyModifier(MIGHTYBUFF);
            break;
         case 5:
            int bh = (int)entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() / 2;
            entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float)bh);
      }

   }
}
