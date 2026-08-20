package thaumcraft.common.entities.monster.zombies;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.entity.ai.attributes.Attributes.SPAWN_REINFORCEMENTS_CHANCE;
import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.BRAINY_ZOMBIE;

public class BrainyZombieEntity extends Zombie {
    public BrainyZombieEntity(EntityType<? extends BrainyZombieEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BrainyZombieEntity(Level level) {
        this(BRAINY_ZOMBIE(),level);
    }
    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes().add(Attributes.MAX_HEALTH, 25).add(Attributes.ATTACK_DAMAGE,5).add(SPAWN_REINFORCEMENTS_CHANCE,0);
    }

    @Override
    public int getArmorValue() {
        return Math.max(super.getArmorValue()+3,20);
    }
}
