package com.linearity.opentc4.fabric;

import com.linearity.opentc4.annotations.StoleFrom;
import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.items.abstracts.IAttackBlockListenerItem;
import com.linearity.opentc4.ITickEvent;
import com.linearity.opentc4.PlatformUniqueUtils;
import com.linearity.opentc4.fabric.client.ThaumcraftModelProvider;
import com.linearity.opentc4.fabric.mixinaccessor.AttributeSupplierAccessor;
import com.linearity.opentc4.mixin.DefaultAttributesAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.fluid.FluidStack;
import dev.felnull.specialmodelloader.api.data.SpecialModelDataGenHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.abstracts.SingleFluidContainerBlockEntity;

import java.util.*;
import java.util.function.Consumer;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

//TODO:Figure out if i made misunderstanding to fabric fluid api
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
            SpecialModelDataGenHelper.generateObjModel(modelLocation.withPath(modelLocation.getPath().replace(".left",".json")),
                    modelLocation,
                    true,
                    false,
                    null,
                    itemModelGenerator.output);
        };
        ThaumcraftModelProvider.itemModelGenerators.add(toApplyModel);
    };

    @Override
    public void registerOnLeftClickBlockListenerForItem(IAttackBlockListenerItem listener, Item forItem){
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
    public List<Item> getItemsFromTag(TagKey<Item> tagKey) {
        RegistryAccess registryAccess = server.registryAccess();
        List<Item> items = new ArrayList<>();
        Registry<Item> itemRegistry = registryAccess.registryOrThrow(Registries.ITEM);
        Optional<HolderSet.Named<Item>> optHolders = itemRegistry.getTag(tagKey);
        optHolders.ifPresent(holders -> holders.stream().forEach(holder -> items.add(holder.value())));

        return items;
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
    @SuppressWarnings({"UnstableApiUsage"})
    public long/*filled*/ insertFluidToPos(Level level, BlockPos pos, Direction dir, FluidStack fluidStack/*will be modified*/,boolean doit){
        var be = getExistingBlockEntity(level,pos);
        if (be == null) {
            return 0;
        }

        var result = FluidStorage.SIDED.find(level,pos,dir);
        if (result == null){
            return 0;
        }
        var content = StorageUtil.findExtractableContent(result,null);
        if (content == null){
            return 0;
        }
        if (content.resource().getFluid() != fluidStack.getFluid()){
            return 0;
        }

        long filled = doit
                ? result.insert(content.resource(), fluidStack.getAmount(), null)
                : StorageUtil.simulateInsert(result,content.resource(),fluidStack.getAmount(),null);
        if (filled != 0) {
            fluidStack.setAmount(fluidStack.getAmount()-filled);
        }
        return filled;
    }
    @Override
    @SuppressWarnings({"UnstableApiUsage"})
    public FluidStack/*extracted*/ extractFluidFromPos(Level level, BlockPos pos, Direction dir, long expectedAmount,boolean doit){
        var be = getExistingBlockEntity(level,pos);
        if (be == null) {
            return FluidStack.empty();
        }

        var result = FluidStorage.SIDED.find(level,pos,dir);
        if (result == null){
            return FluidStack.empty();
        }
        var content = StorageUtil.findExtractableContent(result,null);
        if (content == null){
            return FluidStack.empty();
        }
        long got = doit
                ? result.extract(content.resource(), expectedAmount, null)
                : StorageUtil.simulateExtract(result,content.resource(),expectedAmount,null);
        return FluidStack.create(content.resource().getFluid(),got);
    }
    @Override
    @SuppressWarnings({"UnstableApiUsage"})
    public FluidStack getFluidFromPos(Level level, BlockPos pos, Direction dir,int tankIndex){
        var be = getExistingBlockEntity(level,pos);
        if (be == null) {
            return FluidStack.empty();
        }

        var result = FluidStorage.SIDED.find(level,pos,dir);
        if (result == null){
            return FluidStack.empty();
        }
        int currentIndex = 0;
        for (net.fabricmc.fabric.api.transfer.v1.storage.StorageView<FluidVariant> element : result) {
            currentIndex++;
            if (currentIndex == tankIndex) {
                return FluidStack.create(element.getResource().getFluid(), element.getAmount());
            }
        }
        return FluidStack.empty();
    }
    @Override
    @SuppressWarnings({"UnstableApiUsage"})
    public int getFluidTanksFromPos(Level level, BlockPos pos, Direction dir){
        var be = getExistingBlockEntity(level,pos);
        if (be == null) {
            return 0;
        }

        var result = FluidStorage.SIDED.find(level,pos,dir);
        if (result == null){
            return 0;
        }
        int currentIndex = 0;
        for (net.fabricmc.fabric.api.transfer.v1.storage.StorageView<FluidVariant> ignored : result) {
            currentIndex++;
        }
        return currentIndex;
    }


    @SuppressWarnings({"UnstableApiUsage"})
    public static @Nullable Storage<FluidVariant> getFluidStorageFromItemStack(ItemStack itemStack) {
        return FluidStorage.ITEM.find(itemStack,ContainerItemContext.ofSingleSlot(new SingleStackStorage() {
            @Override
            protected ItemStack getStack() {
                return itemStack;
            }

            @Override
            protected void setStack(ItemStack stack) {
                ExtendedItemStack.setTo(itemStack,stack);
            }
        }));
    }
    
    @Override
    @SuppressWarnings({"UnstableApiUsage"})
    public @NotNull Fluid getFluidFromItemStack(ItemStack itemStack) {
        var result = getFluidStorageFromItemStack(itemStack);
        if (result == null) return Fluids.EMPTY;
        var content = StorageUtil.findExtractableContent(result, null);
        if (content == null) return Fluids.EMPTY;
        return content.resource().getFluid();
    }
    
    @Override
    @SuppressWarnings({"UnstableApiUsage"})
    public @Nullable FluidStack copyFluidStackFromItemStack(ItemStack itemStack) {
        var result = getFluidStorageFromItemStack(itemStack);
        if (result == null) return null;
        var content = StorageUtil.findExtractableContent(result, null);
        if (content == null) return null;
        return FluidStack.create(content.resource().getFluid(), content.amount());
    }

    //return inserted
    @Override
    @SuppressWarnings({"UnstableApiUsage"})
    public long/*filled*/ insertFluidStackToItemStack(ItemStack itemStack, FluidStack fluidStack, boolean doit) {
        var result = getFluidStorageFromItemStack(itemStack);
        if (result == null) return 0;

        return doit
                ? result.insert(FluidVariant.of(fluidStack.getFluid()),fluidStack.getAmount(),null)
                : StorageUtil.simulateInsert(result,FluidVariant.of(fluidStack.getFluid()),fluidStack.getAmount(),null);
    }

    //return decreased
    @Override
    @SuppressWarnings({"UnstableApiUsage"})
    public FluidStack drainFluidStackFromItemStack(ItemStack itemStack,FluidStack fluidStack,boolean doit) {
        var result = getFluidStorageFromItemStack(itemStack);
        if (result == null) return FluidStack.empty();

        var drained = doit
                ?result.extract(FluidVariant.of(fluidStack.getFluid()),fluidStack.getAmount(),null)
                :StorageUtil.simulateExtract(result,FluidVariant.of(fluidStack.getFluid()),fluidStack.getAmount(),null);
        return drained <= 0 ? FluidStack.empty() :FluidStack.create(fluidStack.getFluid(), drained);
    }

    @Override
    @SuppressWarnings({"UnstableApiUsage"})
    public void init(){
        FluidStorage.SIDED.registerForBlocks(
                (world, pos, state, be, context) -> new SingleSlotStorage<>() {
                    @Override
                    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
                        if (be instanceof SingleFluidContainerBlockEntity crucible) {
                            return crucible.insertFluid(resource.getFluid(), maxAmount);
                        }
                        return 0;
                    }

                    @Override
                    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
                        if (be instanceof SingleFluidContainerBlockEntity crucible) {
                            return crucible.extractFluid(resource.getFluid(), maxAmount);
                        }
                        return 0;
                    }

                    @Override
                    public boolean isResourceBlank() {
                        return false;
                    }

                    @Override
                    public FluidVariant getResource() {
                        if (be instanceof SingleFluidContainerBlockEntity crucible) {
                            return FluidVariant.of(crucible.getFluidStack()
                                    .getFluid());
                        }
                        return null;
                    }

                    @Override
                    public long getAmount() {
                        if (be instanceof SingleFluidContainerBlockEntity crucible) {
                            return crucible.getFluidAmount();
                        }
                        return 0;
                    }

                    @Override
                    public long getCapacity() {
                        if (be instanceof SingleFluidContainerBlockEntity crucible) {
                            return crucible.getLiquidCapacity();
                        }
                        return 0;
                    }
                },
                ThaumcraftBlocks.ThaumcraftBlockInstances.CRUCIBLE(), ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_SPA()
        );
    }

    @StoleFrom("appeng.util.Platform#getFakePlayer")
    @Override
    public ServerPlayer getFakePlayer(ServerLevel level, @Nullable UUID playerUuid) {
        Objects.requireNonNull(level);

        if (playerUuid == null) {
            playerUuid = FakePlayer.DEFAULT_UUID;
        }

        return FakePlayer.get(level, new GameProfile(playerUuid, "[Thaumcraft]"));
    }
}
