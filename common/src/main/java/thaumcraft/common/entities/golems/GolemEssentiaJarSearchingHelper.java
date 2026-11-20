package thaumcraft.common.entities.golems;

import com.linearity.opentc4.utils.BlockSearchDeque;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import lombok.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectFilterAccessibleBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaOverflowDestroyerBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportOutBlockEntity;
import thaumcraft.common.entities.golems.GolemEssentiaJarSearchingHelper.GolemSearchingEssentiaJarPriority.FillInPriorityCalculator.PriorityCalculatorContext.MutablePriorityCalculatorContext;

public class GolemEssentiaJarSearchingHelper {
    //lower priority first
    //we wont make sure blockEntity could be used to store aspect here you need to test in other ways
    public static class GolemSearchingEssentiaJarPriority implements Comparable<GolemSearchingEssentiaJarPriority> {
        @AllArgsConstructor
        public static abstract class FillInPriorityCalculator implements Comparable<FillInPriorityCalculator> {

            @AllArgsConstructor
            @Getter
            public static class PriorityCalculatorContext {
                protected @Nullable IEssentiaTransportInBlockEntity inBE;//== maybeEssentiaJar
                protected @NotNull BlockEntity maybeEssentiaJarToFillIn;//we want to insert aspect here
                protected @NotNull Aspect expectedAspectToInsert;
                protected int expectAmountToInsert;
                protected @NotNull Direction interactionDirection;
                protected @Nullable BaseGolemEntity golem;


                public static class MutablePriorityCalculatorContext extends PriorityCalculatorContext {
                    public MutablePriorityCalculatorContext(
                            @NotNull IEssentiaTransportInBlockEntity inBE,
                            @NotNull Aspect expectedAspectToInsert,
                            int expectAmountToInsert,
                            @Nullable BaseGolemEntity golem
                    ){
                        this(inBE,expectedAspectToInsert,expectAmountToInsert,Direction.UP,golem, (BlockEntity) inBE);
                    }
                    public MutablePriorityCalculatorContext(
                            @NotNull IEssentiaTransportInBlockEntity inBE,
                            @NotNull Aspect expectedAspectToInsert,
                            int expectAmountToInsert,
                            @NotNull Direction interactionDirection,
                            @Nullable BaseGolemEntity golem,
                            @NotNull BlockEntity maybeEssentiaJarToFillIn
                    ) {
                        super(inBE,maybeEssentiaJarToFillIn, expectedAspectToInsert, expectAmountToInsert, interactionDirection, golem);
                    }

                    public void setExpectedAspectToInsert(@NotNull Aspect expectedAspectToInsert) {
                        this.expectedAspectToInsert = expectedAspectToInsert;
                    }

                    public void setExpectAmountToInsert(int expectAmountToInsert) {
                        this.expectAmountToInsert = expectAmountToInsert;
                    }

                    public void setInteractionDirection(@NotNull Direction interactionDirection) {
                        this.interactionDirection = interactionDirection;
                    }

                    public void setGolem(@Nullable BaseGolemEntity golem) {
                        this.golem = golem;
                    }
                    public void setInBE(@NotNull IEssentiaTransportInBlockEntity inBE) {
                        this.inBE = inBE;
                        this.maybeEssentiaJarToFillIn = (BlockEntity) inBE;
                    }
                }
            }

            public static final ListenerManager<FillInPriorityCalculator> PRIORITY_CALCULATORS = new ListenerManager<>();
            public final int weight;//calc order

            public int compareTo(FillInPriorityCalculator o) {
                return Integer.compare(weight, o.weight);
            }

            public abstract int getPriorityFrom(PriorityCalculatorContext calculatorContext);

            @AllArgsConstructor
            public enum DefaultImpl {
                FILTERED_FIRST(new FillInPriorityCalculator(100) {
                    @Override
                    public int getPriorityFrom(PriorityCalculatorContext calculatorContext) {
                        return getPriorityMakesFilteredFirst(calculatorContext);
                    }
                }),
                EMPTY_JAR_AFTER_NOT_EMPTY(new FillInPriorityCalculator(200) {
                    @Override
                    public int getPriorityFrom(PriorityCalculatorContext calculatorContext) {
                        return getPriorityMakesEmptyJarAfterNotEmpty(calculatorContext);
                    }
                }),
                WONT_OVERFLOW_INTO_VOID(new FillInPriorityCalculator(300) {
                    @Override
                    public int getPriorityFrom(PriorityCalculatorContext calculatorContext) {

                        return getPriorityMakesOverflowDestroyedAfter(calculatorContext);
                    }
                }),
                ;

                public final FillInPriorityCalculator calculator;

                public static int getPriorityMakesOverflowDestroyedAfter(PriorityCalculatorContext calculatorContext) {
                    if (calculatorContext.getMaybeEssentiaJarToFillIn() instanceof IEssentiaTransportOutBlockEntity outBE && outBE instanceof IEssentiaOverflowDestroyerBlockEntity destroyerBlockEntity) {
                        var expectedAspect = calculatorContext.getExpectedAspectToInsert();
                        var dir = calculatorContext.getInteractionDirection();
                        if (destroyerBlockEntity.willDestroy(expectedAspect)) {
                            return Integer.MAX_VALUE;
                        }
                        return 0;
//                        int remainingSpace =
//                                calculatorContext.inBE.getEssentiaAmount(dir)
//                                        - outBE.getEssentiaAmount(dir);
//                        return remainingSpace - expectAmount;
                    }
                    return Integer.MIN_VALUE;
                }

                public static int getPriorityMakesEmptyJarAfterNotEmpty(PriorityCalculatorContext calculatorContext) {
                    if (calculatorContext.getMaybeEssentiaJarToFillIn() instanceof IEssentiaTransportOutBlockEntity outBE) {
                        var dir = calculatorContext.getInteractionDirection();
                        var type = outBE.getEssentiaType(dir);
                        if (type.isEmpty() || outBE.getEssentiaAmount(dir) == 0) {
                            if (outBE instanceof IEssentiaOverflowDestroyerBlockEntity) {//nonVoid first
                                return 100;
                            }
                            return 0;
                        }
                    }
                    return -100;
                }

                public static int getPriorityMakesFilteredFirst(PriorityCalculatorContext calculatorContext) {
                    if (calculatorContext.getMaybeEssentiaJarToFillIn() instanceof IAspectFilterAccessibleBlockEntity filterAccessible) {
                        return filterAccessible.getAspectFilter().isEmpty() ? 0 : 1;
                    }
                    return -1;
                }
            }

            public static void init() {
                for (var priorityCalcEnum : DefaultImpl.values()) {
                    PRIORITY_CALCULATORS.registerListener(priorityCalcEnum.calculator);
                }
            }
        }

        public final int[] priorities = new int[FillInPriorityCalculator.PRIORITY_CALCULATORS.getListeners().size()];

        public void calculateFor(FillInPriorityCalculator.PriorityCalculatorContext calculatorContext) {
            var listeners = FillInPriorityCalculator.PRIORITY_CALCULATORS.getListeners();
            for (int i = 0; i < listeners.size(); i++) {
                priorities[i] = listeners.get(i).getPriorityFrom(calculatorContext);
            }
        }

        @Override
        public int compareTo(@NotNull GolemEssentiaJarSearchingHelper.GolemSearchingEssentiaJarPriority o) {
            if (this.priorities.length != o.priorities.length) {
                throw new IllegalArgumentException("priorities length not same!did anyone added calculator during game?");
            }
            for (int i = 0; i < this.priorities.length; i++) {
                if (this.priorities[i] != o.priorities[i]) {
                    return Integer.compare(this.priorities[i], o.priorities[i]);
                }
            }
            return 0;
        }

        public static void init() {
            FillInPriorityCalculator.init();
        }
    }

    public static void init() {
        GolemSearchingEssentiaJarPriority.init();
    }

    @NoArgsConstructor
    public static class BestEssentiaContainerResultReceiver implements GolemHelper.Resettable {
        @Getter
        protected boolean foundResult;
        @Getter
        protected @Nullable BlockEntity be;
        @Getter
        protected @Nullable Direction dir;
        protected @NotNull BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);//Integer.MAX is like "null"
        //maybe we'll adapt some block contains essentia but not blockEntity?who knows

        public @NotNull BlockPos getBlockPos() {
            return blockPos;
        }

        public void reset(){
            be = null;
            blockPos.set(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
            dir = null;
            foundResult = false;
        }
    }
    //TODO:[maybe wont finished]Same searching but for getting aspect?maybe we need a essentia gather core.(TBH i dont want this part)
    public static void getBestEssentiaContainerWithRoomForConnectedJars(
            BaseGolemEntity golem,
            BlockPos pos,//marker pos
            Aspect expectedAspectToInsert,
            int expectedAmountToInsert,
            BestEssentiaContainerResultReceiver resultReceiver
    ) {
        var level = golem.level();
        var deque = new BlockSearchDeque();
        var checkingPos = new BlockPos.MutableBlockPos();
        GolemSearchingEssentiaJarPriority checkingPriority = new GolemSearchingEssentiaJarPriority();
        MutablePriorityCalculatorContext calculatorContext = null;
        deque.addIfNotChecked(pos.getX(), pos.getY(), pos.getZ());

//        Direction dirForBest = Direction.UP;
        GolemSearchingEssentiaJarPriority bestPriority = null;
        double distSqrFromHomeForBest = Double.POSITIVE_INFINITY;


        double rangeSqrSqr = golem.getHomeRange();
        rangeSqrSqr *= rangeSqrSqr;
        rangeSqrSqr *= rangeSqrSqr;//azanor wrote this(distNotSqr < rangeSqr)
        // probably a bug but i want to keep it so we have a large range.
        // vote in democracy to change?


        while (!deque.isEmpty()) {
            int currentX = deque.popX();
            int currentY = deque.popY();
            int currentZ = deque.popZ();
            var be = level.getBlockEntity(checkingPos.set(currentX, currentY, currentZ));
            if (be instanceof IEssentiaTransportInBlockEntity inBE) {
                if (calculatorContext == null) {
                    calculatorContext = new MutablePriorityCalculatorContext(
                            inBE,expectedAspectToInsert,expectedAmountToInsert,golem
                    );
                }

                calculatorContext.setInBE(inBE);
                calculatorContext.setExpectedAspectToInsert(expectedAspectToInsert);
                calculatorContext.setExpectAmountToInsert(expectedAmountToInsert);
                calculatorContext.setGolem(golem);

                for (var checkingDir:Direction.values()) {
                    calculatorContext.setInteractionDirection(checkingDir);
                    if (inBE.canInputFrom(checkingDir)
                            && !(inBE.isFullFor(expectedAspectToInsert, checkingDir))
                    ) {
                        checkingPriority.calculateFor(calculatorContext);
                        var distSqrHome = be.getBlockPos().distSqr(golem.getRestrictCenter());
                        if (distSqrHome < rangeSqrSqr
                                && (bestPriority == null
                                || (bestPriority.compareTo(checkingPriority) >= 0
                                && distSqrHome < distSqrFromHomeForBest))
                        ) {
                            {
                                var priorityCache = bestPriority;
                                bestPriority = checkingPriority;
                                checkingPriority = priorityCache;
                            }//exchange value,we're going to override checkingPriority next run
                            distSqrFromHomeForBest = distSqrHome;
                            resultReceiver.blockPos.set(currentX, currentY, currentZ);
                            resultReceiver.be = be;
                            resultReceiver.dir = checkingDir;
                            resultReceiver.foundResult = true;
                        }
                    }
                    for (var dir : Direction.values()) {
                        deque.addIfNotChecked(
                                currentX + dir.getStepX(),
                                currentY + dir.getStepY(),
                                currentZ + dir.getStepZ()
                        );
                    }
                }
            }
        }
    }
    //
}
