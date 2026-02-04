package thaumcraft.common.gui;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.gui.menu.ArcaneWorkbenchMenu;

public class ThaumcraftGUI {
    public static final MenuType<ArcaneWorkbenchMenu> ARCANE_WORKBENCH = Registry.SUPPLIER_ARCANE_WORKBENCH.get();
    public static class Registry{
        public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.MENU);
        public static final RegistrySupplier<MenuType<ArcaneWorkbenchMenu>> SUPPLIER_ARCANE_WORKBENCH = MENUS.register(
                "arcane_workbench",
                () -> new MenuType<>(ArcaneWorkbenchMenu::new, FeatureFlags.VANILLA_SET)
        );
    }

    public static void init(){}
}
