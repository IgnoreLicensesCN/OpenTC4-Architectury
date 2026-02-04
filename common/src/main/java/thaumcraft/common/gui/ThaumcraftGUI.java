package thaumcraft.common.gui;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import thaumcraft.api.tile.TileThaumcraftWithMenu;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.gui.menu.ArcaneWorkbenchMenu;
import thaumcraft.common.gui.menu.DeconstructionTableMenu;
import thaumcraft.common.tiles.crafted.ArcaneWorkbenchBlockEntity;
import thaumcraft.common.tiles.crafted.DeconstructionTableBlockEntity;

import java.util.function.Supplier;

public class ThaumcraftGUI {
    public static final MenuType<ArcaneWorkbenchMenu> ARCANE_WORKBENCH = Registry.SUPPLIER_ARCANE_WORKBENCH.get();
    public static final MenuType<DeconstructionTableMenu> DECONSTRUCTION_TABLE = Registry.SUPPLIER_DECONSTRUCTION_TABLE.get();
    public static class Registry{
        public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.MENU);

        public static final RegistrySupplier<MenuType<ArcaneWorkbenchMenu>> SUPPLIER_ARCANE_WORKBENCH = MENUS.register(
                "arcane_workbench",
                simpleMenuTypeSupplier(ArcaneWorkbenchMenu::new,ArcaneWorkbenchBlockEntity.class)
        );
        public static final RegistrySupplier<MenuType<DeconstructionTableMenu>> SUPPLIER_DECONSTRUCTION_TABLE = MENUS.register(
                "deconstruction_table",
                simpleMenuTypeSupplier(DeconstructionTableMenu::new,DeconstructionTableBlockEntity.class)
        );
    }

    public static void init(){}

    public static <M extends AbstractContainerMenu,BE extends TileThaumcraftWithMenu<M,BE>>
    Supplier<MenuType<M>> simpleMenuTypeSupplier(TileThaumcraftWithMenu.TileThaumcraftWithMenuFactory<M,BE> factory, Class<BE> blockEntityClass){
        return () -> MenuRegistry.ofExtended(((id, inventory, buf) -> {
            var bPos = buf.readBlockPos();
            var level = inventory.player.level();
            var be = level.getBlockEntity(bPos);
            if (blockEntityClass.isInstance(be)){
                return factory.createMenu(id,inventory, (BE) be);
            }
            OpenTC4.LOGGER.error("block entity not found.",new Exception());
            return null;
        }));
    }
}
