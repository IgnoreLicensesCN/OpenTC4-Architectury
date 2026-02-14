package thaumcraft.common.tiles.crafted;

import com.linearity.colorannotation.annotation.RGBColor;
import com.linearity.opentc4.Color;
import com.linearity.opentc4.simpleutils.bauble.BaubleUtils;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
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
import thaumcraft.api.visnet.IVisNodeDetectableItem;
import thaumcraft.api.visnet.VisNetNodeBlockEntity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.crafted.visnet.VisNetRelayBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.List;
//TODO:BER(just render model),TileMagicWorkbenchCharger(and interface to charge wand inside.)
public class VisNetRelayBlockEntity extends VisNetNodeBlockEntity {
    public VisNetRelayBlockEntity(BlockEntityType<VisNetRelayBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
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
        if (Platform.getEnvironment() == Env.CLIENT){
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

    protected int pulse = 0;
    public float pRed = 0.5F;
    public float pGreen = 0.5F;
    public float pBlue = 0.5F;

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
                    if (!stack.isEmpty() && stack.getItem() instanceof IVisNodeDetectableItem detectable) {
                        detectable.onVisNodeNearby(this, stack);
                    }
                }
                //equipped(vanilla)
                for (ItemStack stack : player.getArmorSlots()) {
                    if (!stack.isEmpty() && stack.getItem() instanceof IVisNodeDetectableItem detectable) {
                        detectable.onVisNodeNearby(this, stack);
                    }
                }
                //equipped(bauble slots)
                BaubleUtils.forEachBauble(
                        player, IVisNodeDetectableItem.class, (slot, stack, item) -> {
                            item.onVisNodeNearby(this,stack);
                            return false;
                        }
                );
            });
        }
    }
    public void clientCheckParent(){
        var selfPos = this.getBlockPos();
        var level = this.getLevel();
        if (level == null){
            return;
        }
        if (this.needToLoadParent) {
            var parent = getParent();
            var parentPos = BlockPos.ZERO;
            if (parent != null) {
                parentPos = parent.getBlockPos();
            }
            if (parentPos.equals(selfPos)) {
                this.removeParent();
            } else {
                if (
                        !CommonUtils.isChunkLoaded(level,parentPos)
                ){
                    return;
                }
                if (level.getBlockEntity(parentPos) instanceof VisNetNodeBlockEntity node) {
                    this.setParent(node);
                }
            }
            this.needToLoadParent = false;
            this.parentChanged();
        }
    }
    public void clientTick() {
        if (Platform.getEnvironment() != Env.CLIENT){
            return;
        }
        var level = getLevel();
        if (level == null) {
            return;
        }
        var selfPos = this.getBlockPos();
        clientCheckParent();
        var parent = this.getParent();
        if (parent != null) {
            var parentPos = parent.getBlockPos().getCenter();
            var selfCenterPos = selfPos.getCenter();
            double xx = parentPos.x();
            double yy = parentPos.y();
            double zz = parentPos.z();
            Direction parentFacing = null;
            if (this.getParent() instanceof VisNetRelayBlockEntity relay) {
                parentFacing = relay.getBlockState().getValue(VisNetRelayBlock.FACING);
            }

            Direction thisFacing = this.getBlockState().getValue(VisNetRelayBlock.FACING);
            if (this.level instanceof ClientLevel clientLevel){
                ClientFXUtils.updateBeamPower(
                        this,
                        clientLevel,
                        xx - (parentFacing==null?0:parentFacing.getStepX()) * 0.05,
                        yy - (parentFacing==null?0:parentFacing.getStepY()) * 0.05,
                        zz - (parentFacing==null?0:parentFacing.getStepZ()) * 0.05,
                        selfCenterPos.x() - thisFacing.getStepX() * 0.05,
                        selfCenterPos.y() - thisFacing.getStepY() * 0.05,
                        selfCenterPos.z() - thisFacing.getStepZ() * 0.05,
                        this.pRed,
                        this.pGreen,
                        this.pBlue,
                        this.pulse > 0
                );
            }
        }

        if (this.pRed < 1.0F) {
            this.pRed += 0.025F;
        }
        if (this.pRed > 1.0F) {
            this.pRed = 1.0F;
        }

        if (this.pGreen < 1.0F) {
            this.pGreen += 0.025F;
        }

        if (this.pGreen > 1.0F) {
            this.pGreen = 1.0F;
        }

        if (this.pBlue < 1.0F) {
            this.pBlue += 0.025F;
        }

        if (this.pBlue > 1.0F) {
            this.pBlue = 1.0F;
        }
        if (this.pulse > 0) {
            --this.pulse;
        }
    }

    @Override
    public void triggerConsumeEffect(Aspect aspect) {
        addPulse();
    }

    @Override
    public int getAttunement() {
        return getBlockState().getValue(VisNetRelayBlock.COLOR);
    }

    public void addPulse(){
        this.pulse = 5;
        var colorIndex = getBlockState().getValue(VisNetRelayBlock.COLOR)-1;
        if (colorIndex >= 0){

            Color c = new Color(colors[colorIndex]);
            this.pRed = (float)c.getRed() / 255.0F;
            this.pGreen = (float)c.getGreen() / 255.0F;
            this.pBlue = (float)c.getBlue() / 255.0F;

            for(var vr = this.getParent();
                vr instanceof VisNetRelayBlockEntity relay && relay.pulse == 0;
                vr = vr.getParent()
            ) {
                relay.pRed = this.pRed;
                relay.pGreen = this.pGreen;
                relay.pBlue = this.pBlue;
                relay.pulse = 5;
            }
        }
    }
}
