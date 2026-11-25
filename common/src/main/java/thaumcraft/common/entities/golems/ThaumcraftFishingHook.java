package thaumcraft.common.entities.golems;

import com.linearity.annotations.StoleFrom;
import com.linearity.opentc4.mixin.accessors.FishingHookAccessor;
import com.linearity.opentc4.mixinattachments.overrides.entity.fishinghook.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.THAUMCRAFT_FISHING_HOOK;

public class ThaumcraftFishingHook extends FishingHook
        implements
        FishingHookRecreateFromPacketOverride,
        FishingHookRetrieveOverride,
        FishingHookShouldStopFishingOverride,
        FishingHookTickOverride,
        FishingHookUpdateOwnerInfoOverride
{
    @StoleFrom("net.minecraft.world.entity.projectile.FishingHook")
    public ThaumcraftFishingHook(Entity owner, Level level, int luck, int lureSpeed) {
        this(THAUMCRAFT_FISHING_HOOK(), level);
        noCulling = false;
        var accessor = ((FishingHookAccessor)this);
        accessor.setLuck(Math.max(0,luck));
        accessor.setLureSpeed(Math.max(0,lureSpeed));

        this.setOwner(owner);
        thrownOutByOwner();
    }

    public void thrownOutByOwner(){
        var owner = getOwner();
        if (owner == null){
            return;
        }
        float xRot = owner.getXRot();
        float yRot = owner.getYRot();

        float h = Mth.cos(-yRot * (float) (Math.PI / 180.0) - (float) Math.PI);
        float k = Mth.sin(-yRot * (float) (Math.PI / 180.0) - (float) Math.PI);
        float l = -Mth.cos(-xRot * (float) (Math.PI / 180.0));
        float m = Mth.sin(-xRot * (float) (Math.PI / 180.0));

        double x = owner.getX() - k * 0.3;
        double y = owner.getEyeY();
        double z = owner.getZ() - h * 0.3;
        this.moveTo(x, y, z, yRot, xRot);
        Vec3 vec3 = new Vec3(-k, Mth.clamp(-(m / l), -5.0F, 5.0F), -h);
        double o = vec3.length();
        vec3 = vec3.multiply(
                0.6 / o + this.random.triangle(0.5, 0.0103365),
                0.6 / o + this.random.triangle(0.5, 0.0103365),
                0.6 / o + this.random.triangle(0.5, 0.0103365)
        );
        this.setDeltaMovement(vec3);
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2(vec3.y, vec3.horizontalDistance()) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public ThaumcraftFishingHook(EntityType<? extends FishingHook> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return false;
    }
}
