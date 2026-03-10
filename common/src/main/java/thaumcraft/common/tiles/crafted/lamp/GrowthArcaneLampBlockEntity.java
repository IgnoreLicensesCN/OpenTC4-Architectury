package thaumcraft.common.tiles.crafted.lamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.*;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.GrowthArcaneLampTagAccessors.CHARGE;
import static thaumcraft.api.listeners.lamp.growth.apply.GrowthLampAffectManager.affectPlant;
import static thaumcraft.api.listeners.lamp.growth.check.GrowthLampAffectiveManager.isAffectivePlant;
import static thaumcraft.common.blocks.crafted.lamps.ArcaneLampBlock.FACING;
import static thaumcraft.common.blocks.crafted.lamps.GrowthArcaneLampBlock.LIT;

public class GrowthArcaneLampBlockEntity extends TileThaumcraft implements IEssentiaTransportInBlockEntity {
    public GrowthArcaneLampBlockEntity(BlockEntityType<? extends GrowthArcaneLampBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public GrowthArcaneLampBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.GROWTH_ARCANE_LAMP, blockPos, blockState);
    }

    public static final int CHARGES_PER_ESSENTIA = 100;
    public int getChargesPerEssentia(){
        return CHARGES_PER_ESSENTIA;
    }
    protected int charge = 0;

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        this.charge = CHARGE.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        CHARGE.writeToCompoundTag(compoundTag,this.charge);
    }

    public int getCharge() {
        return charge;
    }

    public Direction getFacing() {
        return getBlockState().getValue(FACING);
    }
    public boolean getLight() {
        return getBlockState().getValue(LIT);
    }

    public void serverTick(){
        if (this.level == null){
            return;
        }

        if (this.charge <= 0) {
            this.charge += this.drawEssentia() * getChargesPerEssentia();
        }

        boolean lightFlag = getLight();
        if (getCharge() <= 0 && lightFlag){
            this.level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(LIT, false));
        } else if (getCharge() > 0 && !lightFlag){
            this.level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(LIT, true));
        }

        if (this.charge > 0){
            this.updatePlant();
        }
    }

    public static final int ESSENTIA_DRAW_RATE = 1;
    public int getEssentiaDrawRate(){
        return ESSENTIA_DRAW_RATE;
    }

    public int drawEssentia() {
        //removed draw delay for less light update(if enough essentia)
//        if (++this.drawDelay % 5 != 0) {
//            return false;
//        } else
//        {
        if (level == null) {
            return 0;
        }
        var facing = getFacing();
        var facingOpposite = facing.getOpposite();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IEssentiaTransportOutBlockEntity outBE) {
            if (!outBE.canOutputTo(facingOpposite)) {return 0;}
            if (outBE.getMinimumSuctionToDrainOut() > this.getSuctionAmount(facing)){
                return 0;
            }
            return outBE.takeEssentia(Aspects.PLANT, getEssentiaDrawRate(), facingOpposite);
        }
        return 0;
//        }
    }

    @Override
    public boolean isConnectable(Direction face) {
        return face == getFacing();
    }

    @Override
    public boolean canInputFrom(Direction face) {
        return isConnectable(face);
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {

    }

    @Override
    public int getSuctionAmount(Direction face) {
        return getCharge() > 0 ? 0 : 128;
    }

    @Override
    public @NotNull Aspect getSuctionType(Direction face) {
        return getAllowedAspect();
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, Direction fromDirection) {
        if (aspect != getAllowedAspect()) {
            return 0;
        }
        if (this.charge > 0){
            return 0;
        }
        this.charge += amount * this.getChargesPerEssentia();
        return amount;
    }

    @Override
    @NotNull
    public Aspect getEssentiaType(Direction face) {
        return Aspects.EMPTY;
    }

    @Override
    public int getEssentiaAmount(Direction face) {
        return 0;
    }

    public Aspect getAllowedAspect(){
        return Aspects.PLANT;
    }

    public static final int AFFECT_RADIUS = 6;
    public int getAffectRadius(){
        return AFFECT_RADIUS;
    }

    private void updatePlant() {
        if (this.level == null){
            return;
        }
        int pickXOffset = this.level.random.nextInt(getAffectRadius()*2 +1) - getAffectRadius();
        int pickZOffset = this.level.random.nextInt(getAffectRadius()*2 +1) - getAffectRadius();
        for (int yOffset = -getAffectRadius(); yOffset <= getAffectRadius(); ++yOffset) {
            var pickPos = getBlockPos().offset(pickXOffset, yOffset, pickZOffset);
            var pickState = this.level.getBlockState(pickPos);
            if (isAffectivePlant(this.level,pickState,pickPos)){
                affectPlant(this.level,pickState,pickPos);
            }
        }

    }
}
