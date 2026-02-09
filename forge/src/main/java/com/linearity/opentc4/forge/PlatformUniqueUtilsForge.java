package com.linearity.opentc4.forge;

import com.linearity.opentc4.AttackBlockListener;
import com.linearity.opentc4.ITickEvent;
import com.linearity.opentc4.PlatformUniqueUtils;
import com.linearity.opentc4.simpleutils.bauble.BaubleConsumer;
import com.linearity.opentc4.simpleutils.bauble.EquippedBaubleSlot;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

public class PlatformUniqueUtilsForge extends PlatformUniqueUtils {
    public MinecraftServer server;
    @Override
    public KeyMapping registerKeyBinding(String key, int glfwKeyCode, String categoryName) {
        KeyMapping keyMapping = new KeyMapping(
                key,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                categoryName
        );
        MinecraftForge.EVENT_BUS.addListener((RegisterKeyMappingsEvent event) -> {
            event.register(keyMapping);
        });
        return keyMapping;
    };
    @Override
    public void registerKeyBinding(KeyMapping mapping){
        MinecraftForge.EVENT_BUS.addListener((RegisterKeyMappingsEvent event) -> {
            event.register(mapping);
        });
    }

    @Override
    public void registerClientTickStartEvent(ITickEvent tickEvent) {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase == TickEvent.Phase.START) {
                tickEvent.tick();
            }
        });
    }
    @Override
    public void registerClientTickEndEvent(ITickEvent tickEvent) {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                tickEvent.tick();
            }
        });
    }
    @Override
    public void registerServerTickStartEvent(ITickEvent tickEvent) {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent event) -> {
            if (event.phase == TickEvent.Phase.START) {
                tickEvent.tick();
            }
        });
    }
    @Override
    public void registerServerTickEndEvent(ITickEvent tickEvent) {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                tickEvent.tick();
            }
        });
    }

    @Override
    public void registerCommand(TriConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext, Commands.CommandSelection> commandDispatcherConsumer) {
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            commandDispatcherConsumer.accept(event.getDispatcher(),event.getBuildContext(),event.getCommandSelection());
        });
    }

    @Override
    public void registerEntityDefaultAttribute(EntityType<? extends LivingEntity> entityType, Attribute attribute) {
        MinecraftForge.EVENT_BUS.addListener((EntityAttributeModificationEvent event) -> {
            event.add(entityType,attribute);
        });

    }

    @Override
    public void registerModelForItem(Item item, ResourceLocation modelLocation) {
        //no need here,but remember to write json :) (see resources/assets/thaumcraft/models/thaumometer.json)
    }

    @Override
    public void registerOnLeftClickBlockForItem(AttackBlockListener listener, Item forItem) {
        MinecraftForge.EVENT_BUS.addListener(
                (PlayerInteractEvent.LeftClickBlock event)
                        -> {
                    var player = event.getEntity();
                    var usingItem = event.getHand() == InteractionHand.MAIN_HAND ? player.getMainHandItem():player.getOffhandItem();
                    if (usingItem.getItem() == forItem){
                        listener.onLeftClickBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getPos(), event.getFace());
                    }
                }
        );
    }

    ;
    @Override
    public MinecraftServer getServer() {
        return server;
    }
    @Override
    public List<Item> getItemsFromTag(String key) {
        if (key == null) {
            return null;
        }

        // 创建 TagKey
        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, ResourceLocation.tryParse(key));
        return getItemsFromTag(tagKey);
    }

    @Override
    public List<Item> getItemsFromTag(@NotNull TagKey<Item> tagKey) {
        List<Item> items = new ArrayList<>();
        ITag<Item> tag = net.minecraftforge.registries.ForgeRegistries.ITEMS.tags().getTag(tagKey);
        tag.forEach(items::add);
        return items;
    }

    @Override
    public List<String> getTagsFromItem(ItemStack stack) {
        if (stack == null) return List.of();

        Item item = stack.getItem();
        IForgeRegistry<Item> registry = net.minecraftforge.registries.ForgeRegistries.ITEMS;
        ITagManager<Item> tagManager = registry.tags();

        if (tagManager == null) return List.of();

        List<String> tags = new ArrayList<>();
        tagManager.getTagNames().forEach(tagid -> {
            ITag<Item> tag = tagManager.getTag(tagid);
            if (tag.contains(item)) {
                tags.add(tagid.location().toString());
            }
        });

        return tags;
    }


    //TODO:Replacecment?
    @Override
    public List<Item> getItemVariants(Item item) {
        // Forge 也没有 getSubItems，返回默认实例即可
        List<Item> list = new ArrayList<>();
        list.add(item);
        return list;
    }
//    @Override
//    public boolean isItemStackMatchTag(ItemStack stack, String tagStr) {
//        if (stack == null || tagStr == null) return false;
//
//        Item item = stack.getItem();
//        IForgeRegistry<Item> registry = ForgeRegistries.ITEMS;
//        ITagManager<Item> tagManager = registry.tags();
//        if (tagManager == null) return false;
//
//        // 尝试解析 ResourceLocation
//        try {
//            TagKey<Item> tagKey = TagKey.create(registry.getRegistryKey(),ResourceLocation.tryParse(tagStr));
//            ITag<Item> tag = tagManager.getTag(tagKey);
//            return tag != null && tag.contains(item);
//        } catch (Exception e) {
//            return false;
//        }
//    }

    @Override
    public BakedModel getModel(ResourceLocation modelLocation) {
        return Minecraft.getInstance()
                .getModelManager()
                .getModel(new ModelResourceLocation(
                        modelLocation,
                        "inventory"
                ));
    }
    @Override
    public List<EquippedBaubleSlot> listEquippedSlots(Player player) {
        List<EquippedBaubleSlot> result = new ArrayList<>();

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.getCurios().forEach((slotType, stacksHandler) -> {
                var stacks = stacksHandler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    result.add(EquippedBaubleSlot.curios(slotType, i));
                }
            });
        });

        return result;
    }
    @Override
    public boolean forEachBauble(
            Player player,
            BaubleConsumer<Item> consumer
    ) {
        return CuriosApi.getCuriosInventory(player).map(handler -> {
            for (var entry : handler.getCurios().entrySet()) {
                var slots = entry.getValue().getStacks();
                for (int i = 0; i < slots.getSlots(); i++) {
                    ItemStack stack = slots.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        if (consumer.accept(EquippedBaubleSlot.curios(entry.getKey(),i), stack, stack.getItem())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }).orElse(false);
    }
    @Override
    public <T> boolean forEachBauble(
            Player player,
            Class<T> expectedItemType,
            BaubleConsumer<T> consumer
    ) {
        return forEachBauble(player, (slot, stack, item) -> {
            if (stack == null || item == null || stack.isEmpty()) return false;
            if (expectedItemType.isAssignableFrom(item.getClass())) {
                return consumer.accept(slot, stack, (T) item);
            }
            return false;
        });
    }

    @Override
    public boolean forEachBaubleWithType(
            String baubleType,
            Player player,
            BaubleConsumer<Item> consumer
    ) {
        return CuriosApi.getCuriosInventory(player).map(handler -> {
            for (var entry : handler.getCurios().entrySet()) {
                var slots = entry.getValue().getStacks();
                for (int i = 0; i < slots.getSlots(); i++) {
                    ItemStack stack = slots.getStackInSlot(i);
                    if (!stack.isEmpty() && Objects.equals(entry.getKey(), baubleType)) {
                        if (consumer.accept(EquippedBaubleSlot.curios(entry.getKey(),i), stack, stack.getItem())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }).orElse(false);
    }
    @Override
    public <T> boolean forEachBaubleWithType(
            String baubleType,
            Player player,
            Class<T> expectedItemType,
            BaubleConsumer<T> consumer
    ) {
        return forEachBaubleWithType(baubleType,player, (slot, stack, item) -> {
            if (stack == null || item == null || stack.isEmpty()) return false;
            if (expectedItemType.isAssignableFrom(item.getClass())) {
                return consumer.accept(slot, stack, (T) item);
            }
            return false;
        });
    }

    @Override
    public String[] listBaubleTypes(LivingEntity livingEntity) {
        Set<String> slotTypes = new HashSet<>();

        CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
            slotTypes.addAll(handler.getCurios().keySet());
        });
        return slotTypes.toArray(new String[0]);

    }
    @Override
    public Optional<ItemStack> getEquippedItem(Player player, EquippedBaubleSlot key) {
        if (!"curios".equals(key.namespace())) return Optional.empty();

        return CuriosApi.getCuriosInventory(player).flatMap(handler -> {
            var slot = handler.getCurios().get(key.slotType());
            if (slot == null) return Optional.empty();
            var stacks = slot.getStacks();
            if (key.index() < 0 || key.index() >= stacks.getSlots()) return Optional.empty();
            return Optional.of(stacks.getStackInSlot(key.index()));
        });
    }

}
