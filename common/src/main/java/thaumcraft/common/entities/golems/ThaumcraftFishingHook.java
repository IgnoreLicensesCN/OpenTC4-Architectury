package thaumcraft.common.entities.golems;

import com.linearity.opentc4.mixinattachments.supercallproviders.entity.FishingHookSuperAccessor;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ThaumcraftFishingHook extends FishingHook {
    public ThaumcraftFishingHook(EntityType<? extends FishingHook> entityType, Level level) {
        super(entityType, level);
    }

    public ThaumcraftFishingHook(Player player, Level level, int i, int j) {
        super(player, level, i, j);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return false;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        ((FishingHookSuperAccessor)this).opentc4$projectileSuper$recreateFromPacket(clientboundAddEntityPacket);
    }
}
