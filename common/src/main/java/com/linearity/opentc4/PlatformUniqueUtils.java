package com.linearity.opentc4;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.abstracts.IAttackBlockListenerItem;

import java.util.List;
import java.util.UUID;

//a impl for fabric and one for forge
public abstract class PlatformUniqueUtils {

    public abstract List<Item> getItemsFromTag(TagKey<Item> key);

    public abstract MinecraftServer getServer();
    public abstract KeyMapping registerKeyBinding(String key, int glfwKeyCode, String categoryName);
    public abstract void registerKeyBinding(KeyMapping mapping);
    public abstract void registerClientTickStartEvent(ITickEvent tickEvent);
    public abstract void registerClientTickEndEvent(ITickEvent tickEvent);
    public abstract void registerServerTickStartEvent(ITickEvent tickEvent);
    public abstract void registerServerTickEndEvent(ITickEvent tickEvent);
    public abstract void registerCommand(TriConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext, Commands.CommandSelection> commandDispatcherConsumer);
    public abstract void registerEntityDefaultAttribute(EntityType<? extends LivingEntity> entityType, Attribute attribute);
    public abstract void registerModelForItem(Item item, ResourceLocation modelLocation);
    public abstract void registerOnLeftClickBlockListenerForItem(IAttackBlockListenerItem listener, Item forItem);
    public abstract BakedModel getModel(ResourceLocation modelLocation);
    public void registerModel(ResourceLocation modelLocation){};

    public abstract long/*filled*/ insertFluidToPos(Level level, BlockPos pos, Direction dir, FluidStack fluidStack/*will be modified*/,boolean doit);
    public abstract FluidStack/*extracted*/ extractFluidFromPos(Level level, BlockPos pos, Direction dir, long expectedAmount,boolean doit);
    public abstract FluidStack/*extracted*/ getFluidFromPos(Level level, BlockPos pos, Direction dir,int tankIndex);
    public abstract int getFluidTanksFromPos(Level level, BlockPos pos, Direction dir);
    public abstract @Nullable FluidStack copyFluidStackFromItemStack(ItemStack itemStack);
    public abstract @NotNull Fluid getFluidFromItemStack(ItemStack itemStack);
    public abstract FluidStack drainFluidStackFromItemStack(ItemStack itemStack,FluidStack fluidStack,boolean doit);
    public abstract long/*filled*/ insertFluidStackToItemStack(ItemStack itemStack, FluidStack fluidStack, boolean doit);

    public abstract ServerPlayer getFakePlayer(ServerLevel level, @Nullable UUID playerUuid);

    public abstract void init();

}
