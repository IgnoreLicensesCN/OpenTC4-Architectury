package thaumcraft.common.items.equipment.elemental;

import com.linearity.opentc4.utils.FakeUseOnContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.ThaumcraftToolAndArmorMaterial;

import static thaumcraft.api.listeners.elementalhoe.apply.ElementalHoeAffectManager.affectPlant;
import static thaumcraft.api.listeners.elementalhoe.check.ElementalHoeAffectiveManager.isAffectivePlant;

public class ElementalHoeItem extends HoeItem {
    public ElementalHoeItem(Tier tier, int i, float f, Properties properties) {
        super(tier, i, f, properties);
    }
    public ElementalHoeItem() {
        this(ThaumcraftToolAndArmorMaterial.TOOL_THAUMIUM_ELEMENTAL,
                -3,
                0.0F,
                new Item.Properties().stacksTo(1).rarity(Rarity.RARE)
        );
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        boolean affectedLand = false;
        var level = useOnContext.getLevel();
        var stack = useOnContext.getItemInHand();
        var user = useOnContext.getPlayer();
        for (var xOffset = -1; xOffset <= 1; xOffset++) {
            for (var zOffset = -1; zOffset <= 1; zOffset++) {
                var newUseOnContext = new FakeUseOnContext(
                        useOnContext.getLevel(),
                        useOnContext.getPlayer(),
                        useOnContext.getHand(),
                        stack,
                        new BlockHitResult(
                                useOnContext.getClickLocation(),
                                useOnContext.getClickedFace(),
                                useOnContext.getClickedPos(),
                                useOnContext.isInside()
                        )
                );
                if (super.useOn(newUseOnContext) != InteractionResult.PASS){
                    if (level instanceof ClientLevel clientLevel){
                        var pos = newUseOnContext.getClickedPos();
                        ClientFXUtils.blockSparkle(clientLevel, pos.getX(),pos.getY(),pos.getZ(), 0x803200, 2);
                    }
                    affectedLand = true;
                }
            }
        }
        if (affectedLand) {
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        var pickPos = useOnContext.getClickedPos();
        var pickState = level.getBlockState(pickPos);
        if (isAffectivePlant(level,pickState,pickPos)){
            if (!level.isClientSide){
                affectPlant(level, pickState, pickPos);
                if (user != null){
                    stack.hurtAndBreak(1, user, playerx -> playerx.broadcastBreakEvent(useOnContext.getHand()));
                } else {
                    stack.setDamageValue(stack.getDamageValue() + 1);
                    if (stack.getDamageValue() >= stack.getMaxDamage()){
                        stack.shrink(1);
                    }
                }
                level.playSound(user,
                        pickPos, ThaumcraftSounds.WAND, SoundSource.PLAYERS, 0.75F, 0.9F + level.getRandom().nextFloat() * 0.2F);
            } else if (level instanceof ClientLevel clientLevel){
                ClientFXUtils.blockSparkle(clientLevel, pickPos.getX(),pickPos.getY(),pickPos.getZ(), 0, 2);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
