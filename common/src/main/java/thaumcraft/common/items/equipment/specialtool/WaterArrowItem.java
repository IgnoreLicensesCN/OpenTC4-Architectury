package thaumcraft.common.items.equipment.specialtool;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.entities.projectile.AspectArrowEntity;

public class WaterArrowItem extends ArrowItem {
    public WaterArrowItem(Properties properties) {
        super(properties);
    }
    public WaterArrowItem() {
        this(new Properties());
    }

    @Override
    public @NotNull AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        return new AspectArrowEntity(level, Aspects.WATER);
    }
}
