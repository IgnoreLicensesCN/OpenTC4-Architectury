package thaumcraft.common.items.placeableentity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEntityPlacerItem<EntityClass extends Entity> extends Item {
    public AbstractEntityPlacerItem(Properties properties) {
        super(properties);
    }

    public abstract @NotNull EntityType<EntityClass> getEntityTypeToPlace();

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        if (canPlaceEntityOn(useOnContext)){
            placeEntityOn(useOnContext);
            return InteractionResult.sidedSuccess(useOnContext.getLevel().isClientSide());
        }
        return super.useOn(useOnContext);
    }

    public boolean canPlaceEntityOn(UseOnContext useOnContext) {
        return true;
    }
    public void applyEntityTags(EntityClass entityPlaced, UseOnContext useOnContext) {
        var stack = useOnContext.getItemInHand();
        if (stack.hasCustomHoverName()){
            entityPlaced.setCustomName(stack.getHoverName());
        }
    }
    public void placeEntityOn(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var player = useOnContext.getPlayer();
        if (!level.isClientSide()) {
            var stack = useOnContext.getItemInHand();
            var entity = getEntityTypeToPlace().create(level);
            if (entity != null) {
                entity.setPos(useOnContext.getClickLocation());
                if (player != null) {
                    entity.lookAt(EntityAnchorArgument.Anchor.EYES,player.getLookAngle());
                }
                applyEntityTags(entity,useOnContext);


                if (player == null || !player.isCreative()){
                    stack.shrink(1);
                }
            }
        }
    }
}
