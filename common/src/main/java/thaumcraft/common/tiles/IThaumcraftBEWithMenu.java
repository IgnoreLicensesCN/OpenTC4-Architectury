package thaumcraft.common.tiles;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

@UtilityLikeAbstraction
public interface IThaumcraftBEWithMenu<M extends AbstractContainerMenu,BE extends IThaumcraftBEWithMenu<M, BE>>
        extends ExtendedMenuProvider {


    @Override
    default void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(getBlockPos());
    }

    @Override
    default @NotNull M createMenu(int i, Inventory inventory, Player player){
        return getMenuFactory().createMenu(
                i,
                inventory,
                (BE) this//any path to get not extended instance(this is abstract class) should be called "shouldn't happen"
        );
    };

    interface IThaumcraftBEWithMenuFactory<M extends AbstractContainerMenu,BE extends IThaumcraftBEWithMenu<M,BE>>{
        M createMenu(int i, Inventory inventory, BE blockEntity);
    }

    BlockPos getBlockPos();
    @NotNull
    IThaumcraftBEWithMenu.IThaumcraftBEWithMenuFactory<M,BE> getMenuFactory();

}
