package thaumcraft.common.tiles.crafted;

import com.linearity.opentc4.VecTransformations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ChestList;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.AbstractMultipartComponentBlock;
import thaumcraft.common.blocks.abstracts.IInfernalFurnaceTickDiscounter;
import thaumcraft.common.blocks.multipartcomponent.infernalfurnace.InfernalFurnaceLavaBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.Consts.InfernalFurnaceBlockEntityTagAccessors.PROCESSED_TICKS_ACCESSOR;
import static com.linearity.opentc4.Consts.InfernalFurnaceBlockEntityTagAccessors.PROCESSING_ITEM_STACK_ACCESSOR;

public class InfernalFurnaceBlockEntity extends TileThaumcraft {
    protected ChestList items = ChestList.withSize(32, ItemStack.EMPTY);
    public int processedTick = 0;
    public int speedyTime = 0;
    protected ItemStack processingStack = ItemStack.EMPTY;

    public InfernalFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public InfernalFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.INFERNAL_FURNACE, blockPos, blockState);
    }

    public void blockEntityTick() {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (processingStack.isEmpty()) {
            processedTick = 0;
            for (ItemStack itemStack : items) {
                if (!itemStack.isEmpty()) {
                    processingStack = itemStack;
                }
            }
        }
        if (!processingStack.isEmpty()) {
            InfernalFurnaceOutput outputs = calculateOutput(processingStack,serverLevel);
            if (outputs.outputStacks.length == 0) {
                processedTick = 0;
                processingStack.setCount(0);
                serverLevel.playSound(
                        null, this.getBlockPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
                return;
            }
            processedTick++;
            if (speedyTime == 0) {
                this.tryConsumeCentiVis();
            }
            if (processedTick >= calculateRequiredProcessTick()) {
                processingStack.shrink(1);
                outputResult(outputs);
                level.blockEvent(
                        this.getBlockPos().offset(VecTransformations.rotate(
                                ThaumcraftBlocks.INFERNAL_FURNACE_BAR.
                                        findTransformBasePosRelatedToSelf(
                                                level,
                                                this.getBlockState(),
                                                this.getBlockPos()
                                        )
                                ,getRotation()
                                )
                        ),
                        ThaumcraftBlocks.INFERNAL_FURNACE_BAR,
                        1,0
                );
            }
            if (speedyTime > 0) {
                speedyTime--;
            }
        }
    }

    public static final Vec3 outputPosRelatedInMultipart = new Vec3(1.6, 0, 0);
    public static final Vec3 outputVelocityRelatedInMultipart = new Vec3(0.13, 0, 0);

    public VecTransformations.Rotation3D getRotation(){
        var blockState = this.getBlockState();
        VecTransformations.Rotation3D rotation = null;
        int yRotState = blockState.getValue(AbstractMultipartComponentBlock.ROTATION_Y_AXIS);
        if (yRotState == AbstractMultipartComponentBlock.ROTATION_DEGREE_0) {
            rotation = VecTransformations.Rotation3D.NONE;
        } else if (yRotState == AbstractMultipartComponentBlock.ROTATION_DEGREE_90) {
            rotation = VecTransformations.Rotation3D.Y_90;
        } else if (yRotState == AbstractMultipartComponentBlock.ROTATION_DEGREE_180) {
            rotation = VecTransformations.Rotation3D.Y_180;
        } else if (yRotState == AbstractMultipartComponentBlock.ROTATION_DEGREE_270) {
            rotation = VecTransformations.Rotation3D.Y_270;
        }
        if (rotation == null) {
            throw new IllegalStateException("furnace rotation is null");
        }
        return rotation;
    }

    public void outputResult(InfernalFurnaceOutput outputs) {
        if (this.level == null) {
            return;
        }
        var pos = this.getBlockPos();
        VecTransformations.Rotation3D rotation = getRotation();
        var outputPosInWorld =
                new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
                        .add(
                                VecTransformations.rotate(outputPosRelatedInMultipart, rotation)
                        );
        var outputVelocity = VecTransformations.rotate(outputVelocityRelatedInMultipart, rotation);

        for (ItemStack toOutput : outputs.outputStacks) {
            if (toOutput == null||toOutput.isEmpty()) {continue;}
            ItemEntity itemEntity = new ItemEntity(
                    this.level,
                    outputPosInWorld.x,
                    outputPosInWorld.y,
                    outputPosInWorld.z,
                    toOutput,
                    outputVelocity.x,
                    outputVelocity.y,
                    outputVelocity.z
            );
            this.level.addFreshEntity(itemEntity);
        }
        if (outputs.outputExpCount > 0) {
            ExperienceOrb experienceOrb = new ExperienceOrb(
                    this.level,
                    outputPosInWorld.x,
                    outputPosInWorld.y,
                    outputPosInWorld.z, outputs.getOutputExp(this.level.getRandom())
            );
            this.level.addFreshEntity(experienceOrb);
        }
    }

    public record InfernalFurnaceOutput(ItemStack[] outputStacks, float outputExpCount) {
        public int getOutputExp(RandomSource random) {
            return ((int) outputExpCount) + (random.nextFloat() <= (outputExpCount - ((int) outputExpCount)) ? 1 : 0);
        }
    }

    public static final Map<Level, Map<ItemStack, InfernalFurnaceOutput>> outputCache = new ConcurrentHashMap<>();

    //note that multi-items (input count >= 1 or input type != 1) input isn't supported here
    @NotNull
    public InfernalFurnaceOutput calculateOutput(ItemStack input,Level level) {
        return outputCache.computeIfAbsent(level, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(
                        input,
                        inStack -> {
                            List<ItemStack> outputStacks = new ArrayList<>();
                            for (var smeltingRecipe : level.getRecipeManager()
                                    .getAllRecipesFor(RecipeType.SMELTING)) {
                                var ingredients = smeltingRecipe.getIngredients();

                                boolean matchesIngredient = false;
                                for (var ingredient : ingredients) {
                                    if (ingredient.test(inStack)) {
                                        matchesIngredient = true;
                                    }
                                }
                                if (!matchesIngredient) {
                                    continue;
                                }
                                var vanillaResult = smeltingRecipe.getResultItem(level.registryAccess());
                                float exp = smeltingRecipe.getExperience();
                                var thaumcraftSmeltingBonus = InfernalFurnaceLavaBlock.getSmeltingBonus(inStack);
                                if (vanillaResult != null) {
                                    outputStacks.add(vanillaResult);
                                }
                                if (thaumcraftSmeltingBonus != null) {
                                    outputStacks.add(thaumcraftSmeltingBonus);
                                }
                                return new InfernalFurnaceOutput(outputStacks.toArray(new ItemStack[0]), exp);
                            }
                            return new InfernalFurnaceOutput(new ItemStack[0], 0);
                        }
                );
    }

    public int calculateRequiredProcessTick() {
        return (this.speedyTime > 0 ? 80 : 140) - this.getProcessingTickDiscounts();
    }

    public static final Map<Direction,BlockPos> EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE = Map.of(
            Direction.SOUTH,new BlockPos(0,0,2),
            Direction.NORTH,new BlockPos(0,0,-2),
//            Direction.EAST,new  BlockPos(2,0,0),
            Direction.WEST,new BlockPos(-2,0,0),
            Direction.DOWN,new BlockPos(0,-2,0)
    );
    public static final Map<VecTransformations.Rotation3D,Map<Direction,BlockPos>> EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE_FOR_ROTATIONS = Map.of(
            VecTransformations.Rotation3D.NONE,EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE,
            VecTransformations.Rotation3D.Y_90, Map.of(
                    VecTransformations.rotate(Direction.SOUTH,VecTransformations.Rotation3D.Y_90),
                    VecTransformations.rotate(
                            EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE.get(Direction.SOUTH),
                            VecTransformations.Rotation3D.Y_90),

                    VecTransformations.rotate(Direction.NORTH,VecTransformations.Rotation3D.Y_90),
                    VecTransformations.rotate(
                            EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE.get(Direction.NORTH),
                            VecTransformations.Rotation3D.Y_90),

                    VecTransformations.rotate(Direction.WEST,VecTransformations.Rotation3D.Y_90),
                    VecTransformations.rotate(
                            EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE.get(Direction.WEST),
                            VecTransformations.Rotation3D.Y_90),
                    Direction.DOWN,new BlockPos(0,-2,0)
                    ),
            VecTransformations.Rotation3D.Y_180, Map.of(
                    VecTransformations.rotate(Direction.SOUTH,VecTransformations.Rotation3D.Y_180),
                    VecTransformations.rotate(
                            EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE.get(Direction.SOUTH),
                            VecTransformations.Rotation3D.Y_180),

                    VecTransformations.rotate(Direction.NORTH,VecTransformations.Rotation3D.Y_180),
                    VecTransformations.rotate(
                            EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE.get(Direction.NORTH),
                            VecTransformations.Rotation3D.Y_180),

                    VecTransformations.rotate(Direction.WEST,VecTransformations.Rotation3D.Y_180),
                    VecTransformations.rotate(
                            EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE.get(Direction.WEST),
                            VecTransformations.Rotation3D.Y_180),
                    Direction.DOWN,new BlockPos(0,-2,0)
            ),
            VecTransformations.Rotation3D.Y_270, Map.of(
                    VecTransformations.rotate(Direction.SOUTH,VecTransformations.Rotation3D.Y_270),
                    VecTransformations.rotate(
                            EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE.get(Direction.SOUTH),
                            VecTransformations.Rotation3D.Y_270),

                    VecTransformations.rotate(Direction.NORTH,VecTransformations.Rotation3D.Y_270),
                    VecTransformations.rotate(
                            EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE.get(Direction.NORTH),
                            VecTransformations.Rotation3D.Y_270),

                    VecTransformations.rotate(Direction.WEST,VecTransformations.Rotation3D.Y_270),
                    VecTransformations.rotate(
                            EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE.get(Direction.WEST),
                            VecTransformations.Rotation3D.Y_270),
                    Direction.DOWN,new BlockPos(0,-2,0)
            )
    );

    public int getProcessingTickDiscounts() {
        int discount = 0;
        if (level == null) {
            return discount;
        }
        var rotation = getRotation();
        for (var entry:EXPOSED_DIRECTIONS_AND_EXPECTED_DISCOUNTER_POSITION_RELATIVE_FOR_ROTATIONS.get(rotation).entrySet()) {
            var exposedDirection = entry.getKey();
            var expectedDiscounterPos = entry.getValue();
            var probablyDiscounterState = level.getBlockState(expectedDiscounterPos);
            if (probablyDiscounterState.getBlock() instanceof IInfernalFurnaceTickDiscounter discounter){
                discount += discounter.getInfernalFurnaceTickDiscount(level,probablyDiscounterState,expectedDiscounterPos,exposedDirection);
            }
        }
        return discount;
    }

    public void tryConsumeCentiVis() {
        speedyTime = VisNetHandler.drainVis(this.level, this.getBlockPos(), Aspects.FIRE, 5);
    }

    public ItemStack insertItemStack(ItemStack itemStackIn) {
        return this.items.insertItem(itemStackIn);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        this.items.saveAllItems(compoundTag);
        PROCESSED_TICKS_ACCESSOR.writeToCompoundTag(compoundTag, processedTick);
        PROCESSING_ITEM_STACK_ACCESSOR.writeToCompoundTag(compoundTag, processingStack);
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        this.items.loadAllItems(compoundTag);
        processedTick = PROCESSED_TICKS_ACCESSOR.readFromCompoundTag(compoundTag);
        processingStack = PROCESSING_ITEM_STACK_ACCESSOR.readFromCompoundTag(compoundTag);
    }

    @NotNull
    protected ChestList getItems() {
        return this.items;
    }
}
