package thaumcraft.common.items.equipment.specialtool;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FireArrowItem extends ArrowItem {
    public FireArrowItem(Properties properties) {
        super(properties);
    }
    public FireArrowItem() {
        this(new Properties());
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        //TODO:Primal arrow entities
        return new FireArrowEntity();
    }
}
