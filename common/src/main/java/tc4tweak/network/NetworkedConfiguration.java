package tc4tweak.network;

import tc4tweak.ConfigurationHandler;

public class NetworkedConfiguration {
    public static boolean checkWorkbenchRecipes = true;
    public static boolean smallerJar = false;

    public static boolean isCheckWorkbenchRecipes() {
        return checkWorkbenchRecipes;
    }

    public static void resetServer() {
        checkWorkbenchRecipes = ConfigurationHandler.INSTANCE.isCheckWorkbenchRecipes();
        smallerJar = ConfigurationHandler.INSTANCE.isSmallerJars();
    }

    public static void resetClient() {
        checkWorkbenchRecipes = ConfigurationHandler.INSTANCE.isCheckWorkbenchRecipes();
        smallerJar = false;
    }

    public static boolean isSmallerJar() {
        return smallerJar;
    }
}
