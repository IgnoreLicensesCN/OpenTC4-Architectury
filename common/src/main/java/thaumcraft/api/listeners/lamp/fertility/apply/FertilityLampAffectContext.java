package thaumcraft.api.listeners.lamp.fertility.apply;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Set;

public class FertilityLampAffectContext<E extends Entity> {
    public final Class<E> entityClass;
    public final @UnmodifiableView Set<E> entities;
    public final int recommendedEntityLimit;
    public boolean endAffect = false;
    public boolean affected = false;

    public FertilityLampAffectContext(
            Class<E> entityClass,
            @UnmodifiableView Set<E> entities,
            int recommendedEntityLimit) {
        this.entityClass = entityClass;
        this.entities = entities;
        this.recommendedEntityLimit = recommendedEntityLimit;
    }
}
