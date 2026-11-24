package com.linearity.opentc4.forge;

import com.linearity.annotations.StoleFrom;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.capability.IFluidHandler;
import thaumcraft.common.items.abstracts.IAttackBlockListenerItem;
import com.linearity.opentc4.ITickEvent;
import com.linearity.opentc4.PlatformUniqueUtils;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.tags.ITag;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static net.minecraftforge.common.capabilities.ForgeCapabilities.FLUID_HANDLER;

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
    public void registerOnLeftClickBlockListenerForItem(IAttackBlockListenerItem listener, Item forItem) {
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
    public List<Item> getItemsFromTag(@NotNull TagKey<Item> tagKey) {
        List<Item> items = new ArrayList<>();
        ITag<Item> tag = net.minecraftforge.registries.ForgeRegistries.ITEMS.tags().getTag(tagKey);
        tag.forEach(items::add);
        return items;
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
    public static class ForgeFluidInteractionManager extends FluidInteractionManager {

        @Override
        public long/*filled*/ insertFluidToPos(Level level, BlockPos pos, Direction dir, FluidStack fluidStack/*will be modified*/,boolean doit){
            var be = getExistingBlockEntity(level,pos);
            if (be == null) {
                return 0;
            }
            LazyOptional<IFluidHandler> cap = be.getCapability(FLUID_HANDLER,dir);
            var handlerInstance = cap.map(handler -> handler).orElse(null);
            if (handlerInstance == null) {
                return 0;
            }
            int filled = handlerInstance.fill(new net.minecraftforge.fluids.FluidStack(fluidStack.getFluid(),(int)(fluidStack.getAmount() % Integer.MAX_VALUE)),doit?IFluidHandler.FluidAction.EXECUTE:IFluidHandler.FluidAction.SIMULATE);
            if (filled != 0) {
                fluidStack.setAmount(fluidStack.getAmount()-filled);
            }
            return filled;
        }
        @Override
        public FluidStack/*extracted*/ extractFluidFromPos(Level level, BlockPos pos, Direction dir, long expectedAmount,boolean doit){
            var be = getExistingBlockEntity(level,pos);
            if (be == null) {
                return FluidStack.empty();
            }
            LazyOptional<IFluidHandler> cap = be.getCapability(FLUID_HANDLER,dir);
            var handlerInstance = cap.map(handler -> handler).orElse(null);
            if (handlerInstance == null) {
                return FluidStack.empty();
            }
            var extracted = handlerInstance.drain((int)(expectedAmount %Integer.MAX_VALUE),doit?IFluidHandler.FluidAction.EXECUTE:IFluidHandler.FluidAction.SIMULATE);
            return FluidStack.create(extracted.getFluid(),extracted.getAmount());
        }
        @Override
        public FluidStack getFluidFromPos(Level level, BlockPos pos, Direction dir,int tankIndex){
            var be = getExistingBlockEntity(level,pos);
            if (be == null) {
                return FluidStack.empty();
            }
            LazyOptional<IFluidHandler> cap = be.getCapability(FLUID_HANDLER,dir);
            var handlerInstance = cap.map(handler -> handler).orElse(null);
            if (handlerInstance == null) {
                return FluidStack.empty();
            }
            var extracted = handlerInstance.getFluidInTank(tankIndex);
            return FluidStack.create(extracted.getFluid(),extracted.getAmount());
        }
        @Override
        public int getFluidTanksFromPos(Level level, BlockPos pos, Direction dir){
            var be = getExistingBlockEntity(level,pos);
            if (be == null) {
                return 0;
            }
            LazyOptional<IFluidHandler> cap = be.getCapability(FLUID_HANDLER,dir);
            var handlerInstance = cap.map(handler -> handler).orElse(null);
            if (handlerInstance == null) {
                return 0;
            }
            return handlerInstance.getTanks();
        }

        @Override
        public @NotNull Fluid getFluidFromItemStack(ItemStack stack) {
            LazyOptional<IFluidHandlerItem> cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            var handlerItem = cap.map(handler -> handler.getFluidInTank(0)).orElse(null);
            if (handlerItem == null) {
                return Fluids.EMPTY;
            }
            return handlerItem.getFluid();
        }

        @Override
        public @Nullable FluidStack copyFluidStackFromItemStack(ItemStack stack) {
            LazyOptional<IFluidHandlerItem> cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            var handlerItem = cap.map(handler -> handler).orElse(null);
            if (handlerItem == null) {return null;}
            int tanks = handlerItem.getTanks();
            if (tanks <= 0){
                return null;
            }
            for (int tankIndex = 0; tankIndex < tanks; tankIndex++) {
                var got = handlerItem.getFluidInTank(tankIndex);
                if (!got.isEmpty()){
                    return FluidStack.create(got.getFluid(),got.getAmount());
                }
            }
            return FluidStack.empty();
        }


        private record TankInfo(net.minecraftforge.fluids.FluidStack stack,int capacity){}
        @Override
        public FluidStack drainFluidStackFromItemStack(ItemStack stack, FluidStack fluidStack, boolean doit) {
            LazyOptional<IFluidHandlerItem> cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            var handlerItem = cap.map(handler -> handler).orElse(null);
            if (handlerItem == null) {return FluidStack.empty();}
            var drained = handlerItem.drain(new net.minecraftforge.fluids.FluidStack(
                            fluidStack.getFluid(),
                            (int)(fluidStack.getAmount() % Integer.MAX_VALUE)),
                    doit?IFluidHandler.FluidAction.EXECUTE:IFluidHandler.FluidAction.SIMULATE);
            return FluidStack.create(drained.getFluid(),drained.getAmount());
        }

        @Override
        public long/*filled*/ insertFluidStackToItemStack(ItemStack stack, FluidStack fluidStack, boolean doit) {
            LazyOptional<IFluidHandlerItem> cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            var handlerItem = cap.map(handler -> handler).orElse(null);
            if (handlerItem == null) {return 0;}
            return handlerItem.fill(
                    new net.minecraftforge.fluids.FluidStack(fluidStack.getFluid(),(int)(fluidStack.getAmount() % Integer.MAX_VALUE)),
                    doit?IFluidHandler.FluidAction.EXECUTE:IFluidHandler.FluidAction.SIMULATE
            );
        }
    }

    protected final FluidInteractionManager fluidInteractionManager = new ForgeFluidInteractionManager();

    @Override
    public FluidInteractionManager getFluidInteractionManager() {
        return fluidInteractionManager;
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

    /**
     * Default UUID, for fake players not associated with a specific (human) player.
     */
    public static UUID DEFAULT_UUID = UUID.fromString("60C173A5-E1E6-4B87-85B1-272CE424521D");
    @StoleFrom("appeng.util.Platform#getFakePlayer")
    @Override
    public ServerPlayer getFakePlayer(ServerLevel level, @Nullable UUID playerUuid) {
        Objects.requireNonNull(level);

        if (playerUuid == null) {
            playerUuid = DEFAULT_UUID;
        }

        return FakePlayer.get(level, new GameProfile(playerUuid, "[Thaumcraft]"));
    }

    @Override
    public boolean isFakePlayer(ServerPlayer player) {
        return player instanceof net.minecraftforge.common.util.FakePlayer;
    }

    @StoleFrom("appeng.util.FakePlayer")
    public static class FakePlayer {
        private static final WeakHashMap<Level, net.minecraftforge.common.util.FakePlayer> FAKE_PLAYERS = new WeakHashMap<>();

        private FakePlayer() {
        }

        /**
         * DO NOT COPY THE PLAYER ANYWHERE! It will keep the world alive, always call this method if you need it.
         */
        static ServerPlayer get(ServerLevel level, GameProfile profile) {
            Objects.requireNonNull(level);

            var wrp = FAKE_PLAYERS.get(level);
            if (wrp != null) {
                return wrp;
            }

            var p = new net.minecraftforge.common.util.FakePlayer(level, profile);
            FAKE_PLAYERS.put(level, p);
            return p;
        }
    }

    @Override
    public void init() {

    }

}
