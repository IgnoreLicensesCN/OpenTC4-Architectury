package thaumcraft.common.tiles.crafted.lamp;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IEssentiaTransportInBlockEntity;
import thaumcraft.api.aspects.IEssentiaTransportOutBlockEntity;
import thaumcraft.api.listeners.lamp.fertility.check.FertilityLampAffectiveManager;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.linearity.opentc4.Consts.FertilityArcaneLampTagAccessors.CHARGE;
import static thaumcraft.api.listeners.lamp.fertility.apply.FertilityLampAffectManager.affectEntity;
import static thaumcraft.common.blocks.crafted.lamps.ArcaneLampBlock.FACING;
import static thaumcraft.common.blocks.crafted.lamps.GrowthArcaneLampBlock.LIT;

public class FertilityArcaneLampBlockEntity extends TileThaumcraft implements IEssentiaTransportInBlockEntity {
    public FertilityArcaneLampBlockEntity(BlockEntityType<? extends FertilityArcaneLampBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public FertilityArcaneLampBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.FERTILITY_ARCANE_LAMP, blockPos, blockState);
    }

    public static final int CHARGES_PER_ESSENTIA = 1;
    public int getChargesPerEssentia(){
        return CHARGES_PER_ESSENTIA;
    }
    protected int charge = 0;
    protected int cooldown = 0;

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

        if (this.charge <= 4) {
            this.charge += this.drawEssentia() * getChargesPerEssentia();
        }

        boolean lightFlag = getLight();
        if (getCharge() <= 0 && lightFlag){
            this.level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(LIT, false));
        } else if (getCharge() > 0 && !lightFlag){
            this.level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(LIT, true));
        }

        if (this.charge >= getChargeCostEachTime()){
            this.updateAnimals();
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
        return Math.min(0,128 - getCharge() * 10);
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

    public static final int AFFECT_RADIUS = 7;
    public int getAffectRadius(){
        return AFFECT_RADIUS;
    }
    public static final int NO_ENTITY_AFFECTED_COOLDOWN = 600;
    public int getNoEntityAffectedCooldown(){
        return NO_ENTITY_AFFECTED_COOLDOWN;
    }
    public static final int HAS_ENTITY_AFFECTED_COOLDOWN = 300;
    public int getHasEntityAffectedCooldown(){
        return HAS_ENTITY_AFFECTED_COOLDOWN;
    }

    public static final int RECOMMENDED_ENTITY_LIMIT = 7;
    public int getRecommendedEntityLimit(){
        return RECOMMENDED_ENTITY_LIMIT;
    }

    public static final int CHARGE_COST_EACH_TIME = 2;
    public int getChargeCostEachTime(){
        return CHARGE_COST_EACH_TIME;
    }

    private void updateAnimals() {
        if (this.cooldown > 0){
            this.cooldown--;
            return;
        }
        if (this.level == null){return;}
//        Object2IntMap<Class<? extends Entity>> entityTypeCounter = new Object2IntOpenHashMap<>();
        var canGrowAnimalsNearby = this.level.getEntitiesOfClass(
                Entity.class,
                AABB.of(new BoundingBox(getBlockPos())).inflate(getAffectRadius()),
                FertilityLampAffectiveManager::isAffectiveEntity
        );
        HashMultimap<Class<? extends Entity>,Entity> entityMap = HashMultimap.create();
        canGrowAnimalsNearby.forEach(entity -> entityMap.put(entity.getClass(),entity));
//        canGrowAnimalsNearby.forEach(e -> entityTypeCounter.mergeInt(e.getClass(), 1, Integer::sum));

        int affectedCounter = 0;
        for (Class<? extends Entity> clazz : entityMap.keySet()) {
            Set<Entity> group = entityMap.get(clazz);

            // 调用方法
            if (affectEntity((Class<Entity>)clazz, group, getRecommendedEntityLimit())) {
                affectedCounter++;
                this.charge -= getChargeCostEachTime();
            }
        }
        if (affectedCounter == 0){
            this.cooldown += getNoEntityAffectedCooldown();
        }else {
            this.cooldown += getHasEntityAffectedCooldown();
        }

    }
}
