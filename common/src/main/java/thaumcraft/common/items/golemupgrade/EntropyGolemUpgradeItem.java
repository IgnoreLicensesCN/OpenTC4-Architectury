package thaumcraft.common.items.golemupgrade;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.List;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.ITEM_ENTITY_TEST;

//GolemUpgrade:5
public class EntropyGolemUpgradeItem extends Item implements TravelingTrunkEntity.ITravelingTrunkUpgradeItem {
    public EntropyGolemUpgradeItem(Properties properties) {
        super(properties);
    }

    public EntropyGolemUpgradeItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("golem_upgrade.entropy.desc"));
    }

    @Override
    public void travelingTrunkUpgrade$tick(TravelingTrunkEntity trunk, ItemStack upgradeStack) {
        var level = trunk.level();
        if (!trunk.isDeadOrDying() && !(trunk.getHealth() <= 0.0F)) {
            if (!level.isClientSide()) {
                //pick
                level.getEntities(
                        ITEM_ENTITY_TEST,
                        AABB.ofSize(
                                trunk.position(),
                                0.5F,
                                0.5F,
                                0.5F
                        ), itemEntity -> !itemEntity.hasPickUpDelay()
                ).forEach(itemEntity -> {
                    var stack = itemEntity.getItem();
                    ItemStack outstack = InventoryUtils.placeItemStackIntoInventory(stack, trunk.inventory, true);
                    if (outstack.isEmpty() || outstack.getCount() != stack.getCount()) {
                        trunk.playSound(SoundEvents.GENERIC_EAT, 0.5F, 0.5F + trunk.getRandom().nextFloat() * 0.5F);
                        level.broadcastEntityEvent(trunk, (byte) 17);
                        if (!outstack.isEmpty()) {
                            itemEntity.setItem(outstack);
                        } else {
                            itemEntity.discard();
                        }
                    }
                });
            }

            //drag in
            level.getEntities(
                    ITEM_ENTITY_TEST,
                    AABB.ofSize(
                            trunk.position(),
                            3,
                            3,
                            3
                    ), itemEntity -> !itemEntity.hasPickUpDelay()
            ).forEach(itemEntity -> {
                var pos = itemEntity.position();
                var trunkPos = trunk.position();
                var vecToItem = pos.subtract(trunkPos);
                var length = vecToItem.length();
                if (length > 0.01){
                    itemEntity.addDeltaMovement(vecToItem.scale(-0.075 / length));
                }
            });

        }
    }
}
