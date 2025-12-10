package thaumcraft.api;


import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Keys {
    public static final KeyMapping keyF = new KeyMapping("keys.thaumcraft.change_wand_focus", GLFW.GLFW_KEY_Y, "keys.thaumcraft.category");
    public static final KeyMapping keyH = new KeyMapping("keys.thaumcraft.active_hover_harness", GLFW.GLFW_KEY_H, "keys.thaumcraft.category");
    public static final KeyMapping keyG = new KeyMapping("keys.thaumcraft.misc_wand_toggle", GLFW.GLFW_KEY_G, "keys.thaumcraft.category");
}
