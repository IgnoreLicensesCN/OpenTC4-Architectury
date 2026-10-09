package thaumcraft.common.items.golem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.golems.IItemStackCollapsibleGolem;

public class GolemBellItem extends Item {
    public GolemBellItem(Properties properties) {
        super(properties);
    }
    public GolemBellItem() {
        this(new Properties().stacksTo(1));
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity victim, LivingEntity user) {
        if (victim instanceof IItemStackCollapsibleGolem collapsibleGolem) {
            if (collapsibleGolem.canBeCollapsedBy(user)){
                user.spawnAtLocation(
                        user.isShiftKeyDown()
                                ?collapsibleGolem.collapseToItemStackWithSneaking()
                                :collapsibleGolem.collapseToItemStackWithoutSneaking()
                        );
                victim.playSound(ThaumcraftSounds.ZAP, 0.5F, 1.0F);
                victim.discard();
            }
            return false;
        }
        return super.hurtEnemy(itemStack, victim, user);
    }
}
