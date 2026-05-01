package thaumcraft.api.listeners.manabean;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

public class ManaBeanEatContext {
    public final @NotNull Aspect aspectOwning;
    public final @NotNull ItemStack eatingStack;
    public final @NotNull Level atLevel;
    public final @NotNull LivingEntity eater;

    public ManaBeanEatContext(@NotNull Aspect aspectOwning, @NotNull ItemStack eatingStack, @NotNull Level atLevel, @NotNull LivingEntity eater) {
        this.aspectOwning = aspectOwning;
        this.eatingStack = eatingStack;
        this.atLevel = atLevel;
        this.eater = eater;
    }
}
