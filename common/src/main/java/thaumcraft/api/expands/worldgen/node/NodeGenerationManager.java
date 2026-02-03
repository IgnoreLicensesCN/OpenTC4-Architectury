package thaumcraft.api.expands.worldgen.node;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.world.level.WorldGenLevel;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.expands.worldgen.node.listeners.*;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static thaumcraft.api.expands.worldgen.node.consts.CreateNodeListeners.DEFAULT_NODE_CREATOR;
import static thaumcraft.api.expands.worldgen.node.consts.DoGenerateConditions.*;
import static thaumcraft.api.expands.worldgen.node.consts.NodeAspectGenerators.DEFAULT_ASPECT_GENERATOR;
import static thaumcraft.api.expands.worldgen.node.consts.NodeCoordinatesPickers.defaultPicker;
import static thaumcraft.api.expands.worldgen.node.consts.NodeModifierPickers.DEFAULT_NODE_MODIFIER_PICKER;
import static thaumcraft.api.expands.worldgen.node.consts.NodeTypePickers.DEFAULT_NODE_TYPE_PICKER;

public class NodeGenerationManager {
    public static List<Aspect> basicAspects = new ArrayList<>();
    public static List<Aspect> complexAspects = new ArrayList<>();

    public static final ListenerManager<DoGenerateCondition> doGenerateConditionsManager = new ListenerManager<>();
    public static final ListenerManager<PickNodeCoordinatesListener> pickNodeCoordinatesListenerManager = new ListenerManager<>();
    public static final ListenerManager<NodeTypePicker> nodeTypePickerManager = new ListenerManager<>();
    public static final ListenerManager<NodeModifierPicker> nodeModifierPickerManager = new ListenerManager<>();
    public static final ListenerManager<NodeAspectGenerator> nodeAspectGeneratorManager = new ListenerManager<>();
    public static final ListenerManager<CreateNodeListener> createNodeListenerManager = new ListenerManager<>();

    public static void init() {
        doGenerateConditionsManager.registerListener(ConfigCondition);
        doGenerateConditionsManager.registerListener(ConfigRandomCondition);
        doGenerateConditionsManager.registerListener(AuraGenChecker);

        pickNodeCoordinatesListenerManager.registerListener(defaultPicker);

        nodeTypePickerManager.registerListener(DEFAULT_NODE_TYPE_PICKER);

        nodeModifierPickerManager.registerListener(DEFAULT_NODE_MODIFIER_PICKER);

        nodeAspectGeneratorManager.registerListener(DEFAULT_ASPECT_GENERATOR);

        createNodeListenerManager.registerListener(DEFAULT_NODE_CREATOR);
    }

    /**
     * will be called when generating nodes
     */
    public static boolean generateWildNodes(Level world, RandomSource random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
        for (DoGenerateCondition condition : doGenerateConditionsManager.getListeners()) {
            if (!condition.check(world, random, chunkX, chunkZ, auraGen, newGen)) {
                return false;
            }
        }

        List<PickNodeCoordinateContext> expectingNodeLocations = new ArrayList<>();
        for (PickNodeCoordinatesListener listener : pickNodeCoordinatesListenerManager.getListeners()) {
            expectingNodeLocations.addAll(Arrays.asList(listener.pickNodeCoordinates(world, random, chunkX, chunkZ, auraGen, newGen)));
        }

        if (expectingNodeLocations.isEmpty()) {
            return false;
        } else {
            for (PickNodeCoordinateContext node : expectingNodeLocations) {
                createRandomNodeAt(world, node.pos, random, node.silverwood, node.eerie, node.small);
            }
            return true;
        }

    }
    public static void tryInitAspects(){
        if (basicAspects.isEmpty()) {
            for (Aspect as : Aspects.ALL_ASPECTS.values()) {
                if (as.getComponents() != null) {
                    complexAspects.add(as);
                } else {
                    basicAspects.add(as);
                }
            }
        }
    }
    public static void createRandomNodeAt(Level world, BlockPos pos, RandomSource random, boolean silverwood, boolean eerie, boolean small) {
        tryInitAspects();
        NodeType type = NodeType.NORMAL;
        for (NodeTypePicker picker : nodeTypePickerManager.getListeners()) {
            type = picker.onPickingNodeType(world, pos, random, silverwood, eerie, small, type);
        }

        NodeModifier modifier = null;
        for (NodeModifierPicker picker : nodeModifierPickerManager.getListeners()) {
            modifier = picker.onPickingNodeModifier(world, pos, random, silverwood, eerie, small, modifier);
        }

        AspectList nodeAspects = new AspectList();
        for (NodeAspectGenerator generator : nodeAspectGeneratorManager.getListeners()) {
            nodeAspects = generator.getNodeAspects(world, pos, random, silverwood, eerie, small,nodeAspects, type,modifier);
        }

        createNodeAt(world,new CreateNodeContext(type,modifier,pos,nodeAspects));
    }

    public static void createRandomNodeAt(WorldGenLevel world, BlockPos pos, RandomSource random, boolean silverwood, boolean eerie, boolean small) {
        tryInitAspects();
        NodeType type = NodeType.NORMAL;
        for (NodeTypePicker picker : nodeTypePickerManager.getListeners()) {
            type = picker.onPickingNodeType(world, pos, random, silverwood, eerie, small, type);
        }

        NodeModifier modifier = null;
        for (NodeModifierPicker picker : nodeModifierPickerManager.getListeners()) {
            modifier = picker.onPickingNodeModifier(world, pos, random, silverwood, eerie, small, modifier);
        }

        AspectList nodeAspects = new AspectList();
        for (NodeAspectGenerator generator : nodeAspectGeneratorManager.getListeners()) {
            nodeAspects = generator.getNodeAspects(world, pos, random, silverwood, eerie, small,nodeAspects, type,modifier);
        }

        createNodeAt(world,new CreateNodeContext(type,modifier,pos,nodeAspects));
    }

    public static void createNodeAt(Level world,CreateNodeContext context) {
        for (CreateNodeListener listener : createNodeListenerManager.getListeners()) {
            if (listener.onCreateNode(world, context)) {
                break;
            }
        }
    }
    public static void createNodeAt(WorldGenLevel world, CreateNodeContext context) {
        for (CreateNodeListener listener : createNodeListenerManager.getListeners()) {
            if (listener.onCreateNode(world, context)) {
                break;
            }
        }
    }
}
