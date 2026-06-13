package thaumcraft.common.lib.utils;

import com.linearity.opentc4.mixin.LivingEntityAccessor;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tc4tweak.ConfigurationHandler;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.entities.monster.mods.ChampionModifier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.*;

public class EntityUtils {

    @Deprecated(forRemoval = true,since = "got some misunderstanding")
    public static class AttributeModifierTweaked extends AttributeModifier {

        public AttributeModifierTweaked(UUID p_i1606_1_, String p_i1606_2_, double p_i1606_3_, Operation p_i1606_5_) {
            super(p_i1606_1_, p_i1606_2_, p_i1606_3_, p_i1606_5_);
        }

        @Override
        public double getAmount() {
            return ConfigurationHandler.INSTANCE.getChampionModValue(getId(), super.getAmount());
        }
    }

    public static final Set<EntityType<? extends LivingEntity>> livingEntityTypes = ConcurrentHashMap.newKeySet();

    public static void init() {
        Registry.ATTRIBUTES.register();
//        RegistrarManager manager = RegistrarManager.get(Thaumcraft.MOD_ID);
//        manager.builder(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":mobmod"), ThaumcraftAttributeInstances.CHAMPION_MOD)
//                .build();
//        manager.builder(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":championhealth"), ThaumcraftAttributeInstances.CHAMPION_HEALTH)
//                .build();
//        manager.builder(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":championdmg"), ThaumcraftAttributeInstances.CHAMPION_DAMAGE)
//                .build();
//        manager.builder(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":blodspeedboost"), ThaumcraftAttributeInstances.BOLDBUFF)
//                .build();
//        manager.builder(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":mightlydmgboost"), ThaumcraftAttributeInstances.MIGHTYBUFF)
//                .build();
//        int counter = 0;
//        for (AttributeModifierTweaked modifier : ThaumcraftAttributeInstances.HPBUFF) {
//            manager.builder(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":hpbuff_" + counter), modifier)
//                    .build();
//            counter += 1;
//        }
//        counter = 0;
//        for (AttributeModifierTweaked modifier : ThaumcraftAttributeInstances.DMGBUFF) {
//            manager.builder(ResourceLocation.tryParse(Thaumcraft.MOD_ID + ":dmgbuff_" + counter), modifier)
//                    .build();
//            counter += 1;
//        }
//        for (EntityType<? extends Entity> entityType: DefaultAttributesAccessor.opentc4$getSuppliers().keySet()) {
//            try {
//                Entity tryCreated = ((EntityTypeAccessor)entityType).opentc4$getFactory().create(entityType,null);
//                if (tryCreated instanceof LivingEntity) {
//                    var livingType = (EntityType<? extends LivingEntity>) entityType;
//                    platformUtils.registerEntityDefaultAttribute(livingType,CHAMPION_MOD);
//                    livingEntityTypes.add(livingType);
//                }
//            }catch (NullPointerException e){
//                LOGGER.error("EntityUtils init: ", e);
//            }
//        }
    }
    public static final UUID CHAMPION_HEALTH_MODIFIER_UUID = UUID.fromString("a62bef38-48cc-42a6-ac5e-ef913841c4fd");
    public static AttributeModifier getNewChampionHealthModifier(){
        return new AttributeModifier(CHAMPION_HEALTH_MODIFIER_UUID, "Champion health buff", 30.0F, ADDITION);
    }
    public static final UUID CHAMPION_DAMAGE_MODIFIER_UUID = UUID.fromString("a340d2db-d881-4c25-ac62-f0ad14cd63b0");
    public static AttributeModifier getNewChampionDamageModifier(){
        return new AttributeModifier(CHAMPION_DAMAGE_MODIFIER_UUID, "Champion damage buff", 2.0F, MULTIPLY_TOTAL);
    }
    public static final UUID BOLD_BUFF_MODIFIER_UUID = UUID.fromString("4b1edd33-caa9-47ae-a702-d86c05701037");
    public static AttributeModifier getNewBoldBuffModifier(){
        return new AttributeModifier(BOLD_BUFF_MODIFIER_UUID, "Bold speed boost", 0.3, MULTIPLY_BASE);
    }
    public static final UUID MIGHTY_BUFF_MODIFIER_UUID = UUID.fromString("7163897f-07f5-49b3-9ce4-b74beb83d2d3");
    public static AttributeModifier getNewMightyBuffModifier(){
        return new AttributeModifier(MIGHTY_BUFF_MODIFIER_UUID, "Mighty damage boost", 3.0F, MULTIPLY_TOTAL);
    }
    public static class ChampionModifierBaseValues {

        public static final double CHAMPION_MOD_BASE_VALUE_NOT_ATTACHED = -2.;
        public static final double CHAMPION_MOD_BASE_VALUE_ATTACHED_NOT_AFFECTED = -1.;
        public static final double CHAMPION_MOD_BASE_VALUE_ATTACHED_AFFECTED = 0.;
    }
    public static class ThaumcraftAttributeCategoryInstances {

        public static Attribute CHAMPION_MOD() {
            return Registry.SUPPLIER_CHAMPION_MOD.get();
        }

        public static Attribute STEP_HEIGHT_ADDITION_NOT_SNEAKING() {
            return Registry.SUPPLIER_STEP_HEIGHT_ADDITION_NOT_SNEAKING.get();
        }

        public static Attribute FLYING_SPEED_CONTROL_OVERRIDE() {
            return Registry.SUPPLIER_FLYING_SPEED_CONTROL_OVERRIDE.get();
        }

        public static Attribute HARNESS_FLYING_SPEED_ADD_PERCENT() {
            return Registry.SUPPLIER_HARNESS_FLYING_SPEED_ADD_PERCENT.get();
        }

        public static Attribute HARNESS_FUEL_DURATION_ADD_PERCENT() {
            return Registry.SUPPLIER_HARNESS_FUEL_DURATION_ADD_PERCENT.get();
        }
//        public static final Attribute FORWARD_IMPULSE_NOT_IN_WATER = Registry.SUPPLIER_FORWARD_IMPULSE_NOT_IN_WATER.get();
//        public static final Attribute FORWARD_IMPULSE_IN_WATER = Registry.SUPPLIER_FORWARD_IMPULSE_IN_WATER.get();
//        public static final AttributeModifierTweaked CHAMPION_HEALTH = new AttributeModifierTweaked(
//                UUID.fromString("a62bef38-48cc-42a6-ac5e-ef913841c4fd"), "Champion health buff", 30.0F, ADDITION);
//        public static final AttributeModifierTweaked CHAMPION_DAMAGE = new AttributeModifierTweaked(
//                UUID.fromString("a340d2db-d881-4c25-ac62-f0ad14cd63b0"), "Champion damage buff", 2.0F, MULTIPLY_TOTAL);
//        public static final AttributeModifierTweaked BOLDBUFF = new AttributeModifierTweaked(
//                UUID.fromString("4b1edd33-caa9-47ae-a702-d86c05701037"), "Bold speed boost", 0.3, MULTIPLY_BASE);
//        public static final AttributeModifierTweaked MIGHTYBUFF = new AttributeModifierTweaked(
//                UUID.fromString("7163897f-07f5-49b3-9ce4-b74beb83d2d3"), "Mighty damage boost", 3.0F, MULTIPLY_TOTAL);
//        public static final AttributeModifierTweaked[] HPBUFF = new AttributeModifierTweaked[]{
//                new AttributeModifierTweaked(
//                        UUID.fromString("54d621c1-dd4d-4b43-8bd2-5531c8875797"), "HEALTH BUFF 1", 50.0F, ADDITION),
//                new AttributeModifierTweaked(
//                        UUID.fromString("f51257dc-b7fa-4f7a-92d7-75d68e8592c4"), "HEALTH BUFF 2", 50.0F, ADDITION),
//                new AttributeModifierTweaked(
//                        UUID.fromString("3d6b2e42-4141-4364-b76d-0e8664bbd0bb"), "HEALTH BUFF 3", 50.0F, ADDITION),
//                new AttributeModifierTweaked(
//                        UUID.fromString("02c97a08-801c-4131-afa2-1427a6151934"), "HEALTH BUFF 4", 50.0F, ADDITION),
//                new AttributeModifierTweaked(
//                        UUID.fromString("0f354f6a-33c5-40be-93be-81b1338567f1"), "HEALTH BUFF 5", 50.0F, ADDITION)};
//        public static final AttributeModifierTweaked[] DMGBUFF = new AttributeModifierTweaked[]{
//                new AttributeModifierTweaked(
//                        UUID.fromString("534f8c57-929a-48cf-bbd6-0fd851030748"), "DAMAGE BUFF 1", 0.5F, ADDITION),
//                new AttributeModifierTweaked(
//                        UUID.fromString("d317a76e-0e7c-4c61-acfd-9fa286053b32"), "DAMAGE BUFF 2", 0.5F, ADDITION),
//                new AttributeModifierTweaked(
//                        UUID.fromString("ff462d63-26a2-4363-830e-143ed97e2a4f"), "DAMAGE BUFF 3", 0.5F, ADDITION),
//                new AttributeModifierTweaked(
//                        UUID.fromString("cf1eb39e-0c67-495f-887c-0d3080828d2f"), "DAMAGE BUFF 4", 0.5F, ADDITION),
//                new AttributeModifierTweaked(
//                        UUID.fromString("3cfab9da-2701-43d8-ac07-885f16fa4117"), "DAMAGE BUFF 5", 0.5F, ADDITION)};
    }
    public static class Registry {
        public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.ATTRIBUTE);
        public static final RegistrySupplier<Attribute> SUPPLIER_CHAMPION_MOD = ATTRIBUTES.register(
                "champion_modifier_applied_state",
                () -> new RangedAttribute("tc.mobmod", ChampionModifierBaseValues.CHAMPION_MOD_BASE_VALUE_NOT_ATTACHED, ChampionModifierBaseValues.CHAMPION_MOD_BASE_VALUE_NOT_ATTACHED, 100.0F).setSyncable(true)
        );
        public static final RegistrySupplier<Attribute> SUPPLIER_STEP_HEIGHT_ADDITION_NOT_SNEAKING = ATTRIBUTES.register(
                "attributes." + Thaumcraft.MOD_ID + ".step_height_addition_not_sneaking",
                () -> new RangedAttribute("attributes." + Thaumcraft.MOD_ID + ".step_height_addition_not_sneaking", 0, 0, 100.0F).setSyncable(true)
        );
        public static final RegistrySupplier<Attribute> SUPPLIER_FLYING_SPEED_CONTROL_OVERRIDE = ATTRIBUTES.register(
                "attributes." + Thaumcraft.MOD_ID + ".flying_speed_control_override",
                () -> new RangedAttribute("attributes." + Thaumcraft.MOD_ID + ".flying_speed_control_override", 0, 0, 100.0F).setSyncable(true)
        );
        public static final RegistrySupplier<Attribute> SUPPLIER_HARNESS_FLYING_SPEED_ADD_PERCENT = ATTRIBUTES.register(
                "attributes." + Thaumcraft.MOD_ID + ".harness_flying_speed_add_percent",
                () -> new RangedAttribute("attributes." + Thaumcraft.MOD_ID + ".harness_flying_speed_add_percent", 0, -100, 100.0F).setSyncable(true)
        );
        public static final RegistrySupplier<Attribute> SUPPLIER_HARNESS_FUEL_DURATION_ADD_PERCENT = ATTRIBUTES.register(
                "attributes." + Thaumcraft.MOD_ID + ".harness_fuel_duration_add_percent",
                () -> new RangedAttribute("attributes." + Thaumcraft.MOD_ID + ".harness_fuel_duration_add_percent", 0, -100, 100.0F).setSyncable(true)
        );
//        public static final RegistrySupplier<Attribute> SUPPLIER_FORWARD_IMPULSE_NOT_IN_WATER = ATTRIBUTES.register(
//                "attributes." + Thaumcraft.MOD_ID + "." + "forward_impulse_not_in_water",
//                () -> new RangedAttribute("attributes." + Thaumcraft.MOD_ID + "." + "forward_impulse_not_in_water",0,0,100).setSyncable(true)
//        );
//        public static final RegistrySupplier<Attribute> SUPPLIER_FORWARD_IMPULSE_IN_WATER = ATTRIBUTES.register(
//                "attributes." + Thaumcraft.MOD_ID + "." + "forward_impulse_in_water",
//                () -> new RangedAttribute("attributes." + Thaumcraft.MOD_ID + "." + "forward_impulse_in_water",0,0,100).setSyncable(true)
//        );
    }

    // 简单重载：只给 range
    public static Entity getPointedEntity(Entity entity, double range) {
        return getPointedEntity(entity, 0, range, 1.1, null, false);
    }

    // 重载：range + padding
    public static Entity getPointedEntity(Entity entity, double range, double padding) {
        return getPointedEntity(entity, 0, range, padding, null, false);
    }

    // 重载：range + 排除类型
    public static Entity getPointedEntity(Entity entity, double range, Class<?> excludeClass) {
        return getPointedEntity(entity, 0, range, 1.1, excludeClass, false);
    }

    public static Entity getPointedEntity(Entity entity, double minrange, double range, float padding, boolean nonCollide) {
        return getPointedEntity(entity, minrange, range, padding, Entity.class, nonCollide);
    }

    // 核心方法
    public static Entity getPointedEntity(
            Entity entityIn, double minRange, double maxRange, double padding,
            Class<?> excludeClass, boolean nonCollide
    ) {
        Level world = entityIn.level();
        Vec3 eyePos = entityIn.getEyePosition(1.0F);
        Vec3 lookVec = entityIn.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(lookVec.scale(maxRange));

        Entity pointedEntity = null;
        double closestDistance = maxRange;

        // 搜索盒子
        AABB searchBox = entityIn.getBoundingBox()
                .expandTowards(lookVec.scale(maxRange))
                .inflate(padding);

        List<Entity> entities = world.getEntities(
                entityIn, searchBox, e ->
                        (nonCollide || e.isPickable()) && (excludeClass == null || !excludeClass.isInstance(e))
        );

        for (Entity entity : entities) {
            Optional<Vec3> hitVecOpt = entity.getBoundingBox()
                    .inflate(Math.max(0.8, entity.getBbWidth() * 0.5))
                    .clip(eyePos, endPos);

            if (entity.getBoundingBox()
                    .contains(eyePos)) {
                pointedEntity = entity;
                closestDistance = 0;
                break;
            }

            if (hitVecOpt.isPresent()) {
                double distance = eyePos.distanceTo(hitVecOpt.get());
                if (distance < closestDistance && distance >= minRange) {
                    pointedEntity = entity;
                    closestDistance = distance;
                }
            }
        }

        return pointedEntity;
    }

    public static boolean canEntityBeSeen(Entity entity, net.minecraft.world.level.block.entity.BlockEntity te) {
        Vec3 start = new Vec3(
                te.getBlockPos()
                        .getX() + 0.5, te.getBlockPos()
                .getY() + 1.25, te.getBlockPos()
                .getZ() + 0.5
        );
        Vec3 end = entity.getEyePosition(1.0F); // 从实体眼睛位置射线
        return isLineUnobstructed(entity, entity.level(), start, end);
    }

    // 从指定坐标看实体
    public static boolean canEntityBeSeen(Entity entity, double x, double y, double z) {
        Vec3 start = new Vec3(x, y, z);
        Vec3 end = entity.getEyePosition(1.0F);
        return isLineUnobstructed(entity, entity.level(), start, end);
    }

    // 实体对实体
    public static boolean canEntityBeSeen(Entity from, Entity to) {
        Vec3 start = from.getEyePosition(1.0F);
        Vec3 end = to.getEyePosition(1.0F);
        return isLineUnobstructed(from, from.level(), start, end);
    }

    // 核心方法：射线是否被方块阻挡
    private static boolean isLineUnobstructed(Entity startEntity, Level world, Vec3 start, Vec3 end) {
        HitResult hit = world.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER, // 射线碰撞方块
                ClipContext.Fluid.NONE,
                startEntity
        ));
        return hit.getType() == HitResult.Type.MISS;
    }

    public static void setRecentlyHit(LivingEntity ent, int hit) {
        ((LivingEntityAccessor) ent).opentc4$setLastHurtByPlayerTime(hit);
    }

    public static int getRecentlyHit(LivingEntity ent) {
        return ((LivingEntityAccessor) ent).opentc4$getLastHurtByPlayerTime();
    }

    public static HitResult getHitResultFromPlayer(Level par1World, Player par2Player, boolean hitFluid) {
        double reach = 5.0; // 默认射程
        //TODO:get one

        // 如果是服务端玩家，从游戏模式获取射程
//        if (par2Player instanceof ServerPlayer sp) {
//            MinecraftClient
//            reach = sp.getAttributeValue(Attribute);
//            reach = sp.gameMode.getReachDistance();
//        }

        // 射线起点 = 玩家眼睛位置
        Vec3 start = par2Player.getEyePosition(1.0F);
        // 射线方向 = 玩家视线
        Vec3 look = par2Player.getLookAngle();
        // 射线终点
        Vec3 end = start.add(look.scale(reach));

        // ClipContext 参数：
        // Block.COLLIDER = 方块碰撞盒射线
        // Fluid.ANY 或 NONE = 是否碰流体
        ClipContext.Block blockMode = ClipContext.Block.COLLIDER;
        ClipContext.Fluid fluidMode = hitFluid ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE;

        return par1World.clip(new ClipContext(start, end, blockMode, fluidMode, par2Player));
    }
//   public static HitResult getHitResultFromPlayer(Level par1World, Player par2Player, boolean par3) {
//      float f = 1.0F;
//      float f1 = par2Player.prevRotationPitch + (par2Player.rotationPitch - par2Player.prevRotationPitch) * f;
//      float f2 = par2Player.prevRotationYaw + (par2Player.rotationYaw - par2Player.prevRotationYaw) * f;
//      double d0 = par2Player.prevPosX + (par2Player.posX - par2Player.prevPosX) * (double)f;
//      double d1 = par2Player.prevPosY + (par2Player.posY - par2Player.prevPosY) * (double)f + (double)(par1World.isClientSide() ? par2Player.getEyeHeight() - par2Player.getDefaultEyeHeight() : par2Player.getEyeHeight());
//      double d2 = par2Player.prevPosZ + (par2Player.posZ - par2Player.prevPosZ) * (double)f;
//      Vec3 vec3 = new Vec3(d0, d1, d2);
//      float f3 = MathHelper.cos(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
//      float f4 = MathHelper.sin(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
//      float f5 = -MathHelper.cos(-f1 * ((float)Math.PI / 180F));
//      float f6 = MathHelper.sin(-f1 * ((float)Math.PI / 180F));
//      float f7 = f4 * f5;
//      float f8 = f3 * f5;
//      double d3 = 5.0F;
//      if (par2Player instanceof ServerPlayer) {
//         d3 = ((ServerPlayer)par2Player).theItemInWorldManager.getBlockReachDistance();
//      }
//
//      Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
//      return par1World.func_147447_a(vec3, vec31, par3, !par3, false);
//   }

    public static <T extends Entity> List<T> getEntitiesInRange(Level world, double x, double y, double z, Entity self, Class<T> clazz, double range) {
        AABB box = new AABB(x - range, y - range, z - range, x + range, y + range, z + range);
        return world.getEntitiesOfClass(clazz, box, e -> e != self);
//      ArrayList<Entity> out = new ArrayList<>();
//      List<Entity> list = (List<Entity>)world.getEntitiesWithinAABB(clazz, AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(range, range, range));
//      if (!list.isEmpty()) {
//         for(Entity e : list) {
//             if (entity == null || entity.getEntityId() != e.getEntityId()) {
//               out.add(e);
//            }
//         }
//      }
//
//      return out;
    }

    public static boolean isVisibleTo(float fov, Entity ent, Entity ent2, float range) {
        return isVisibleToInternal(
                fov, ent,
                ent2.getX(),
                ent2.getBoundingBox().minY + ent2.getEyeHeight() / 2.0,
                ent2.getZ(),
                range
        );
    }

    public static boolean isVisibleTo(float fov, Entity ent, double xx, double yy, double zz, float range) {
        return isVisibleToInternal(fov, ent, xx, yy, zz, range);
    }

    // 私有统一方法
    private static boolean isVisibleToInternal(float fov, Entity ent, double tx, double ty, double tz, float range) {
        // 玩家视线向量
        Vec3 look = ent.getLookAngle();
        Vec3 start = new Vec3(ent.getX(), ent.getBoundingBox().minY + ent.getEyeHeight(), ent.getZ());
        Vec3 end = start.add(look.x * range, look.y * range, look.z * range);

        // 目标位置
        double[] target = new double[]{tx, ty, tz};
        double[] origin = new double[]{start.x, start.y, start.z};
        double[] lookPos = new double[]{end.x, end.y, end.z};

        // 调用你的 Utils.isLyingInCone
        return Utils.isLyingInCone(target, origin, lookPos, fov);
    }

    //TODO:Migrate to entity drop item logic(whatever it will lead to,always floating or anything else)
    public static ItemEntity entityDropSpecialItem(Entity entity, ItemStack stack, float dropheight) {
        if (stack.getCount() != 0 && !stack.isEmpty()) {
            EntitySpecialItem entityitem = new EntitySpecialItem(
                    entity.level(), entity.getX(), entity.getY() + (double) dropheight, entity.getZ(), stack);
            entityitem.delayBeforeCanPickup = 10;
            entityitem.motionY = 0.1F;
            entityitem.motionX = 0.0F;
            entityitem.motionZ = 0.0F;
            if (entity.captureDrops) {
                entity.capturedDrops.add(entityitem);
            } else {
                entity.level().addFreshEntity(entityitem);
            }

            return entityitem;
        } else {
            return null;
        }
    }

    public static void makeChampion(LivingEntity entity, boolean persist) {
        int type = 0;
        if (!(entity instanceof Creeper)) {
            type = entity.getRandom()
                    .nextInt(ChampionModifier.mods.length);
        }

        AttributeInstance modai = entity.getAttribute(ThaumcraftAttributeCategoryInstances.CHAMPION_MOD());
        if (modai == null) {
            return;
        }
        modai.setBaseValue(ChampionModifierBaseValues.CHAMPION_MOD_BASE_VALUE_ATTACHED_AFFECTED);
//      modai.removeModifier(ChampionModifier.mods[type].attributeMod);
//      modai.applyModifier(ChampionModifier.mods[type].attributeMod);
        if (!(entity instanceof EntityThaumcraftBoss)) {

            AttributeInstance iattributeinstance = entity.getAttribute(Attributes.MAX_HEALTH);
            assert iattributeinstance != null;
            iattributeinstance.removeModifier(CHAMPION_HEALTH_MODIFIER_UUID);
            iattributeinstance.addPermanentModifier(getNewChampionHealthModifier());

            AttributeInstance iattributeinstance2 = entity.getAttribute(Attributes.ATTACK_DAMAGE);
            assert iattributeinstance2 != null;
            iattributeinstance2.removeModifier(CHAMPION_DAMAGE_MODIFIER_UUID);
            iattributeinstance2.addPermanentModifier(getNewChampionDamageModifier());
            entity.heal(25.0F);
            entity.setCustomName(Component.literal(
                    ChampionModifier.mods[type].getModNameLocalized() + " " + entity.getName()
                            .getString()));
        } else {
            ((EntityThaumcraftBoss) entity).generateName();
        }

        if (persist && entity instanceof Mob mob) {
            mob.setPersistenceRequired();
//         entity.func_110163_bv();
        }

        switch (type) {
            case 0:
                AttributeInstance sai = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                if (sai == null) {
                    return;
                }
                sai.removeModifier(BOLD_BUFF_MODIFIER_UUID);
                sai.addPermanentModifier(getNewBoldBuffModifier());
                break;
            case 3:
                AttributeInstance mai = entity.getAttribute(Attributes.ATTACK_DAMAGE);
                if (mai == null) {
                    return;
                }
                mai.removeModifier(MIGHTY_BUFF_MODIFIER_UUID);
                mai.addPermanentModifier(getNewMightyBuffModifier());
                break;
            case 5:
                AttributeInstance attrInstance = entity.getAttribute(Attributes.MAX_HEALTH);
                if (attrInstance == null) {
                    return;
                }
                int bh = (int) attrInstance.getBaseValue() / 2;
                entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float) bh);
        }

    }
}
