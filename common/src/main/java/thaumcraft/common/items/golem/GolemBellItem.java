package thaumcraft.common.items.golem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.IItemStackCollapsibleGolem;
import thaumcraft.common.entities.golems.Marker;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.IGolemColorfulMarkerProviderItem;

import java.util.Objects;

import static com.linearity.opentc4.Consts.GolemBellTagAccessors.*;
import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.common.entities.golems.Marker.INVALID_MARKER_COLOR;
import static thaumcraft.common.entities.golems.Marker.MARKER_COLOR_MAX;

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

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var usingStack = useOnContext.getItemInHand();
        var stackTag = usingStack.getOrCreateTag();
        var player = useOnContext.getPlayer();
        if (player != null){
            var golem = findGolem(usingStack);
            if (golem != null){
                if (golem.isOwnedBy(player.getUUID())){
                    var level = useOnContext.getLevel();

                    var usedAtPos = useOnContext.getClickedPos();
                    var usedAtFace = useOnContext.getClickedFace();
                    ResourceLocation levelResLoc = level.dimension().location();

                    Marker markerExisting = findMarkerAt(golem, usedAtPos, usedAtFace, levelResLoc);

                    if (markerExisting != null){
                        if (!player.isShiftKeyDown()
                                && !golem.golemUpgradesCacheView.get(IGolemColorfulMarkerProviderItem.class).isEmpty()){
                            golem.markers.remove(markerExisting);
                            byte newColor = markerExisting.color;
                            newColor += 1;
                            if (newColor > MARKER_COLOR_MAX){
                                newColor = INVALID_MARKER_COLOR;
                            }
                            if (level.isClientSide()){
                                if (newColor != INVALID_MARKER_COLOR){
                                    player.sendSystemMessage(Component.translatable("tc.markerchange", UtilsFX.colorNames[newColor-1]));
                                }else {
                                    player.sendSystemMessage(Component.translatable("tc.markerchangeany"));
                                }
                            }

                            markerExisting = new Marker(markerExisting.pos,markerExisting.dim,markerExisting.side,newColor);
                            addMarkerFor(golem, markerExisting, stackTag);
                        } else {
                            golem.markers.remove(markerExisting);
                        }
                    }else {
                        Marker marker = new Marker(usedAtPos,levelResLoc,usedAtFace,INVALID_MARKER_COLOR);
                        addMarkerFor(golem, marker, stackTag);
                    }
                    golem.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.7F, 1.0F + golem.getRandom().nextFloat() * 0.1F);
                    player.swing(useOnContext.getHand());
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        return super.useOn(useOnContext);
    }

    public static void addMarkerFor(BaseGolemEntity golem, Marker markerExisting, CompoundTag stackTag) {
        golem.markers.add(markerExisting);
        var markersPutInItem = MARKERS.readFromCompoundTag(stackTag);
        markersPutInItem.add(markerExisting);
        MARKERS.writeToCompoundTag(stackTag,markersPutInItem);
    }
    public static void removeMarkerFor(BaseGolemEntity golem, Marker markerExisting, CompoundTag stackTag) {
        golem.markers.remove(markerExisting);
        var markersPutInItem = MARKERS.readFromCompoundTag(stackTag);
        markersPutInItem.remove(markerExisting);
        MARKERS.writeToCompoundTag(stackTag,markersPutInItem);
    }

    private static @Nullable Marker findMarkerAt(BaseGolemEntity golem, BlockPos usedAtPos, Direction usedAtFace, ResourceLocation levelResLoc) {
        var markers = golem.markers;
        for (var marker : markers) {
            if (Objects.equals(marker.pos, usedAtPos)
            && Objects.equals(marker.side, usedAtFace)
            && Objects.equals(marker.dim, levelResLoc)
            ){
                return marker;
            }
        }
        return null;
    }

    public void onInteractGolem(Player player, InteractionHand hand, ItemStack usingStack, BaseGolemEntity golem) {
        var tag = usingStack.getOrCreateTag();
        if (!golem.isOwnedBy(player.getUUID())) {
            golem.playSound(ThaumcraftSounds.ZAP, 0.7F, 1.0F + golem.getRandom().nextFloat() * 0.1F);
            return;
        }
        if (findGolem(usingStack) != golem){
            MARKERS.writeToCompoundTag(tag,golem.markers);
            HOME_FACE.writeToCompoundTag(tag,golem.homeDirection);
            HOME_POS.writeToCompoundTag(tag,golem.getRestrictCenter());
            HOME_LEVEL.writeToCompoundTag(tag,golem.homeLevelResourceLocation);
            GOLEM_ID.writeToCompoundTag(tag,golem.getUUID());
        }else {
            tag.remove(MARKERS.tagKey);
            tag.remove(HOME_FACE.tagKey);
            tag.remove(HOME_POS.tagKey);
            tag.remove(HOME_LEVEL.tagKey);
            tag.remove(GOLEM_ID.tagKey);
        }
        golem.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.7F, 1.0F + golem.getRandom().nextFloat() * 0.1F);
        player.swing(hand);

    }

    public @Nullable BaseGolemEntity findGolem(ItemStack stack){
        var tag = stack.getTag();
        if (tag == null){
            return null;
        }
        for (var level:platformUtils.getServer().getAllLevels()){
            var result = level.getEntity(GOLEM_ID.readFromCompoundTag(tag));
            if (result instanceof BaseGolemEntity golem){
                return golem;
            }
        }
        return null;
    }
}
