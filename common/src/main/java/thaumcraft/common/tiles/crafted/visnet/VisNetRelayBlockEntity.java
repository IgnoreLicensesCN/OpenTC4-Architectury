package thaumcraft.common.tiles.crafted.visnet;

import com.google.common.collect.MapMaker;
import com.linearity.colorannotation.annotation.RGBColor;
import com.linearity.opentc4.Color;
import com.linearity.opentc4.simpleutils.bauble.BaubleUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import tc4tweak.CommonUtils;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.visnet.IVisNetNodeDetectableItem;
import thaumcraft.api.visnet.VisNetNodeBlockEntity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.crafted.noderelated.visnet.VisNetRelayBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.List;
import java.util.Map;

//TODO:BER(just render model),TileMagicWorkbenchCharger(and interface to charge wand inside.)
public class VisNetRelayBlockEntity extends VisNetNodeBlockEntity {
    public VisNetRelayBlockEntity(BlockEntityType<? extends VisNetRelayBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public VisNetRelayBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.VIS_RELAY, blockPos, blockState);
    }

    public int getRange() {
        return 8;
    }
    public boolean isSource() {
        return false;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level != null && this.level.isClientSide()) {
            ClientFXUtils.clearBeamPower(this);
        }
    }


    //client fields
    public static final @RGBColor int[] colors = new int[]{
            0xFFFF7E,
            0xFF3C01,
            0x0090FF,
            0x00A000,
            0xEECCFF,
            0x555577,
    };

    //then server-side wont create these 4 fields
    public static class ClientTickContext {
        private int pulse = 0;
        private float pRed = 0.5F;
        private float pGreen = 0.5F;
        private float pBlue = 0.5F;
        private static final Map<VisNetRelayBlockEntity,ClientTickContext> contexts =
                new MapMaker().weakKeys().makeMap();

        public static void clientCheckParent(VisNetRelayBlockEntity visNetRelayBlockEntity){
            var selfPos = visNetRelayBlockEntity.getBlockPos();
            var level = visNetRelayBlockEntity.getLevel();
            if (level == null){
                return;
            }
            if (visNetRelayBlockEntity.needToLoadParent) {
                var parent = visNetRelayBlockEntity.getParent();
                var parentPos = BlockPos.ZERO;
                if (parent != null) {
                    parentPos = parent.getBlockPos();
                }
                if (parentPos.equals(selfPos)) {
                    visNetRelayBlockEntity.removeParent();
                } else {
                    if (
                            !CommonUtils.isChunkLoaded(level,parentPos)
                    ){
                        return;
                    }
                    if (level.getBlockEntity(parentPos) instanceof VisNetNodeBlockEntity node) {
                        visNetRelayBlockEntity.setParent(node);
                    }
                }
                visNetRelayBlockEntity.needToLoadParent = false;
                visNetRelayBlockEntity.parentChanged();
            }
        }
        public static void clientTick(VisNetRelayBlockEntity visNetRelayBlockEntity) {

            var level = visNetRelayBlockEntity.getLevel();
            if (level == null) {
                return;
            }
            var context = contexts.computeIfAbsent(visNetRelayBlockEntity,be -> new ClientTickContext());
            var selfPos = visNetRelayBlockEntity.getBlockPos();
            clientCheckParent(visNetRelayBlockEntity);
            var parent = visNetRelayBlockEntity.getParent();
            if (parent != null) {
                var parentPos = parent.getBlockPos().getCenter();
                var selfCenterPos = selfPos.getCenter();
                double xx = parentPos.x();
                double yy = parentPos.y();
                double zz = parentPos.z();
                Direction parentFacing = null;
                if (visNetRelayBlockEntity.getParent() instanceof VisNetRelayBlockEntity relay) {
                    parentFacing = relay.getBlockState().getValue(VisNetRelayBlock.FACING);
                }

                Direction thisFacing = visNetRelayBlockEntity.getBlockState().getValue(VisNetRelayBlock.FACING);
                if (visNetRelayBlockEntity.level instanceof ClientLevel clientLevel){
                    ClientFXUtils.updateBeamPower(
                            visNetRelayBlockEntity,
                            clientLevel,
                            xx - (parentFacing==null?0:parentFacing.getStepX()) * 0.05,
                            yy - (parentFacing==null?0:parentFacing.getStepY()) * 0.05,
                            zz - (parentFacing==null?0:parentFacing.getStepZ()) * 0.05,
                            selfCenterPos.x() - thisFacing.getStepX() * 0.05,
                            selfCenterPos.y() - thisFacing.getStepY() * 0.05,
                            selfCenterPos.z() - thisFacing.getStepZ() * 0.05,
                            context.pRed,
                            context.pGreen,
                            context.pBlue,
                            context.pulse > 0
                    );
                }
            }

            if (context.pRed < 1.0F) {
                context.pRed += 0.025F;
            }
            if (context.pRed > 1.0F) {
                context.pRed = 1.0F;
            }
            if (context.pGreen < 1.0F) {
                context.pGreen += 0.025F;
            }
            if (context.pGreen > 1.0F) {
                context.pGreen = 1.0F;
            }

            if (context.pBlue < 1.0F) {
                context.pBlue += 0.025F;
            }

            if (context.pBlue > 1.0F) {
                context.pBlue = 1.0F;
            }
            if (context.pulse > 0) {
                --context.pulse;
            }
        }

        public static void addPulse(VisNetRelayBlockEntity visNetRelayBlockEntity) {
            var context = contexts.computeIfAbsent(visNetRelayBlockEntity,be -> new ClientTickContext());
            context.pulse = 5;
            var colorIndex = visNetRelayBlockEntity.getBlockState().getValue(VisNetRelayBlock.COLOR)-1;
            if (colorIndex >= 0){

                Color c = new Color(colors[colorIndex]);
                context.pRed = (float)c.getRed() / 255.0F;
                context.pGreen = (float)c.getGreen() / 255.0F;
                context.pBlue = (float)c.getBlue() / 255.0F;

                var current = visNetRelayBlockEntity.getParent();
                while (current instanceof VisNetRelayBlockEntity relay) {
                    var ctxCurrent = contexts.computeIfAbsent(relay, be -> new ClientTickContext());
                    if (ctxCurrent.pulse != 0) break;
                    ctxCurrent.pRed = context.pRed;
                    ctxCurrent.pGreen = context.pGreen;
                    ctxCurrent.pBlue = context.pBlue;
                    ctxCurrent.pulse = 5;
                    current = relay.getParent();
                }
            }
        }
    }

    protected boolean needToLoadParent = false;
    public void tick(){
        super.tick();
        if (nodeCounter% 20 == 0){
            if (level == null){return;}
            AABB box = new AABB(worldPosition)
                    .inflate(5.0D);

            List<Player> players = level.getEntitiesOfClass(
                    Player.class,
                    box
            );
            //since itemAmulet ticks(when wearing) and this ticks and both detects range,why not finish it here?
            players.forEach(player -> {
                //hands
                for (ItemStack stack : List.of(player.getMainHandItem(), player.getOffhandItem())) {
                    if (!stack.isEmpty() && stack.getItem() instanceof IVisNetNodeDetectableItem detectable) {
                        detectable.onVisNodeNearby(this, stack);
                    }
                }
                //equipped(vanilla)
                for (ItemStack stack : player.getArmorSlots()) {
                    if (!stack.isEmpty() && stack.getItem() instanceof IVisNetNodeDetectableItem detectable) {
                        detectable.onVisNodeNearby(this, stack);
                    }
                }
                //equipped(bauble slots)
                BaubleUtils.forEachBauble(
                        player, IVisNetNodeDetectableItem.class, (slot, stack, item) -> {
                            item.onVisNodeNearby(this,stack);
                            return false;
                        }
                );
            });
        }
    }
    public void clientTick() {
        if (!(this.level != null && this.level.isClientSide())) {
            return;
        }
        ClientTickContext.clientTick(this);
    }

    @Override
    public void triggerConsumeEffect(Aspect aspect) {
        if (this.level == null){return;}
        if (!this.level.isClientSide) {
            return;
        }
        ClientTickContext.addPulse(this);
    }


    @Override
    public int getAttunement() {
        return getBlockState().getValue(VisNetRelayBlock.COLOR);
    }

}
