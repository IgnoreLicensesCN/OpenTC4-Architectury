package thaumcraft.common.menu;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import thaumcraft.common.tiles.IThaumcraftBEWithMenu;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.menu.menu.*;
import thaumcraft.common.tiles.crafted.*;
import thaumcraft.common.tiles.crafted.essentiabe.AlchemicalFurnaceBlockEntity;
import thaumcraft.common.tiles.crafted.essentiabe.ThaumatoriumBlockEntity;
import thaumcraft.common.tiles.crafted.vis.FocalManipulatorBlockEntity;

import java.util.function.Supplier;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class ThaumcraftGUI {
    public static class ThaumcraftMenuTypeInstances {
        public static MenuType<ArcaneWorkbenchMenu> ARCANE_WORKBENCH() {
            return Registry.SUPPLIER_ARCANE_WORKBENCH.get();
        }

        public static MenuType<DeconstructionTableMenu> DECONSTRUCTION_TABLE() {
            return Registry.SUPPLIER_DECONSTRUCTION_TABLE.get();
        }

        public static MenuType<ResearchTableMenu> RESEARCH_TABLE() {
            return Registry.SUPPLIER_RESEARCH_TABLE.get();
        }

        public static MenuType<AlchemicalFurnaceMenu> ALCHEMICAL_FURNACE() {
            return Registry.SUPPLIER_ALCHEMICAL_FURNACE.get();
        }

        public static MenuType<ThaumatoriumMenu> THAUMATORIUM() {
            return Registry.SUPPLIER_THAUMATORIUM.get();
        }

        public static MenuType<ArcaneBoreMenu> ARCANE_BORE() {
            return Registry.SUPPLIER_ARCANE_BORE.get();
        }

        public static MenuType<FocalManipulatorMenu> FOCAL_MANIPULATOR() {
            return Registry.SUPPLIER_FOCAL_MANIPULATOR.get();
        }

        public static MenuType<ArcaneSpaMenu> ARCANE_SPA() {
            return Registry.SUPPLIER_ARCANE_SPA.get();
        }
    }

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
        public static final RegistrySupplier<MenuType<ResearchTableMenu>> SUPPLIER_RESEARCH_TABLE = MENUS.register(
                "research_Table",
                simpleMenuTypeSupplier(ResearchTableMenu::new, ResearchTableBlockEntity.class)
        );
        public static final RegistrySupplier<MenuType<AlchemicalFurnaceMenu>> SUPPLIER_ALCHEMICAL_FURNACE = MENUS.register(
                "alchemical_furnace",
                simpleMenuTypeSupplier(AlchemicalFurnaceMenu::new, AlchemicalFurnaceBlockEntity.class)
        );
        public static final RegistrySupplier<MenuType<ThaumatoriumMenu>> SUPPLIER_THAUMATORIUM = MENUS.register(
                "thaumatorium",
                simpleMenuTypeSupplier(ThaumatoriumMenu::new, ThaumatoriumBlockEntity.class)
        );
        public static final RegistrySupplier<MenuType<ArcaneBoreMenu>> SUPPLIER_ARCANE_BORE = MENUS.register(
                "arcane_bore",
                simpleMenuTypeSupplier(ArcaneBoreMenu::new, ArcaneBoreBlockEntity.class)
        );
        public static final RegistrySupplier<MenuType<FocalManipulatorMenu>> SUPPLIER_FOCAL_MANIPULATOR = MENUS.register(
                "focal_manipulator",
                simpleMenuTypeSupplier(FocalManipulatorMenu::new, FocalManipulatorBlockEntity.class)
        );
        public static final RegistrySupplier<MenuType<ArcaneSpaMenu>> SUPPLIER_ARCANE_SPA = MENUS.register(
                "arcane_spa",
                simpleMenuTypeSupplier(ArcaneSpaMenu::new, ArcaneSpaBlockEntity.class)
        );
    }

    public static void init(){
        Registry.MENUS.register();
    }

    public static <M extends AbstractContainerMenu,BE extends IThaumcraftBEWithMenu<M,BE>>
    Supplier<MenuType<M>> simpleMenuTypeSupplier(
            IThaumcraftBEWithMenu.IThaumcraftBEWithMenuFactory<M, BE> factory,
            Class<BE> blockEntityClass
    ){
        return () -> MenuRegistry.ofExtended(((id, inventory, buf) -> {
            var bPos = buf.readBlockPos();
            var level = inventory.player.level();
            var be = LevelBlockEntityAccessing.getExistingBlockEntity(level, bPos);
            if (blockEntityClass.isInstance(be)){
                return factory.createMenu(id,inventory, (BE) be);
            }
            OpenTC4.LOGGER.error("block entity not found.",new Exception());
            return null;
        }));
    }
}
