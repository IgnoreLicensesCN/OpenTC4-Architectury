package thaumcraft.common.items.equipment.elemental;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.ThaumcraftToolAndArmorMaterial;
import thaumcraft.common.items.abstracts.IDowsingTool;

public class ElementalPickaxeItem extends PickaxeItem implements IDowsingTool {
    public ElementalPickaxeItem(Tier tier, int i, float f, Properties properties) {
        super(tier, i, f, properties);
    }
    public ElementalPickaxeItem() {
        this(
                ThaumcraftToolAndArmorMaterial.TOOL_THAUMIUM_ELEMENTAL,
                1, -2.8F,
                new Properties().stacksTo(1).rarity(Rarity.RARE)
        );
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2) {
        livingEntity.setSecondsOnFire(2);
        return super.hurtEnemy(itemStack, livingEntity, livingEntity2);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var stack = useOnContext.getItemInHand();
        var level = useOnContext.getLevel();
        if (!level.isClientSide){
            stack.setDamageValue(stack.getDamageValue() + 5);
            if (stack.getDamageValue() >= stack.getMaxDamage()) {
                stack.shrink(1);
            }
            level.playSound(
                    useOnContext.getPlayer(),
                    useOnContext.getClickedPos(),
                    ThaumcraftSounds.WAND_FAIL,
                    SoundSource.PLAYERS,
                    .2F,
                    .2F + level.random.nextFloat()*.2f
            );
        }else {
            //TODO:Render ores around
            //Thaumcraft.instance.renderEventHandler.startScan(player, x, y, z, System.currentTimeMillis() + 5000L, 8);
            //            player.swingItem();
            var player = useOnContext.getPlayer();
            if (player != null){
                player.swing(useOnContext.getHand());
            }
        }

        return super.useOn(useOnContext);
    }
}
