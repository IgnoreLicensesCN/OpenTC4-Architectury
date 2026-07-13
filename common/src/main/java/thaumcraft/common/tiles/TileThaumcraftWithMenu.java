package thaumcraft.common.tiles;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@UtilityLikeAbstraction
public abstract class TileThaumcraftWithMenu<M extends AbstractContainerMenu,BE extends IThaumcraftBEWithMenu<M, BE>>
        extends TileThaumcraft
        implements IThaumcraftBEWithMenu<M, BE> {

    public TileThaumcraftWithMenu(
            BlockEntityType<? extends TileThaumcraftWithMenu<M,BE>> blockEntityType,
            BlockPos blockPos, BlockState blockState,
            IThaumcraftBEWithMenu.IThaumcraftBEWithMenuFactory<M,BE> menuFactory//hint:keep it "xxxMenu::new"
    ) {
        super(blockEntityType, blockPos, blockState);
        this.menuFactory = menuFactory;
    }
    private final IThaumcraftBEWithMenu.IThaumcraftBEWithMenuFactory<M,BE> menuFactory;


    @Override
    public IThaumcraftBEWithMenu.@NotNull IThaumcraftBEWithMenuFactory<M, BE> getMenuFactory() {
        return menuFactory;
    }

}
