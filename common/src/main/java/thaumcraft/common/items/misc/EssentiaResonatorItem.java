package thaumcraft.common.items.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportConnectableBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.common.ThaumcraftSounds;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class EssentiaResonatorItem extends Item {
    public EssentiaResonatorItem(Properties properties) {
        super(properties);
    }
    public EssentiaResonatorItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var player = useOnContext.getPlayer();
        if (player == null) {
            return super.useOn(useOnContext);
        }
        var level = useOnContext.getLevel();
        var blockPos = useOnContext.getClickedPos();
        var hand = useOnContext.getHand();
        var be = getExistingBlockEntity(level, blockPos);
        var face = useOnContext.getClickedFace();
        if (be instanceof IEssentiaTransportConnectableBlockEntity connectableBE){
            if (level.isClientSide){
                player.swing(hand);
                return InteractionResult.sidedSuccess(true);
            }

            var type = connectableBE.getEssentiaType(face);
            var amount = connectableBE.getEssentiaAmount(face);
            player.displayClientMessage(Component.translatable(
                    "tc.resonator1",
                    amount,
                    type.getName()
                    ),false
            );

            if (be instanceof IEssentiaTransportInBlockEntity inBE){
                var suctionType = inBE.getSuctionType(face);
                var suctionTypeComponent = suctionType.isEmpty()?Component.translatable("tc.resonator3") : suctionType.getName();
                player.displayClientMessage(Component.translatable("tc.resonator2",inBE.getSuctionAmount(face),suctionTypeComponent),false);
            }
            level.playSound(player,blockPos, ThaumcraftSounds.ALEMBIC_KNOCK, SoundSource.PLAYERS, 0.5F, 1.9F + level.random.nextFloat() * 0.1F);
            return InteractionResult.sidedSuccess(false);
        }
        return super.useOn(useOnContext);
    }
}
