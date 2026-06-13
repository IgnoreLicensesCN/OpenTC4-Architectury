package thaumcraft.common.tiles.crafted.essentiabe.advancedalchemicalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectView;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceUpperFenceBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.AdvancedAlchemicalFurnaceBlockEntityTagAccessors.*;
import static thaumcraft.api.listeners.aspects.item.bonus.ItemBonusAspectCalculator.getBonusAspects;

public class AdvancedAlchemicalFurnaceBlockEntity extends TileThaumcraft {
    public AdvancedAlchemicalFurnaceBlockEntity(BlockEntityType<? extends AdvancedAlchemicalFurnaceBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public AdvancedAlchemicalFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.ADVANCED_ALCHEMICAL_FURNACE, blockPos, blockState);
    }

    public static final int ASPECT_CAPACITY = 500;
    public static final int FUEL_VIS_CAPACITY = 500;

    public AspectList<Aspect> aspects = new LinkedHashAspectList<>();
    public UnmodifiableAspectView<Aspect> aspectsView = UnmodifiableAspectView.EMPTY;
    public int fuelVisAmouontFire = 0;
    private int fuelVisAmountEntropy = 0;
    private int fuelVisAmountWater = 0;

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ASPECTS_OWNING.writeToCompoundTag(compoundTag,aspects);
        FUEL_AMOUNT_FIRE.writeIntToCompoundTag(compoundTag, fuelVisAmouontFire);
        FUEL_AMOUNT_ENTROPY.writeIntToCompoundTag(compoundTag,fuelVisAmountEntropy);
        FUEL_AMOUNT_WATER.writeIntToCompoundTag(compoundTag,fuelVisAmountWater);
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        aspects = ASPECTS_OWNING.readFromCompoundTag(compoundTag);
        aspectsView = new UnmodifiableAspectView<>(aspects);
        fuelVisAmouontFire = FUEL_AMOUNT_FIRE.readIntFromCompoundTag(compoundTag);
        fuelVisAmountEntropy = FUEL_AMOUNT_ENTROPY.readIntFromCompoundTag(compoundTag);
        fuelVisAmountWater = FUEL_AMOUNT_WATER.readIntFromCompoundTag(compoundTag);
    }

    public int getVisCapacity() {
        return ASPECT_CAPACITY;
    }

    protected int getFuelVisCapacity() {
        return FUEL_VIS_CAPACITY;
    }
    
    public int tickCount  = System.identityHashCode(this) & 63;
    public int burnItemTickCooldown = 0;


    public void serverTick() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        //destroy logic is in multipart now not here.
        tickCount += 1;
        calculateCooldown();
        if (tickCount %5 == 0){
            updateFuelVis();
        }
    }

    public void calculateCooldown() {
        if (burnItemTickCooldown > 0){
            burnItemTickCooldown -= 1;
        }
    }
    
    public void updateFuelVis() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        var pos = getBlockPos();
        int pt = this.fuelVisAmouontFire--;
        if (this.fuelVisAmouontFire <= getFuelVisCapacity()) {
            this.fuelVisAmouontFire += VisNetHandler.drainCentiVis(this.level, pos, Aspects.FIRE, 50);
        }

        if (this.fuelVisAmountEntropy <= getFuelVisCapacity()) {
            this.fuelVisAmountEntropy += VisNetHandler.drainCentiVis(this.level, pos, Aspects.ENTROPY, 50);
        }

        if (this.fuelVisAmountWater <= getFuelVisCapacity()) {
            this.fuelVisAmountWater += VisNetHandler.drainCentiVis(this.level, pos, Aspects.WATER, 50);
        }

        if (pt / 50 != this.fuelVisAmouontFire / 50) {
            markDirtyAndUpdateSelf();
        }
        updateFences();
    }
    public static final BlockPos[] FENCES_POS_RELATED = {
            BlockPos.ZERO.above().north(),
            BlockPos.ZERO.above().south(),
            BlockPos.ZERO.above().east(),
            BlockPos.ZERO.above().west(),
    };
    public void updateFences(){
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        var selfPos = getBlockPos();
        for (var relatedPos : FENCES_POS_RELATED) {
            var fencePos = selfPos.offset(relatedPos);
            var fenceState = this.level.getBlockState(fencePos);
            int lightLevel = (int) (this.fuelVisAmouontFire / (float) getFuelVisCapacity() * 12.0F);
            int currentLightLevel = fenceState.getValue(AdvancedAlchemicalFurnaceUpperFenceBlock.LIGHT_LEVEL);
            if (lightLevel != currentLightLevel){
                fenceState.setValue(AdvancedAlchemicalFurnaceUpperFenceBlock.LIGHT_LEVEL, lightLevel);
                level.setBlockAndUpdate(fencePos, fenceState);
            }
        }
    }

    public void handleItemEntity(ItemEntity itemEntity) {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        if (burnItemTickCooldown <= 0){
            var stack = itemEntity.getItem();
            if (stack.isEmpty()) {
                return;
            }
            var aspectsContaining = getBonusAspects(stack,!this.level.isClientSide);
            if (!aspectsContaining.isEmpty()) {
                var visSize = aspectsContaining.visSize();
                if (visSize*2 <= this.fuelVisAmouontFire
                        && visSize < this.fuelVisAmountEntropy
                        && visSize <= this.fuelVisAmountWater
                        && visSize < getVisCapacity() - this.aspects.visSize()
                ) {
                    this.fuelVisAmouontFire -= visSize*2;
                    this.fuelVisAmountEntropy -= visSize;
                    this.fuelVisAmountWater -= visSize;

                    float baseCooldown = this.burnItemTickCooldown;
                    float heatFactor = 1.0f - ((float)this.fuelVisAmouontFire / baseCooldown);
                    heatFactor = Math.max(0.0f, heatFactor);
                    float additionalCooldown = 5.0f + heatFactor * 100.0f;
                    this.burnItemTickCooldown += (int) additionalCooldown;

                    level.playSound(itemEntity,getBlockPos(), ThaumcraftSounds.BUBBLE, SoundSource.BLOCKS, 0.2F, 1.0F + level.getRandom().nextFloat() * 0.4F);
                    stack.shrink(1);
                    markDirtyAndUpdateSelf();
                    if (stack.isEmpty()){
                        itemEntity.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
            }
        }
    }

}
