package thaumcraft.common.entities;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.SPECIAL_ITEM;

//Oh if someone even kill everything like this(more than minecraft:item)you should kick their ass.
//TODO:Render
public class SpecialItemEntity extends ItemEntity {
    @Override
    public EntityType<?> getType() {
        return super.getType();
    }

    public SpecialItemEntity(Level level) {
        this(SPECIAL_ITEM(), level);
    }
    public SpecialItemEntity(EntityType<? extends ItemEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SpecialItemEntity(Level level, double d, double e, double f, ItemStack itemStack) {
        this(level, d, e, f, itemStack, level.random.nextDouble() * 0.2 - 0.1, 0.2, level.random.nextDouble() * 0.2 - 0.1);
    }

    public SpecialItemEntity(Level level, double d, double e, double f, ItemStack itemStack, double g, double h, double i) {
        this(SPECIAL_ITEM(), level);
        this.setPos(d, e, f);
        this.setDeltaMovement(g, h, i);
        this.setItem(itemStack);
    }

    private SpecialItemEntity(SpecialItemEntity itemEntity) {
        super(SPECIAL_ITEM(), itemEntity.level());
        this.setItem(itemEntity.getItem().copy());
        this.copyPosition(itemEntity);
//        this.age = itemEntity.getAge();
//        this.bobOffs = itemEntity.bobOffs;
    }

    @Override
    public @NotNull ItemEntity copy() {
        return new SpecialItemEntity(this);
    }

    @Override
    public void tick() {
        var movementPre = getDeltaMovement();
        var movementY = movementPre.y;
        if (movementY > (double)0.0F) {
            movementY *= 0.9F;
        }

        movementY += 0.04F;
        setDeltaMovement(new Vec3(movementPre.x,movementY,movementPre.z));
        super.tick();
    }
    @Override
    public boolean hurt(
            DamageSource source,
            float amount
    ) {
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        }

        return super.hurt(source, amount);
    }
}
