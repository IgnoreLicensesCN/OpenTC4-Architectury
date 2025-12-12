package thaumcraft.common.entities.projectile;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.items.ThaumcraftItems;

public class EntityAlumentum extends ThrowableItemProjectile {
   public static EntityType<EntityAlumentum> entityAlumentumType;

   public EntityAlumentum(Level par1World){
      this(ThaumcraftEntities.ALUMENTUM,par1World);
   }
   public EntityAlumentum(EntityType<EntityAlumentum> type,Level par1World) {
      super(type,par1World);
   }
   public EntityAlumentum(LivingEntity par2EntityLiving, Level par1World){
      this(ThaumcraftEntities.ALUMENTUM,par2EntityLiving,par1World);
   }
   public EntityAlumentum(EntityType<EntityAlumentum> type, LivingEntity par2EntityLiving,Level par1World) {
      super(type,par2EntityLiving,par1World);
   }

   public EntityAlumentum(Level par1World, double par2, double par4, double par6) {
      this(ThaumcraftEntities.ALUMENTUM,par6, par2, par4, par1World);
   }
   public EntityAlumentum(EntityType<EntityAlumentum> type, double par2, double par4, double par6,Level par1World) {
      super(type,par6, par2, par4, par1World);
   }

   @Override
   public void shoot(double d, double e, double f, float g, float h) {
      super.shoot(d, e, f, g * 0.75F, h);
   }

   @Override
   protected @NotNull Item getDefaultItem() {
      return ThaumcraftItems.ALUMENTUM;
   }
   @Override
   public void tick() {
      super.tick();
      if (this.level() instanceof ClientLevel clientLevel) {
         for(int a = 0; a < 3; ++a) {
            ClientFXUtils.wispFX2(clientLevel, this.getX() + (double)((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F), this.getY() + (double)((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F), this.getZ() + (double)((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F), 0.3F, 5, true, true, 0.02F);
            double x2 = (this.getX() + this.xOld) / (double)2.0F + (double)((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F);
            double y2 = (this.getY() + this.yOld) / (double)2.0F + (double)((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F);
            double z2 = (this.getZ() + this.zOld) / (double)2.0F + (double)((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.3F);
            ClientFXUtils.wispFX2(clientLevel, x2, y2, z2, 0.3F, 5, true, true, 0.02F);
            ClientFXUtils.sparkle((float)this.getX() + (clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.1F, (float)this.getY() + (clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.1F, (float)this.getZ() + (clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 0.1F, 6);
         }
      }

   }

   @Override
   protected void onHit(HitResult hitResult) {
      if (this.level() instanceof ServerLevel level) {
         boolean mobGriefing = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
         level.explode(null, getX(), getY(), getZ(), 1.66F, mobGriefing ,Level.ExplosionInteraction.MOB);
         discard();
      }
   }

   public float getShadowSize() {
      return 0.1F;
   }

}
