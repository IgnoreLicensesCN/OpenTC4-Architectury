package com.linearity.opentc4.fabric;

import com.linearity.opentc4.AttackBlockListener;
import com.linearity.opentc4.ITickEvent;
import com.linearity.opentc4.PlatformUniqueUtils;
import com.linearity.opentc4.fabric.client.ThaumcraftModelProvider;
import com.linearity.opentc4.fabric.mixinaccessor.AttributeSupplierAccessor;
import com.linearity.opentc4.mixin.DefaultAttributesAccessor;
import com.linearity.opentc4.simpleutils.bauble.BaubleConsumer;
import com.linearity.opentc4.simpleutils.bauble.EquippedBaubleSlot;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.CommandDispatcher;
import dev.emi.trinkets.api.TrinketsApi;
import dev.felnull.specialmodelloader.api.data.SpecialModelDataGenHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.function.Consumer;

public class PlatformUniqueUtilsFabric extends PlatformUniqueUtils {
    public MinecraftServer server;

    @Override
    public MinecraftServer getServer() {
        return server;
    }

    @Override
    public KeyMapping registerKeyBinding(String key, int glfwKeyCode, String categoryName) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(
                key,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                categoryName
        ));
    }

    @Override
    public void registerKeyBinding(KeyMapping mapping) {
        KeyBindingHelper.registerKeyBinding(mapping);
    }

    @Override
    public void registerClientTickStartEvent(ITickEvent tickEvent) {
        ClientTickEvents.START_CLIENT_TICK.register(client -> tickEvent.tick());
    }
    @Override
    public void registerClientTickEndEvent(ITickEvent tickEvent) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> tickEvent.tick());
    }

    @Override
    public void registerServerTickStartEvent(ITickEvent tickEvent) {
        ServerTickEvents.START_SERVER_TICK.register(client -> tickEvent.tick());
    }
    @Override
    public void registerServerTickEndEvent(ITickEvent tickEvent) {
        ServerTickEvents.END_SERVER_TICK.register(client -> tickEvent.tick());
    }

    @Override
    public void registerCommand(TriConsumer<CommandDispatcher<CommandSourceStack>, CommandBuildContext, Commands.CommandSelection> commandDispatcherConsumer) {
        CommandRegistrationCallback.EVENT.register(commandDispatcherConsumer::accept);
    }

    @Override
    public void registerEntityDefaultAttribute(EntityType<? extends LivingEntity> entityType, Attribute attribute) {
        var oldSupplier = DefaultAttributesAccessor.opentc4$getSuppliers().get(entityType);
        if (oldSupplier == null) {
            FabricDefaultAttributeRegistry.register(entityType, AttributeSupplier.builder().add(attribute));
        }else {
            AttributeSupplier.Builder builder = AttributeSupplier.builder();
            builder.add(attribute);
            ((AttributeSupplierAccessor) oldSupplier).opentc4$getAttributesMap().forEach((attr, inst) ->
                    builder.add(attr, inst.getBaseValue())
            );
            FabricDefaultAttributeRegistry.register(entityType,builder);
        }
//        FabricDefaultAttributeRegistry.register(entityType, AttributeSupplier.builder().add(attribute));

    }

    @Override
    public void registerModelForItem(Item item, ResourceLocation modelLocation) {
        if (modelLocation == null) {
            throw new IllegalStateException("resource not found " + item);
        }
        Consumer<ItemModelGenerators> toApplyModel = (itemModelGenerator) -> {
            SpecialModelDataGenHelper.generateObjModel(item,
                    modelLocation,
                    true,
                    false,
                    null,
                    itemModelGenerator.output);
        };
        ThaumcraftModelProvider.itemModelGenerators.add(toApplyModel);
    }
    public void registerModel(ResourceLocation modelLocation){

        if (modelLocation == null) {
            throw new IllegalStateException("resource not found " + modelLocation);
        }
        Consumer<ItemModelGenerators> toApplyModel = (itemModelGenerator) -> {
            SpecialModelDataGenHelper.generateObjModel(modelLocation.withPath(modelLocation.getPath().replace(".obj",".json")),
                    modelLocation,
                    true,
                    false,
                    null,
                    itemModelGenerator.output);
        };
        ThaumcraftModelProvider.itemModelGenerators.add(toApplyModel);
    };

    @Override
    public void registerOnLeftClickBlockForItem(AttackBlockListener listener, Item forItem){
        AttackBlockCallback.EVENT.register((player, level, interactionHand, blockPos, direction) -> {
            var usingItem = interactionHand == InteractionHand.MAIN_HAND ? player.getMainHandItem():player.getOffhandItem();
            if (usingItem.getItem() == forItem){
                return listener.onLeftClickBlock(player, level, interactionHand, blockPos, direction);
            }
            return InteractionResult.PASS;
        });
    }

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
    public List<Item> getItemsFromTag(String key) {
        if (server == null) {
            throw new IllegalStateException("server is null, too early to call this method");
        }


        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(key));//无法访问 net. fabricmc. fabric. api. tag. FabricTagKey

        return getItemsFromTag(tagKey);
    }

    @Override
    public List<Item> getItemsFromTag(TagKey<Item> tagKey) {
        RegistryAccess registryAccess = server.registryAccess();
        List<Item> items = new ArrayList<>();
        Registry<Item> itemRegistry = registryAccess.registryOrThrow(Registries.ITEM);
        Optional<HolderSet.Named<Item>> optHolders = itemRegistry.getTag(tagKey);
        if (optHolders.isPresent()) {
            HolderSet.Named<Item> holders = optHolders.get();
            holders.stream().forEach(holder -> items.add(holder.value()));
        }

        return items;
    }

    @Override
    public List<String> getTagsFromItem(ItemStack itemStack) {
        List<String> tags = new ArrayList<>();
        RegistryAccess registryAccess = server.registryAccess();
        Registry<Item> itemRegistry = registryAccess.registryOrThrow(Registries.ITEM);

        // 遍历 Stream<Pair<TagKey<Item>, HolderSet.Named<Item>>>
        itemRegistry.getTags().forEach(pair -> {
            TagKey<Item> tagKey = pair.getFirst();
            HolderSet.Named<Item> holderSet = pair.getSecond();

            // 在 HolderSet 里查 item
            Item item = itemStack.getItem();
            boolean contains = holderSet.stream()
                    .map(Holder::value)
                    .anyMatch(i -> i == item);

            if (contains) {
                tags.add(tagKey.location().toString());
            }
        });

        return tags;
    }

    //TODO:Replacecment?
    @Override
    public List<Item> getItemVariants(Item item) {
        // Fabric 1.20 没有 getSubItems/CreativeTab，我们只能返回默认实例
        List<Item> list = new ArrayList<>();
        list.add(item);
        return list;
    }

//    @Override
//    public boolean isItemStackMatchTag(ItemStack stack, String tagStr) {
//        if (stack == null || tagStr == null || server == null) return false;
//
//        Item item = stack.getItem();
//        RegistryAccess registryAccess = server.registryAccess();
//        Registry<Item> itemRegistry = registryAccess.registryOrThrow(Registries.ITEM);
//
//        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, ResourceLocation.tryParse(tagStr));
//        Optional<HolderSet.Named<Item>> optTag = itemRegistry.getTag(tagKey);
//
//        return optTag.isPresent() && optTag.get().stream().map(Holder::value).anyMatch(i -> i == item);
//    }

    @Override
    public List<EquippedBaubleSlot> listEquippedSlots(Player player) {
        List<EquippedBaubleSlot> result = new ArrayList<>();

        TrinketsApi.getTrinketComponent(player).ifPresent(comp -> {
            comp.getInventory().forEach((groupId, group) -> {
                group.forEach((slotId, slot) -> {
                    for (int i = 0; i < slot.getContainerSize(); i++) {
                        result.add(
                                EquippedBaubleSlot.trinkets(groupId + "/" + slotId, i)
                        );
                    }
                });
            });
        });

        return result;
    }
    @Override
    public Optional<ItemStack> getEquippedItem(Player player, EquippedBaubleSlot key) {
        if (!"trinkets".equals(key.namespace())) return Optional.empty();

        return TrinketsApi.getTrinketComponent(player).flatMap(comp -> {
            String[] ids = key.slotType().split("/");
            var group = comp.getInventory().get(ids[0]);
            if (group == null) return Optional.empty();
            var slot = group.get(ids[1]);
            if (slot == null) return Optional.empty();
            if (key.index() < 0 || key.index() >= slot.getContainerSize()) return Optional.empty();
            return Optional.of(slot.getItem(key.index()));
        });
    }

    @Override
    public boolean forEachBauble(
            Player player,
            BaubleConsumer<Item> consumer
    ) {
        var comp = TrinketsApi.getTrinketComponent(player);
        if (comp.isEmpty()) return false;

        for (var group : comp.get().getInventory().values()) {
            for (var slot : group.values()) {
                for (int i = 0; i < slot.getContainerSize(); i++) {
                    ItemStack stack = slot.getItem(i);
                    if (!stack.isEmpty()) {
                        var item = stack.getItem();
                        if (consumer.accept(EquippedBaubleSlot.trinkets(slot.getSlotType().getName(),i), stack, item)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
    public String[] listBaubleTypes(LivingEntity livingEntity) {
        Set<String> slotTypes = new HashSet<>();

        TrinketsApi.getTrinketComponent(livingEntity).ifPresent(comp -> comp.getInventory().forEach(
                (groupId, group) ->
                group.keySet().forEach(
                        slotId -> slotTypes.add(groupId + "/" + slotId)
                )
        ));
        return slotTypes.toArray(new String[0]);
    }


    @Override
    public boolean forEachBaubleWithType(
            String baubleType,
            Player player,
            BaubleConsumer<Item> consumer
    ) {
        var comp = TrinketsApi.getTrinketComponent(player);
        if (comp.isEmpty()) return false;

        for (var group : comp.get().getInventory().values()) {
            for (var slot : group.values()) {
                for (int i = 0; i < slot.getContainerSize(); i++) {
                    ItemStack stack = slot.getItem(i);
                    if (!stack.isEmpty() && slot.getSlotType().getName().equals(baubleType)) {
                        if (consumer.accept(EquippedBaubleSlot.trinkets(slot.getSlotType().getName(),i), stack, stack.getItem())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
}
