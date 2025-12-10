package com.linearity.opentc4;

import com.linearity.opentc4.simpleutils.bauble.BaubleConsumer;
import com.linearity.opentc4.simpleutils.bauble.EquippedBaubleSlot;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;
import java.util.Optional;

//a impl for fabric and one for forge
public abstract class PlatformUniqueUtils {

    public abstract List<Item> getItemsFromTag(String key);
    public abstract List<String> getTagsFromItem(ItemStack stack);
    public abstract List<Item> getItemVariants(Item item);
    public boolean isItemStackMatchTag(ItemStack stack, String tag){
        return stack.is(
                TagKey.create(Registries.ITEM, new ResourceLocation(tag))
        );
    };
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
    public abstract void registerOnLeftClickBlockForItem(AttackBlockListener listener, Item forItem);
    public abstract BakedModel getModel(ResourceLocation modelLocation);
    public void registerModel(ResourceLocation modelLocation){};
    public abstract Optional<ItemStack> getEquippedItem(
            Player player,
            EquippedBaubleSlot slotKey
    );

    public abstract List<EquippedBaubleSlot> listEquippedSlots(Player player);
    public abstract boolean forEachBauble(
            Player player,
            BaubleConsumer<Item> consumer
    );

    public abstract <T extends Item> boolean forEachBauble(
            Player player,
            Class<T> expectedItemType,
            BaubleConsumer<T> consumer
    );
    public abstract boolean forEachBaubleWithType(
            String baubleType,
            Player player,
            BaubleConsumer<Item> consumer
    );

    public abstract <T extends Item> boolean forEachBaubleWithType(
            String baubleType,
            Player player,
            Class<T> expectedItemType,
            BaubleConsumer<T> consumer
    );

    public abstract String[] listBaubleTypes(LivingEntity livingEntity);

}
