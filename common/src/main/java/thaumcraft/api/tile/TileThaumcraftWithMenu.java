package thaumcraft.api.tile;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class TileThaumcraftWithMenu<M extends AbstractContainerMenu,BE extends TileThaumcraftWithMenu<M, BE>> extends TileThaumcraft implements ExtendedMenuProvider {
    private final TileThaumcraftWithMenuFactory<M,BE> menuFactory;
    public TileThaumcraftWithMenu(
            BlockEntityType<? extends TileThaumcraftWithMenu<M,BE>> blockEntityType,
            BlockPos blockPos, BlockState blockState,
            TileThaumcraftWithMenuFactory<M,BE> menuFactory//hint:keep it "xxxMenu::new"
    ) {
        super(blockEntityType, blockPos, blockState);
        this.menuFactory = menuFactory;
    }

    @Override
    public final void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }

    @Override
    public final @NotNull M createMenu(int i, Inventory inventory, Player player){
        return menuFactory.createMenu(
                i,
                inventory,
                (BE) this//any path to get not extended instance(this is abstract class) should be called "shouldn't happen"
        );
    };

    public interface TileThaumcraftWithMenuFactory<M extends AbstractContainerMenu,BE extends TileThaumcraftWithMenu<M,BE>>{
        M createMenu(int i, Inventory inventory, BE blockEntity);
    }

}
