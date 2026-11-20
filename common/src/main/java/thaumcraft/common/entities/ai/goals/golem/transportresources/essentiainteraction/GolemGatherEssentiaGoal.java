package thaumcraft.common.entities.ai.goals.golem.transportresources.essentiainteraction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.essentiabe.IEssentiaForceOutBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportOutBlockEntity;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.core.EssentiaGolemCoreItem;
import thaumcraft.common.tiles.abstracts.IAlembic;
import thaumcraft.common.tiles.crafted.essentiabe.AlchemicalFurnaceBlockEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class GolemGatherEssentiaGoal extends AbstractGolemEssentiaGoal{

    public GolemGatherEssentiaGoal(BaseGolemEntity golem) {
        super(golem);
    }

    @Override
    public double reachRange() {
        return 2.5;
    }

    protected BlockPos.MutableBlockPos alembicPos = new BlockPos.MutableBlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    @Override
    public boolean canUse() {
        var homeBEPos = golem.getHomeAttachedBlockPos();
        if (homeBEPos.distToCenterSqr(golem.position()) > reachRangeSqr()) {
            return false;
        }
        var level = golem.level();
        var homeBE = getExistingBlockEntity(level, homeBEPos);
        var dir = golem.homeDirection;
        if (homeBE instanceof AlchemicalFurnaceBlockEntity alchemicalFurnace){
            int aboveRange = alchemicalFurnace.getAlembicRange();
            alembicPos.set(homeBEPos);
            for (int aboveIndex = 1;aboveIndex <= aboveRange;aboveIndex++) {
                alembicPos.move(Direction.UP);
                if (getExistingBlockEntity(level,alembicPos) instanceof IAlembic alembic){
                    if (checkTransporter(alembic)){return true;}
                }
            }
        }
        if (homeBE instanceof IEssentiaTransportOutBlockEntity outBE){
            return checkTransporter(outBE,dir);
        }
        if (homeBE instanceof IEssentiaForceOutBlockEntity<?> outBE){
            return checkTransporter(outBE);
        }
        return false;
    }

    @Override
    public void start() {
        super.start();

        var homeBEPos = golem.getHomeAttachedBlockPos();
        var level = golem.level();
        var homeBE = getExistingBlockEntity(level, homeBEPos);
        var dir = golem.homeDirection;
        if (homeBE instanceof AlchemicalFurnaceBlockEntity alchemicalFurnace){
            int aboveRange = alchemicalFurnace.getAlembicRange();
            alembicPos.set(homeBEPos);
            for (int aboveIndex = 1;aboveIndex <= aboveRange;aboveIndex++) {
                alembicPos.move(Direction.UP);
                if (getExistingBlockEntity(level,alembicPos) instanceof IAlembic alembic){
                    if (extractFrom(alembic)){
                        return;
                    }
                }
            }
        }
        if (homeBE instanceof IEssentiaForceOutBlockEntity<?> outBE){
            if (extractFrom(outBE)){
                return;
            }
        }
        if (homeBE instanceof IEssentiaTransportOutBlockEntity outBE){
            if (extractFrom(outBE,dir)){
                return;
            }
        }

    }
    protected void onExtractedAspect(Aspect aspect,int amount){
        var holding = EssentiaGolemCoreItem.GolemHoldingEssentiaFakeField.getHolding(golem);
        holding.amount += amount;
        holding.aspect = aspect;
        golem.playSound(SoundEvents.GENERIC_SWIM,0.05F, golem.getRandom().nextFloat()*0.6F + 0.7F);
    }

    protected boolean extractFrom(IEssentiaForceOutBlockEntity<? extends Aspect> outBE){
        var aspects = outBE.getAspects();
        if (!aspects.isEmpty()){
            var holding = EssentiaGolemCoreItem.GolemHoldingEssentiaFakeField.getHolding(golem);
            holding.verify();
            int canHoldAmount = golem.getEssentiaCarryPower() - holding.amount;
            if (canHoldAmount > 0){
                var extracting = holding.aspect;
                if (extracting.isEmpty()){
                    extracting = outBE.getAspects().getFirstAspect();
                }
                if (!extracting.isEmpty()){
                    int reallyGot = Math.min(canHoldAmount,outBE.containerContains(holding.aspect));
                    if (reallyGot > 0){
                        if (outBE.takeFromContainer(holding.aspect,reallyGot)){
                            onExtractedAspect(extracting,reallyGot);
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }
    protected boolean extractFrom(IEssentiaTransportOutBlockEntity outBE,Direction dir){
        if (outBE.canOutputTo(dir)){
            var outType = outBE.getEssentiaType(dir);
            var outAmount = outBE.getEssentiaAmount(dir);
            var holding = EssentiaGolemCoreItem.GolemHoldingEssentiaFakeField.getHolding(golem);
            if (canReceiveAspectByType(holding.aspect, outType) && !outType.isEmpty()){
                int canHoldAmount = golem.getEssentiaCarryPower() - holding.amount;
                int got = outBE.takeEssentia(outType,Math.min(canHoldAmount,outAmount),dir);
                if (got > 0){
                    onExtractedAspect(outType,got);
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean checkTransporter(IEssentiaForceOutBlockEntity<? extends Aspect> outBE){
        var aspects = outBE.getAspects();
        if (!aspects.isEmpty()){
            var holding = EssentiaGolemCoreItem.GolemHoldingEssentiaFakeField.getHolding(golem);
            if (holding.amount < golem.getEssentiaCarryPower()){
                if (holding.aspect.isEmpty()){
                    return !aspects.getFirstAspect().isEmpty();
                }
                return outBE.containerContains(holding.aspect) > 0;
            }
        }
        return false;
    }
    protected boolean checkTransporter(IEssentiaTransportOutBlockEntity outBE, Direction dir){
        if (outBE.canOutputTo(dir)){
            var outType = outBE.getEssentiaType(dir);
            var outAmount = outBE.getEssentiaAmount(dir);
            var holding = EssentiaGolemCoreItem.GolemHoldingEssentiaFakeField.getHolding(golem);
            if (canReceiveAspectByType(holding.aspect, outType) && outAmount > 0){
                return holding.amount < golem.getEssentiaCarryPower();
            }
        }
        return false;
    }

    public static boolean canReceiveAspectByType(Aspect holdingAspect, Aspect outType) {
        return holdingAspect.isEmpty() || (holdingAspect.equals(outType) && !outType.isEmpty());
    }
}
