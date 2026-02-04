package thaumcraft.common.gui;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.gui.menu.ArcaneWorkbenchMenu;
import thaumcraft.common.gui.menu.DeconstructionTableMenu;
import thaumcraft.common.tiles.crafted.ArcaneWorkbenchBlockEntity;
import thaumcraft.common.tiles.crafted.DeconstructionTableBlockEntity;

public class ThaumcraftGUI {
    public static final MenuType<ArcaneWorkbenchMenu> ARCANE_WORKBENCH = Registry.SUPPLIER_ARCANE_WORKBENCH.get();
    public static final MenuType<DeconstructionTableMenu> DECONSTRUCTION_TABLE = Registry.SUPPLIER_DECONSTRUCTION_TABLE.get();
    public static class Registry{
        public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.MENU);

        public static final RegistrySupplier<MenuType<ArcaneWorkbenchMenu>> SUPPLIER_ARCANE_WORKBENCH = MENUS.register(
                "arcane_workbench",
                () -> MenuRegistry.ofExtended(((id, inventory, buf) -> {
                    var bPos = buf.readBlockPos();
                    var level = inventory.player.level();
                    var be = level.getBlockEntity(bPos);
                    if (be instanceof ArcaneWorkbenchBlockEntity arcaneWorkbench){
                        return new ArcaneWorkbenchMenu(id,inventory,arcaneWorkbench);
                    }
                    OpenTC4.LOGGER.error("block entity not found.",new Exception());
                    return null;
                }))
        );
        public static final RegistrySupplier<MenuType<DeconstructionTableMenu>> SUPPLIER_DECONSTRUCTION_TABLE = MENUS.register(
                "deconstruction_table",
                () -> MenuRegistry.ofExtended(((id, inventory, buf) -> {
                    var bPos = buf.readBlockPos();
                    var level = inventory.player.level();
                    var be = level.getBlockEntity(bPos);
                    if (be instanceof DeconstructionTableBlockEntity deconstructionTable){
                        return new DeconstructionTableMenu(id,inventory,deconstructionTable);
                    }
                    OpenTC4.LOGGER.error("block entity not found.",new Exception());
                    return null;
                }))
        );
    }

    public static void init(){}
}
