package thaumcraft.common.blocks.crafted.ownedblock;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableOwnedBlock;
import thaumcraft.common.tiles.crafted.OwnedBlockEntity;

import java.util.ArrayList;
import java.util.List;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static com.linearity.opentc4.utils.consts.EntityTypeTests.ENTITY_TEST;

public class ArcanePressurePlateBlock extends PressurePlateBlock
        implements IWandInteractableOwnedBlock {
    public ArcanePressurePlateBlock(Sensitivity sensitivity, Properties properties, BlockSetType blockSetType) {
        super(sensitivity, properties, blockSetType);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(POWERED, false)
                        .setValue(SETTING,TRIGGER_BY_EVERYTHING)
        );
    }

    public ArcanePressurePlateBlock() {
        this(
                Sensitivity.EVERYTHING,
                Properties.of()
                        .strength(-1,999)
                        .sound(SoundType.WOOD),
                BlockSetType.IRON);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SETTING);
    }

    public static final int TRIGGER_BY_EVERYTHING = 0;
    public static final int TRIGGER_EXCEPT_OWNER = 1;
    public static final int TRIGGER_ONLY_OWNER = 2;
    public static final IntegerProperty SETTING = IntegerProperty.create("setting", 0, 2);


    @Override
    protected int getSignalStrength(Level level, BlockPos blockPos) {
        int settings = level.getBlockState(blockPos.below()).getValue(SETTING);
        if (!(LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof OwnedBlockEntity ownedBlockEntity)){
            return 0;
        }
        return hasValidEntity(level, TOUCH_AABB.move(blockPos), settings,ownedBlockEntity)? 15 : 0;
    }

    protected boolean hasValidEntity(Level level, net.minecraft.world.phys.AABB aABB, int settings, OwnedBlockEntity ownedBE) {
        List<Entity> list = new ArrayList<>(1);
        level.getEntities(
                ENTITY_TEST,
                aABB,
                EntitySelector.NO_SPECTATORS
                        .and(entity -> !entity.isIgnoringBlockTriggers())
                        .and(entity -> {
                            if (settings == TRIGGER_BY_EVERYTHING){
                                return true;
                            }
                            boolean ownedFlag = false;
                            if (entity instanceof Player player){
                                if (ownedBE.playerOwnThis(player)){
                                    ownedFlag = true;
                                }
                            }
                            if (settings == TRIGGER_EXCEPT_OWNER){
                                return !ownedFlag;
                            }
                            if (settings == TRIGGER_ONLY_OWNER){
                                return ownedFlag;
                            }
                            throw new IllegalStateException("Unexpected trigger: " + settings);
                        }),
                list,
                1
        );
        return !list.isEmpty();
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()){
            if (LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof OwnedBlockEntity ownedBlockEntity){
                int setting = blockState.getValue(SETTING);
                setting = (setting+1)%3;
                if (player != null){
                    if (setting == TRIGGER_BY_EVERYTHING) {
                        player.displayClientMessage(Component.translatable("thaumcraft.use_block.arcane_pressure_plate_setting.0"),true);
                    }
                    if (setting == TRIGGER_EXCEPT_OWNER) {
                        player.displayClientMessage(Component.translatable("thaumcraft.use_block.arcane_pressure_plate_setting.1"),true);
                    }
                    if (setting == TRIGGER_ONLY_OWNER) {
                        player.displayClientMessage(Component.translatable("thaumcraft.use_block.arcane_pressure_plate_setting.2"),true);
                    }
                    level.playSound(null,blockPos, SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value() , SoundSource.BLOCKS, 0.1F, 0.9F);
                    ownedBlockEntity.setBlockStateAndUpdate(blockState.setValue(SETTING,setting));
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        setPlacedOwnedBlockBy(level, blockPos, blockState, livingEntity, itemStack);
    }
}
