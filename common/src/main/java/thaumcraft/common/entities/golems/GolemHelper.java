package thaumcraft.common.entities.golems;

import com.linearity.opentc4.annotations.ReturnValueFieldOfSelf;
import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportBlockEntity;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.ai.goals.golem.itemstackinteraction.GolemPlaceItemStackToHomeGoalWithItemStackTickMark;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.junkbox.TileEssentiaReservoir;
import thaumcraft.common.tiles.junkbox.TileJarFillable;
import thaumcraft.common.tiles.junkbox.TileJarFillableVoid;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static com.linearity.opentc4.Consts.getEmptyWeakRef;
import static com.linearity.opentc4.mixinaccessors.IObjectCacheableFakeFieldOwner.opentc4$getCachedObjectFrom;
import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class GolemHelper {
    public static final double ADJACENT_RANGE = 4.0F;
    static HashMap<String, TileJarFillable> jarlist = new HashMap<>();
    private static ArrayList<Integer> reggedLiquids = null;
    static ArrayList<SortingItemTimeout> itemTimeout = new ArrayList<>();

    public static ArrayList<IInventory> getMarkedContainers(World world, EntityGolemBase golem) {
        ArrayList<IInventory> results = new ArrayList<>();

        for (Marker marker : golem.getMarkers()) {
            TileEntity te = world.getTileEntity(marker.x, marker.y, marker.z);
            if (marker.dim == world.dimension() && te instanceof IInventory) {
                results.add((IInventory) te);
                if (InventoryUtils.getDoubleChest(te) != null) {
                    results.add(InventoryUtils.getDoubleChest(te));
                }
            }
        }

        return results;
    }

    public interface Resettable {
        void reset();
    }

    //usage like fake field
    public static class ContainerInfo {
        public @NotNull WeakReference<Container> containerRef = getEmptyWeakRef();
        public @NotNull WeakReference<BlockEntity> blockEntityRef = getEmptyWeakRef();
        public Marker marker;

        public void reset() {
            this.containerRef = getEmptyWeakRef();
            this.blockEntityRef = getEmptyWeakRef();
            this.marker = null;
        }

        public void setTo(ContainerInfo info) {
            this.containerRef = info.containerRef;
            this.blockEntityRef = info.blockEntityRef;
            this.marker = info.marker;
        }

        //         public boolean validate(){
//            var container = this.container == null ? null : this.container.get();
//            var containerBE = this.blockEntity == null ? null : this.blockEntity.get();
//            if (container == null || containerBE == null || containerBE.isRemoved()) {
//               return false;
//            }
//            return true;
//         }
        public boolean verify() {
            var containerInstance = containerRef.get();
            if (containerInstance == null) {
                return false;
            }
            var beInstance = blockEntityRef.get();
            return beInstance != null && !beInstance.isRemoved();
        }

        public boolean verify(Container container, BlockEntity blockEntity, Marker marker) {
            return container != null && container == blockEntity && marker != null && !blockEntity.isRemoved();
        }
    }

    public static class MarkedContainerAndMarkerIterator implements Iterator<ContainerInfo>, Resettable {

        protected final BaseGolemEntity golem;
        protected @Nullable Iterator<Marker> markers;
        protected @NotNull ContainerInfo resultCache = new ContainerInfo();
        protected @NotNull ContainerInfo resultCacheOut = new ContainerInfo();
        public @NotNull BiPredicate<BaseGolemEntity /*the one above*/ , BlockEntity /*impl Container*/> containerPredicate;

        public MarkedContainerAndMarkerIterator(
                BaseGolemEntity golem,
                @NotNull BiPredicate<BaseGolemEntity /*the one above*/ ,
                        BlockEntity /*impl Container*/> containerPredicate
        ) {
            this.golem = golem;
            this.containerPredicate = containerPredicate;
            reset();
        }

        @Override
        public void reset() {
            markers = null;
            resultCache.containerRef = getEmptyWeakRef();
            resultCache.blockEntityRef = getEmptyWeakRef();
            resultCache.marker = null;
        }

        protected void initMarkersIfNull() {
            if (this.markers == null) {
                this.markers = golem.markers.iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (resultCache.verify()) {
                return true;
            }
            var level = golem.level();
            var levelResLoc = level.dimension().location();
            initMarkersIfNull();
            assert markers != null;
            while (this.markers.hasNext()) {
                var marker = this.markers.next();
                if (Objects.equals(marker.dim, levelResLoc)) {
                    var checkingBE = getExistingBlockEntity(level, marker.pos.relative(marker.side.getOpposite()));
                    if (checkingBE instanceof Container container && !checkingBE.isRemoved()) {
                        if (containerPredicate.test(golem, checkingBE)) {
                            resultCache.containerRef = new WeakReference<>(container);
                            resultCache.blockEntityRef = new WeakReference<>(checkingBE);
                            resultCache.marker = marker;
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        @ReturnValueFieldOfSelf
        public ContainerInfo next() {
            if (!hasNext()) {
                throw new IllegalStateException("call hasNext() first!");
            }
            this.resultCacheOut.setTo(resultCache);
            resultCache.reset();
            return resultCacheOut;
        }
    }

    public static class MarkedContainerIterator implements Iterator<Container>, Resettable {
        protected final BaseGolemEntity golem;
        protected @Nullable Iterator<Marker> markers;
        protected Container nextContainerCache;
        public @NotNull BiPredicate<BaseGolemEntity /*the one above*/ , BlockEntity /*impl Container*/> containerPredicate;

        public MarkedContainerIterator(BaseGolemEntity golem, @NotNull BiPredicate<BaseGolemEntity /*the one above*/ , BlockEntity /*impl Container*/> containerPredicate) {
            this.golem = golem;
            this.containerPredicate = containerPredicate;
            reset();
        }

        @Override
        public void reset() {
            markers = null;
        }

        protected void initMarkersIfNull() {
            if (this.markers == null) {
                this.markers = golem.markers.iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (nextContainerCache != null) {
                return true;
            }
            var level = golem.level();
            var levelResLoc = level.dimension().location();
            initMarkersIfNull();
            assert markers != null;
            while (this.markers.hasNext()) {
                var marker = this.markers.next();
                if (Objects.equals(marker.dim, levelResLoc)) {
                    var checkingBE = getExistingBlockEntity(level, marker.pos.relative(marker.side.getOpposite()));
                    if (checkingBE instanceof Container container) {
                        if (containerPredicate.test(golem, checkingBE)) {
                            nextContainerCache = container;
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public Container next() {
            if (nextContainerCache == null && !hasNext()) {
                throw new IllegalStateException("No more containers!call hasNext() first!");
            }
            var result = nextContainerCache;
            nextContainerCache = null;
            return result;
        }
    }

    public static void getMarkedContainers(
            Collection<Container> results,
            BaseGolemEntity golem,
            @NotNull BiPredicate<BaseGolemEntity /*the one above*/ , BlockEntity /*impl Container*/> containerPredicate) {
        var level = golem.level();
        var levelResLoc = level.dimension().location();
        for (var marker : golem.markers) {
            if (Objects.equals(marker.dim, levelResLoc)) {
                var checkingBE = getExistingBlockEntity(level, marker.pos.relative(marker.side.getOpposite()));
                if (checkingBE instanceof Container container) {
                    if (containerPredicate.test(golem, checkingBE)) {
                        results.add(container);
                    }
                }
            }
        }
    }

    public static void getMarkedContainersAsBlockEntity(
            Collection<BlockEntity> results,
            BaseGolemEntity golem,
            @NotNull BiPredicate<BaseGolemEntity /*the one above*/ , BlockEntity /*impl Container*/> containerPredicate) {
        var level = golem.level();
        var levelResLoc = level.dimension().location();
        for (var marker : golem.markers) {
            if (Objects.equals(marker.dim, levelResLoc)) {
                var checkingBE = getExistingBlockEntity(level, marker.pos.relative(marker.side.getOpposite()));
                if (checkingBE instanceof Container container) {
                    if (containerPredicate.test(golem, checkingBE)) {
                        results.add(checkingBE);
                    }
                }
            }
        }
    }

    public static List<Container> getMarkedContainers(
            BaseGolemEntity golem,
            @NotNull BiPredicate<BaseGolemEntity /*the one above*/, BlockEntity /*impl Container*/> containerPredicate) {
        List<Container> results = new ArrayList<>();
        getMarkedContainers(results, golem, containerPredicate);
        return results;
    }

    public static List<BlockEntity> getMarkedContainersAsBlockEntity(
            BaseGolemEntity golem,
            @NotNull BiPredicate<BaseGolemEntity /*the one above*/, BlockEntity /*impl Container*/> containerPredicate) {
        List<BlockEntity> results = new ArrayList<>();
        getMarkedContainersAsBlockEntity(results, golem, containerPredicate);
        return results;
    }

    public static final BiPredicate<BaseGolemEntity, BlockEntity> DEFAULT_DISTANCE_PREDICATE = (golem, be) -> golem.distanceToSqr(be.getBlockPos().getCenter()) < 4;

    public static void getMarkedContainersWithinDefaultRange(
            Collection<Container> results,
            BaseGolemEntity golem) {
        getMarkedContainers(results, golem, DEFAULT_DISTANCE_PREDICATE);
    }

    public static void getMarkedContainersWithinDefaultRangeAsBlockEntity(
            Collection<BlockEntity> results,
            BaseGolemEntity golem) {
        getMarkedContainersAsBlockEntity(results, golem, DEFAULT_DISTANCE_PREDICATE);
    }

    public static ByteSet getColorsMatching(BaseGolemEntity golem, ItemStack stackMatching) {
        ByteSet result = new ByteArraySet(17);
        getColorsMatching(result, golem, stackMatching);
        return result;
    }

    public static void getColorsMatching(ByteSet colors, BaseGolemEntity golem, ItemStack stackMatching) {
        var colorsOfGolem = golem.getInventoryColors();
        if (colorsOfGolem.length < golem.inventory.size()) {
            return;
        }
        boolean allEmpty = true;

        var matcher = golem.getItemStackMatcher();
        var golemItemInHand = golem.getMainHandItem();
        for (int a = 0; a < golem.inventory.size(); ++a) {
            if (!golem.inventory.get(a).isEmpty()) {
                allEmpty = false;
            }
            if (matcher.test(golemItemInHand, stackMatching)) {
                colors.add(colorsOfGolem[a]);
            }
            if (colors.size() == 17) {//color limit:any color(0) and 1~16
                break;
            }
        }

        if (allEmpty) {
            for (var colorExisting : colorsOfGolem) {
                colors.add(colorExisting);
            }
        }
    }

    public static ArrayList<ChunkCoordinates> getMarkedBlocksAdjacentToGolem(World world, EntityGolemBase golem, byte color) {
        ArrayList<ChunkCoordinates> results = new ArrayList<>();

        for (Marker marker : golem.getMarkers()) {
            if ((marker.color == color || color == -1) && (golem.level().getTileEntity(marker.x, marker.y, marker.z) == null || !(golem.level().getTileEntity(marker.x, marker.y, marker.z) instanceof IInventory)) && golem.getDistanceSq((double) marker.x + (double) 0.5F, (double) marker.y + (double) 0.5F, (double) marker.z + (double) 0.5F) < (double) 4.0F) {
                results.add(new ChunkCoordinates(marker.x, marker.y, marker.z));
            }
        }

        return results;
    }

    public static class ContainersWithRoomIterator implements Iterator<ContainerInfo>, Resettable {
        protected final BaseGolemEntity golem;
        protected MarkedContainerAndMarkerIterator markedContainerIterator;
        protected ContainerInfo resultCache = new ContainerInfo();
        protected ContainerInfo resultCacheOut = new ContainerInfo();
        //      protected byte matchingColor = INVALID_MARKER_COLOR;
        protected WeakReference<ItemStack> matchingStackRef = new WeakReference<>(ItemStack.EMPTY);

        public ContainersWithRoomIterator(BaseGolemEntity golem) {
            this.golem = golem;
            this.markedContainerIterator = new MarkedContainerAndMarkerIterator(golem, (what, ever) -> true);
        }

        public void reset() {
            markedContainerIterator.reset();
            matchingStackRef = null;
//         matchingColor = 0;
            resultCache.reset();
            resultCacheOut.reset();
        }

//      public void resetWith(byte color){
//         markedContainerIterator.reset();
//         matchingStackRef = null;
//         matchingColor = color;
//      }

        public void resetWith(ItemStack stackToMatch/*,byte color*/) {
            reset();
            markedContainerIterator.reset();
            matchingStackRef = new WeakReference<>(stackToMatch);
//         matchingColor = color;
        }

        @Override
        public boolean hasNext() {
            if (resultCache.verify()) {
                return true;
            }
            while (markedContainerIterator.hasNext()) {
                var containerInfo = markedContainerIterator.next();
                var container = containerInfo.containerRef.get();
                var containerBE = containerInfo.blockEntityRef.get();
                if (container == null || containerBE == null || containerBE.isRemoved()) {
                    continue;
                }
                var marker = containerInfo.marker;
//            if (!marker.colorEqualsFuzzy(matchingColor)){
//               continue;
//            }
                var matchingStack = matchingStackRef == null ? null : matchingStackRef.get();
                if (matchingStack == null) {
                    matchingStack = golem.getMainHandItem();
                }
                ItemStack result = InventoryUtils.placeItemStackIntoInventory(matchingStack, container, marker.side.getOpposite(), false);
                if (!ItemStack.matches(result, matchingStack)) {
                    resultCache.containerRef = containerInfo.containerRef;
                    resultCache.blockEntityRef = containerInfo.blockEntityRef;
                    resultCache.marker = marker;
                    return true;
                }
            }
            return false;
        }

        @Override
        @ReturnValueFieldOfSelf
        public ContainerInfo next() {
            if (!hasNext()) {
                throw new IllegalStateException("call hasNext() first!");
            }
            this.resultCacheOut.setTo(resultCache);
            resultCache.reset();
            return resultCacheOut;
        }
    }

    public static ArrayList<IInventory> getContainersWithRoom(World world, EntityGolemBase golem, byte color) {
        ArrayList<IInventory> results = new ArrayList<>();

        for (IInventory inventory : getMarkedContainers(world, golem)) {
            boolean hasRoom = false;

            for (Integer side : getMarkedSides(golem, (TileEntity) inventory, color)) {
                ItemStack result = InventoryUtils.placeItemStackIntoInventory(golem.getCarried(), inventory, side, false);
                if (!ItemStack.areItemStacksEqual(result, golem.itemCarried)) {
                    results.add(inventory);
                    break;
                }

//            if (InventoryUtils.getDoubleChest((TileEntity)inventory) != null) {
//               result = InventoryUtils.placeItemStackIntoInventory(golem.getCarried(), InventoryUtils.getDoubleChest((TileEntity)inventory), side, false);
//               if (!ItemStack.areItemStacksEqual(result, golem.itemCarried)) {
//                  results.add(InventoryUtils.getDoubleChest((TileEntity)inventory));
//               }
//            }
            }
        }

        return results;
    }

    public static ArrayList<IInventory> getContainersWithRoom(World world, EntityGolemBase golem, byte color, ItemStack itemToMatch) {
        ArrayList<IInventory> results = new ArrayList<>();

        for (IInventory inventory : getMarkedContainers(world, golem)) {
            boolean hasRoom = false;

            for (Integer side : getMarkedSides(golem, (TileEntity) inventory, color)) {
                ItemStack result = InventoryUtils.placeItemStackIntoInventory(itemToMatch, inventory, side, false);
                if (!ItemStack.areItemStacksEqual(result, itemToMatch)) {
                    results.add(inventory);
                    break;
                }

//            if (InventoryUtils.getDoubleChest((TileEntity)inventory) != null) {
//               result = InventoryUtils.placeItemStackIntoInventory(itemToMatch, InventoryUtils.getDoubleChest((TileEntity)inventory), side, false);
//               if (!ItemStack.areItemStacksEqual(result, itemToMatch)) {
//                  results.add(InventoryUtils.getDoubleChest((TileEntity)inventory));
//               }
//            }
            }
        }

        return results;
    }

    public static Set<Direction> getMarkedSides(BaseGolemEntity golem, BlockEntity tile, byte color) {
        var pos = tile.getBlockPos();
        return getMarkedSides(golem, pos.getX(), pos.getY(), pos.getZ(), golem.level().dimension().location(), color);
    }

    public static void getMarkedSides(Set<Direction> markedSides, BaseGolemEntity golem, BlockEntity tile, byte color) {
        var pos = tile.getBlockPos();
        getMarkedSides(markedSides, golem, pos.getX(), pos.getY(), pos.getZ(), golem.level().dimension().location(), color);
    }

    public static void getMarkedSides(Set<Direction> markedSides, BaseGolemEntity golem, int x, int y, int z, ResourceLocation dimResLoc, byte color) {
        for (var marker : golem.markers) {
            if (marker.equalsFuzzy(x, y, z, dimResLoc, null, color)) {
                markedSides.add(marker.side);
            }
        }
    }

    public static Set<Direction> getMarkedSides(BaseGolemEntity golem, int x, int y, int z, ResourceLocation dimResLoc, byte color) {
        var out = new ObjectArraySet<Direction>();
        for (var marker : golem.markers) {
            if (marker.equalsFuzzy(x, y, z, dimResLoc, null, color)) {
                out.add(marker.side);
            }
        }
        return out;
    }

//   public static boolean contained(ArrayList<Marker> l, Marker m) {
//      for(Marker mark : l) {
//         if (m.equalsFuzzy(mark)) {
//            return true;
//         }
//      }
//
//      return false;
//   }

//   public static ArrayList<IInventory> getContainersWithGoods(World world, EntityGolemBase golem, ItemStack goods, byte color) {
//      ArrayList<IInventory> results = new ArrayList<>();
//
//      for(IInventory inventory : getMarkedContainers(world, golem)) {
//         try {
//            for(Integer side : getMarkedSides(golem, (TileEntity)inventory, color)) {
//               if (InventoryUtils.extractStack(inventory, goods, side, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT(), false) != null) {
//                  results.add(inventory);
//                  break;
//               }
//
//               if (InventoryUtils.getDoubleChest((TileEntity)inventory) != null
//                       && InventoryUtils.extractStack(InventoryUtils.getDoubleChest((TileEntity)inventory), goods, side, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT(), false) != null) {
//                  results.add(InventoryUtils.getDoubleChest((TileEntity)inventory));
//                  break;
//               }
//            }
//         } catch (Exception ignored) {
//         }
//      }
//
//      return results;
//   }

    public static final Map<Item, Set<TagKey<Item>>> consideringTagsForItem = new ConcurrentHashMap<>();

    public static void onDatapackReloaded() {
        consideringTagsForItem.clear();
    }

    @SuppressWarnings("deprecation")
    public static Set<TagKey<Item>> getConsideringTagsForItem(Item item) {
        return consideringTagsForItem.computeIfAbsent(item, i ->
                i.builtInRegistryHolder()
                        .tags()
                        .filter(GolemHelper::shouldConsiderItemTag)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    //probably mixin point?
    public static boolean shouldConsiderItemTag(TagKey<Item> tag) {
        return true;
    }

    public static int getAmountNeededSmart(NonNullList<ItemStack> inventory, ItemStack stackInSlot, boolean fuzzy) {
        int amt = 0;

        for (ItemStack consideringStack : inventory) {
            if (!consideringStack.isEmpty()) {
                if (ItemStack.isSameItemSameTags(consideringStack, stackInSlot)) {
                    amt += consideringStack.getCount();
                } else if (fuzzy) {
                    var tags = getConsideringTagsForItem(stackInSlot.getItem());
                    var tagsForConsidering = getConsideringTagsForItem(consideringStack.getItem());
                    if (!Collections.disjoint(tags, tagsForConsidering)){
                        amt += consideringStack.getCount();
                    }
                }
            }
        }

        return amt;
    }

//   public static List<ItemStack> getMissingItems(BaseGolemEntity golem) {
//      Direction facing = Direction.getOrientation(golem.homeFacing);
//      ChunkCoordinates home = golem.getHomePosition();
//      int attachedContainerX = home.posX - facing.offsetX;
//      int attachedContainerY = home.posY - facing.offsetY;
//      int attachedContainerZ = home.posZ - facing.offsetZ;
//
//      int slotCount = golem.inventory.slotCount;
//      if (golem.getToggles()[0]) {//Any amount
//         List<ItemStack> qr = new ArrayList<>();
//
//         for(int q = 0; q < slotCount; ++q) {
//            ItemStack toCheck = golem.inventory.inventory[q];
//            if (toCheck != null) {
//               ItemStack ret = toCheck.copy();
//               qr.add(ret);
//            }
//         }
//
//         return qr;
//      }
//      {
////         TileEntity homeBlockEntity = golem.level().getTileEntity(attachedContainerX, attachedContainerY, attachedContainerZ);
//         var homeBlockEntity = getExistingBlockEntity(golem.level(),new BlockPos(attachedContainerX, attachedContainerY, attachedContainerZ));
//         if (homeBlockEntity == null) {
//            return null;
//         } else {
//            List<ItemStack> qr = new ArrayList<>();
//
//            boolean fuzzyMatch = golem.configurationProviders.contains(MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY)
//                    && MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY.provider.getBooleanFrom(golem);
//            var golemStackMatcher = golem.getItemStackMatcher();
//
////            label105:
//            for(var toCheckInGolemInventory:golem.inventory) {
//               boolean checkNext = false;
//               if (!toCheckInGolemInventory.isEmpty()) {
//                  int foundAmount = 0;
//
//                     if (homeBlockEntity instanceof WorldlyContainer worldlyContainer /*&& facing.ordinal() > -1*/) {
//                        int[] canAccessSlots = worldlyContainer.getSlotsForFace(facing);
//
//                         for (int i : canAccessSlots) {
//                            ItemStack consideringStack = worldlyContainer.getItem(i);
//                             if (
//                                     golem.getItemStackMatcher().test(toCheckInGolemInventory, consideringStack)
//                             ) {
//                                foundAmount += consideringStack.getCount();
//                                 if (foundAmount >= getAmountNeededSmart(
//                                         golem.inventory,
//                                         consideringStack,
//                                         fuzzyMatch)
//                                 ) {
//                                    checkNext = true;
//                                    break;
////                                     continue label105;
//                                 }
//                             }
//                         }
//                     } else {
//                        if (!(homeBlockEntity instanceof Container container)) {
//                           break;
//                        }
//
//                        int containerSize = container.getContainerSize();
//
//                        for(int l = 0; l < containerSize; ++l) {
//                           ItemStack consideringStackInContainer = container.getItem(l);
//                           if (golemStackMatcher.test(consideringStackInContainer, toCheckInGolemInventory)) {
//                              foundAmount += consideringStackInContainer.getCount();
//                              if (foundAmount >= getAmountNeededSmart(golem.inventory, consideringStackInContainer, fuzzyMatch)) {
//                                 checkNext = true;
//                                 break;

    /// /                                 continue label105;
//                              }
//                           }
//                        }
//                     }
//                  if (checkNext) {
//                     continue;
//                  }
//
//                  ItemStack ret = toCheckInGolemInventory.copy();
//                  ret.setCount(ret.getCount() - foundAmount);
//                  qr.add(ret);
//               }
//            }
//
//            return qr;
//         }
//      }
//   }
    public static ChunkCoordinates findJarWithRoom(EntityGolemBase golem) {
        ChunkCoordinates dest = null;
        World world = golem.level();
        float dmod = golem.getRange();
        dmod *= dmod;
        ArrayList<TileEntity> jars = new ArrayList<>();
        ArrayList<TileEntity> others = new ArrayList<>();

        for (Marker marker : golem.getMarkers()) {
            TileEntity te = world.getTileEntity(marker.x, marker.y, marker.z);
            if (marker.dim == world.dimension() && te instanceof TileJarFillable) {
                if (te.getDistanceFrom(golem.getHomePosition().posX, golem.getHomePosition().posY, golem.getHomePosition().posZ) <= (double) dmod) {
                    jars.add(te);
                }
            } else if (marker.dim == world.dimension() && te instanceof TileEssentiaReservoir res) {
                if (res.getSuctionAmount(res.facing) > 0 && (res.getSuctionType(res.facing) == null || res.getSuctionType(res.facing) == golem.essentia) && te.getDistanceFrom(golem.getHomePosition().posX, golem.getHomePosition().posY, golem.getHomePosition().posZ) <= (double) dmod) {
                    others.add(te);
                }
            } else if (marker.dim == world.dimension() && te instanceof IEssentiaTransportBlockEntity trans) {
                if (golem.essentia != null && golem.essentiaAmount > 0 && trans.canInputFrom(Direction.getOrientation(marker.side)) && trans.getSuctionAmount(Direction.getOrientation(marker.side)) > 0 && (trans.getSuctionType(Direction.getOrientation(marker.side)) == null || trans.getSuctionType(Direction.getOrientation(marker.side)) == golem.essentia) && te.getDistanceFrom(golem.getHomePosition().posX, golem.getHomePosition().posY, golem.getHomePosition().posZ) <= (double) dmod) {
                    others.add(te);
                }
            }
        }

        if (!jars.isEmpty()) {
            jarlist.clear();

            for (TileEntity jar : jars) {
                jarlist.put(jar.xCoord + ":" + jar.yCoord + ":" + jar.zCoord, (TileJarFillable) jar);
                getConnectedJars((TileJarFillable) jar);
            }
        } else if (others.isEmpty()) {
            return null;
        }

        jars = new ArrayList<>(others);

        for (TileJarFillable jar : jarlist.values()) {
            if (jar.aspect != null && jar.amount > 0 && jar.amount < jar.maxAmount && jar.aspectFilter != null && golem.essentiaAmount > 0 && jar.aspect.equals(golem.essentia) && jar.doesContainerAccept(golem.essentia)) {
                jars.add(jar);
            }
        }

        if (jars.isEmpty()) {
            for (TileJarFillable jar : jarlist.values()) {
                if ((jar.aspect == null || jar.amount == 0) && jar.aspectFilter != null && jar.doesContainerAccept(golem.essentia)) {
                    jars.add(jar);
                }
            }
        }

        if (jars.isEmpty()) {
            for (TileJarFillable jar : jarlist.values()) {
                if (jar.aspect != null && jar.amount >= jar.maxAmount && jar instanceof TileJarFillableVoid && jar.aspectFilter != null && golem.essentiaAmount > 0 && jar.aspect.equals(golem.essentia) && jar.doesContainerAccept(golem.essentia)) {
                    jars.add(jar);
                }
            }
        }

        if (jars.isEmpty()) {
            for (TileJarFillable jar : jarlist.values()) {
                if (jar.aspect != null && jar.amount > 0 && jar.amount < jar.maxAmount && jar.aspectFilter == null && golem.essentiaAmount > 0 && jar.aspect.equals(golem.essentia) && jar.doesContainerAccept(golem.essentia)) {
                    jars.add(jar);
                }
            }
        }

        if (jars.isEmpty()) {
            for (TileJarFillable jar : jarlist.values()) {
                if ((jar.aspect == null || jar.amount == 0) && jar.aspectFilter == null && !(jar instanceof TileJarFillableVoid) && jar.doesContainerAccept(golem.essentia)) {
                    jars.add(jar);
                }
            }
        }

        if (jars.isEmpty()) {
            for (TileJarFillable jar : jarlist.values()) {
                if (jar.aspect != null && jar instanceof TileJarFillableVoid && jar.aspectFilter == null && golem.essentiaAmount > 0 && jar.aspect.equals(golem.essentia) && jar.doesContainerAccept(golem.essentia)) {
                    jars.add(jar);
                }
            }
        }

        if (jars.isEmpty()) {
            for (TileJarFillable jar : jarlist.values()) {
                if ((jar.aspect == null || jar.amount == 0) && jar.aspectFilter == null && jar instanceof TileJarFillableVoid && jar.doesContainerAccept(golem.essentia)) {
                    jars.add(jar);
                }
            }
        }

        double dist = Double.MAX_VALUE;

        for (TileEntity jar : jars) {
            double d = jar.getDistanceFrom(golem.getHomePosition().posX, golem.getHomePosition().posY, golem.getHomePosition().posZ);
            if (jar instanceof TileJarFillableVoid) {
                d += dmod;
            }

            if (d < dist) {
                dist = d;
                dest = new ChunkCoordinates(jar.xCoord, jar.yCoord, jar.zCoord);
            }
        }

        jarlist.clear();
        return dest;
    }

    private static void getConnectedJars(TileJarFillable jar) {
        World world = jar.getLevel();

        for (int dir = 0; dir < 6; ++dir) {
            Direction fd = Direction.getOrientation(dir);
            int xx = jar.xCoord + fd.offsetX;
            int yy = jar.yCoord + fd.offsetY;
            int zz = jar.zCoord + fd.offsetZ;
            if (!jarlist.containsKey(xx + ":" + yy + ":" + zz)) {
                TileEntity te = world.getTileEntity(xx, yy, zz);
                if (te instanceof TileJarFillable) {
                    jarlist.put(te.xCoord + ":" + te.yCoord + ":" + te.zCoord, (TileJarFillable) te);
                    getConnectedJars((TileJarFillable) te);
                }
            }
        }

    }

    public static ArrayList<Integer> getReggedLiquids() {
        if (reggedLiquids == null) {
            reggedLiquids = new ArrayList<>();

            reggedLiquids.addAll(FluidRegistry.getRegisteredFluidIDs().values());
        }

        return reggedLiquids;
    }

    public static ArrayList<FluidStack> getMissingLiquids(EntityGolemBase golem) {
        ArrayList<FluidStack> out = new ArrayList<>();
        Direction facing = Direction.getOrientation(golem.homeFacing);
        ChunkCoordinates home = golem.getHomePosition();
        int cX = home.posX - facing.offsetX;
        int cY = home.posY - facing.offsetY;
        int cZ = home.posZ - facing.offsetZ;
        TileEntity tile = golem.level().getTileEntity(cX, cY, cZ);
        if (tile instanceof IFluidHandler fluidhandler) {
            Iterator<Integer> i$ = getReggedLiquids().iterator();

            while (true) {
                Integer id;
                while (true) {
                    if (!i$.hasNext()) {
                        return out;
                    }

                    id = i$.next();
                    if ((golem.fluidCarried == null || golem.fluidCarried.amount <= 0 || golem.fluidCarried.getFluidID() == id) && fluidhandler.canFill(facing, FluidRegistry.getFluid(id))) {
                        FluidStack fs = new FluidStack(FluidRegistry.getFluid(id), Integer.MAX_VALUE);
                        if (!golem.inventory.hasSomething()) {
                            break;
                        }

                        FluidStack fis = null;
                        boolean found = false;

                        for (int a = 0; a < golem.inventory.slotCount; ++a) {
                            fis = FluidContainerRegistry.getFluidForFilledItem(golem.inventory.getStackInSlot(a));
                            if (fis != null && fis.isFluidEqual(fs)) {
                                found = true;
                                break;
                            }
                        }

                        if (found) {
                            break;
                        }
                    }
                }

                out.add(new FluidStack(id, Integer.MAX_VALUE));
            }
        } else {
            return out;
        }
    }

    public static Vec3 findPossibleLiquid(FluidStack ls, EntityGolemBase golem) {
        Direction facing = Direction.getOrientation(golem.homeFacing);
        ChunkCoordinates home = golem.getHomePosition();
        int var10000 = home.posX - facing.offsetX;
        var10000 = home.posY - facing.offsetY;
        var10000 = home.posZ - facing.offsetZ;
        float dmod = golem.getRange();
        ChunkCoordinates v = null;
        ArrayList<IFluidHandler> fluidhandlers = getMarkedFluidHandlers(ls, golem.level(), golem);
        double dd = Double.MAX_VALUE;
        if (fluidhandlers != null) {
            for (IFluidHandler fluidhandler : fluidhandlers) {
                if (fluidhandler != null) {
                    TileEntity tile = (TileEntity) fluidhandler;
                    double d = golem.getDistanceSq((double) tile.xCoord + (double) 0.5F, (double) tile.yCoord + (double) 0.5F, (double) tile.zCoord + (double) 0.5F);
                    if (d <= (double) (dmod * dmod) && d < dd) {
                        dd = d;
                        v = new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
                    }
                }
            }
        }

        if (v == null) {
            ArrayList<ChunkCoordinates> inworld = getMarkedFluidBlocks(ls, golem.level(), golem);
            dd = Double.MAX_VALUE;
            if (inworld != null) {
                for (ChunkCoordinates coord : inworld) {
                    if (coord != null) {
                        double d = golem.getDistanceSq((double) coord.posX + (double) 0.5F, (double) coord.posY + (double) 0.5F, (double) coord.posZ + (double) 0.5F);
                        if (d <= (double) (dmod * dmod) && d < dd) {
                            dd = d;
                            v = new ChunkCoordinates(coord.posX, coord.posY, coord.posZ);
                        }
                    }
                }
            }
        }

        return v != null ? Vec3.createVectorHelper(v.posX, v.posY, v.posZ) : null;
    }

    public static ArrayList<Marker> getMarkedFluidHandlersAdjacentToGolem(FluidStack ls, World world, EntityGolemBase golem) {
        ArrayList<Marker> results = new ArrayList<>();

        for (Marker marker : golem.getMarkers()) {
            TileEntity te = world.getTileEntity(marker.x, marker.y, marker.z);
            if (marker.dim == world.dimension() && te instanceof IFluidHandler && golem.getDistanceSq((double) te.xCoord + (double) 0.5F, (double) te.yCoord + (double) 0.5F, (double) te.zCoord + (double) 0.5F) < (double) 4.0F) {
                FluidStack fs = ((IFluidHandler) te).drain(Direction.getOrientation(marker.side), new FluidStack(ls.getFluid(), 1), false);
                if (fs != null && fs.amount > 0) {
                    results.add(marker);
                }
            }
        }

        return results;
    }

    public static ArrayList getMarkedFluidHandlers(FluidStack ls, World world, EntityGolemBase golem) {
        ArrayList<IFluidHandler> results = new ArrayList<>();

        for (Marker marker : golem.getMarkers()) {
            TileEntity te = world.getTileEntity(marker.x, marker.y, marker.z);
            if (marker.dim == world.dimension() && te instanceof IFluidHandler) {
                FluidStack fs = ((IFluidHandler) te).drain(Direction.getOrientation(marker.side), new FluidStack(ls.getFluid(), 1), false);
                if (fs != null && fs.amount > 0) {
                    results.add((IFluidHandler) te);
                }
            }
        }

        return results;
    }

    public static ArrayList getMarkedFluidBlocks(FluidStack ls, World world, EntityGolemBase golem) {
        ArrayList<ChunkCoordinates> results = new ArrayList<>();

        for (Marker marker : golem.getMarkers()) {
            Block bi = world.getBlock(marker.x, marker.y, marker.z);
            if (marker.dim == world.dimension() && FluidRegistry.getFluid(ls.getFluidID()).getBlock() == bi) {
                if (bi instanceof BlockFluidBase && ((BlockFluidBase) bi).canDrain(world, marker.x, marker.y, marker.z)) {
                    results.add(new ChunkCoordinates(marker.x, marker.y, marker.z));
                } else if (ls.getFluidID() == FluidRegistry.WATER.getID() || ls.getFluidID() == FluidRegistry.LAVA.getID()) {
                    int wmd = world.getBlockMetadata(marker.x, marker.y, marker.z);
                    if ((FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.WATER && ls.getFluidID() == FluidRegistry.WATER.getID() || FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.LAVA && ls.getFluidID() == FluidRegistry.LAVA.getID()) && wmd == 0) {
                        results.add(new ChunkCoordinates(marker.x, marker.y, marker.z));
                    }
                }
            }
        }

        return results;
    }

    public static ArrayList<ItemStack> getItemsNeeded(EntityGolemBase golem, boolean fuzzy) {
        ArrayList<ItemStack> needed = null;
        switch (golem.getCore()) {
            case 1:
                needed = golem.inventory.getItemsNeeded(golem.getUpgradeAmount(5) > 0);
                if (needed.isEmpty()) {
                    return null;
                }

                return filterEmptyCore(golem, needed);
            case 8:
                needed = golem.inventory.getItemsNeeded(golem.getUpgradeAmount(5) > 0);
                if (needed.isEmpty()) {
                    return null;
                }

                return filterUseCore(golem, needed);
            case 10:
                needed = getItemsInHomeContainer(golem);
                return filterSortCore(golem, needed);
            default:
                return needed;
        }
    }

    private static ArrayList<ItemStack> filterEmptyCore(EntityGolemBase golem, ArrayList<ItemStack> in) {
        ArrayList<ItemStack> out = new ArrayList<>();

        for (ItemStack itemToMatch : in) {
            if (!isOnTimeOut(golem, itemToMatch) && findSomethingEmptyCore(golem, itemToMatch)) {
                out.add(itemToMatch);
            }
        }

        return out;
    }

    private static ArrayList<ItemStack> filterUseCore(EntityGolemBase golem, ArrayList<ItemStack> in) {
        ArrayList<ItemStack> out = new ArrayList<>();

        for (ItemStack itemToMatch : in) {
            if (!isOnTimeOut(golem, itemToMatch) && findSomethingUseCore(golem, itemToMatch)) {
                out.add(itemToMatch);
            }
        }

        return out;
    }

    private static ArrayList<ItemStack> filterSortCore(EntityGolemBase golem, ArrayList<ItemStack> in) {
        ArrayList<ItemStack> out = new ArrayList<>();

        for (ItemStack itemToMatch : in) {
            if (!isOnTimeOut(golem, itemToMatch) && findSomethingSortCore(golem, itemToMatch)) {
                out.add(itemToMatch);
            }
        }

        return out;
    }

    public static boolean findSomethingUseCore(EntityGolemBase golem, ItemStack itemToMatch) {
        for (byte col : golem.getColorsMatching(itemToMatch)) {
            for (Marker marker : golem.getMarkers()) {
                if ((marker.color == col || col == -1) && (!golem.getToggles()[0] || golem.level().isAirBlock(marker.x, marker.y, marker.z)) && (golem.getToggles()[0] || !golem.level().isAirBlock(marker.x, marker.y, marker.z))) {
                    Direction opp = Direction.getOrientation(marker.side);
                    if (golem.level().isAirBlock(marker.x + opp.offsetX, marker.y + opp.offsetY, marker.z + opp.offsetZ)) {
                        return true;
                    }
                }
            }
        }

        itemTimeout.add(new SortingItemTimeout(golem.getEntityId(), itemToMatch.copy(), System.currentTimeMillis() + (long) Config.golemIgnoreDelay));
        return false;
    }

    public static boolean findSomethingEmptyCore(EntityGolemBase golem, ItemStack itemToMatch) {
        ArrayList<Byte> matchingColors = golem.getColorsMatching(itemToMatch);

        for (byte color : matchingColors) {
            ArrayList<IInventory> markers = getContainersWithRoom(golem.level(), golem, color, itemToMatch);
            if (!markers.isEmpty()) {
                Direction i$1 = Direction.getOrientation(golem.homeFacing);
                ChunkCoordinates marker = golem.getHomePosition();
                int cX = marker.posX - i$1.offsetX;
                int cY = marker.posY - i$1.offsetY;
                int cZ = marker.posZ - i$1.offsetZ;
                double range = Double.MAX_VALUE;
                float dmod = golem.getRange();

                for (IInventory te : markers) {
                    double distance = golem.getDistanceSq((double) ((TileEntity) te).xCoord + (double) 0.5F, (double) ((TileEntity) te).yCoord + (double) 0.5F, (double) ((TileEntity) te).zCoord + (double) 0.5F);
                    if (distance < range && distance <= (double) (dmod * dmod) && (((TileEntity) te).xCoord != cX || ((TileEntity) te).yCoord != cY || ((TileEntity) te).zCoord != cZ)) {
                        return true;
                    }
                }
            }
        }

        for (byte color : matchingColors) {
            for (Marker marker1 : golem.getMarkers()) {
                if ((marker1.color == color || color == -1) && (golem.level().getTileEntity(marker1.x, marker1.y, marker1.z) == null || !(golem.level().getTileEntity(marker1.x, marker1.y, marker1.z) instanceof IInventory))) {
                    return true;
                }
            }
        }

        itemTimeout.add(new SortingItemTimeout(golem.getEntityId(), itemToMatch.copy(), System.currentTimeMillis() + (long) Config.golemIgnoreDelay));
        return false;
    }

    public static boolean findSomethingSortCore(EntityGolemBase golem, ItemStack itemToMatch) {
        ArrayList<IInventory> markers = getContainersWithRoom(golem.level(), golem, (byte) -1, itemToMatch);
        if (!markers.isEmpty()) {
            Direction i$1 = Direction.getOrientation(golem.homeFacing);
            ChunkCoordinates marker = golem.getHomePosition();
            int cX = marker.posX - i$1.offsetX;
            int cY = marker.posY - i$1.offsetY;
            int cZ = marker.posZ - i$1.offsetZ;
            double range = Double.MAX_VALUE;
            float dmod = golem.getRange();

            for (IInventory te : markers) {
                double distance = golem.getDistanceSq((double) ((TileEntity) te).xCoord + (double) 0.5F, (double) ((TileEntity) te).yCoord + (double) 0.5F, (double) ((TileEntity) te).zCoord + (double) 0.5F);
                if (distance < range && distance <= (double) (dmod * dmod) && (((TileEntity) te).xCoord != cX || ((TileEntity) te).yCoord != cY || ((TileEntity) te).zCoord != cZ)) {
                    for (int side : getMarkedSides(golem, (TileEntity) te, (byte) -1)) {
                        if (InventoryUtils.inventoryContains(te, itemToMatch, side, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT())) {
                            return true;
                        }
                    }
                }
            }
        }

        itemTimeout.add(new SortingItemTimeout(golem.getEntityId(), itemToMatch.copy(), System.currentTimeMillis() + (long) Config.golemIgnoreDelay));
        return false;
    }

    public static boolean isOnTimeOut(BaseGolemEntity golem, ItemStack stack) {
        var fakeField = opentc4$getCachedObjectFrom(
                stack,
                GolemPlaceItemStackToHomeGoalWithItemStackTickMark.ItemStackSortingTickMarkFakeFields.class);
        if (fakeField == null) {
            return false;
        }
        return fakeField.golemID == golem.getId() && fakeField.placeBeforeTickCount < golem.tickCount;
    }

    public static boolean validTargetForItem(BaseGolemEntity golem, ItemStack stack) {
        if (isOnTimeOut(golem, stack)) {
            return false;
        } else {
            Direction facing = Direction.getOrientation(golem.homeFacing);
            ChunkCoordinates home = golem.getHomePosition();
            int cX = home.posX - facing.offsetX;
            int cY = home.posY - facing.offsetY;
            int cZ = home.posZ - facing.offsetZ;
            switch (golem.getCore()) {
                case 1:
                    return findSomethingEmptyCore(golem, stack);
                case 8:
                    return findSomethingUseCore(golem, stack);
                case 10:
                    return findSomethingSortCore(golem, stack);
                default:
                    golem.level().getTileEntity(cX, cY, cZ);
                    ArrayList<ItemStack> neededList = getItemsNeeded(golem, golem.getUpgradeAmount(5) > 0);
                    if (neededList != null && !neededList.isEmpty()) {
                        for (ItemStack ss : neededList) {
                            if (InventoryUtils.areItemStacksEqual(ss, golem.itemCarried, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT())) {
                                return true;
                            }
                        }
                    }

                    itemTimeout.add(new SortingItemTimeout(golem.getEntityId(), stack.copy(), System.currentTimeMillis() + (long) Config.golemIgnoreDelay));
                    return false;
            }
        }
    }

    public static int getGolemCarrySpace(BaseGolemEntity golem, int stackCount, int maxStackCount) {
        return Math.min(golem.getCarryPower() - stackCount,
                maxStackCount - stackCount);
    }

    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class IntRangeIterator implements IntIterator {
        public int currentIndex = 0;
        public final int maxIndex;

        @Override
        public int nextInt() {
            currentIndex += 1;
            return currentIndex;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < maxIndex;
        }
    }
    @RequiredArgsConstructor
    public static class IntArrayIterator implements IntIterator {
        public int currentIndex = 0;
        public final int[] array;

        @Override
        public int nextInt() {
            currentIndex += 1;
            return array[currentIndex];
        }

        @Override
        public boolean hasNext() {
            return currentIndex < array.length;
        }
    }

    public static ItemStack getFirstItemUsingTimeout(BaseGolemEntity golem, Container container, @Nullable Direction side, boolean doit) {
        IntIterator iterator = null;
        if (container instanceof WorldlyContainer isidedinventory && side != null) {
            int[] aint = isidedinventory.getSlotsForFace(side);
            iterator = new IntArrayIterator(aint);
        } else {
            iterator = new IntRangeIterator(container.getContainerSize());
        }
        while (iterator.hasNext()) {
            int i = iterator.nextInt();

            var extracted = extractStackFromContainerWithTimeout(
                    golem,container,i,side,doit
            );
            if (!extracted.isEmpty()) {
                return extracted;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack extractStackFromContainerWithTimeout(BaseGolemEntity golem,Container container, int slot,@Nullable Direction side,boolean doit) {
        var consideringStack = container.getItem(slot);
        if (!consideringStack.isEmpty()) {
            if (isOnTimeOut(golem, consideringStack)) {
                return ItemStack.EMPTY;
            }

            var stack1 = ExtendedItemStack.copyAndShareTag(consideringStack);
            stack1.setCount(getGolemCarrySpace(golem,stack1.getCount(),stack1.getMaxStackSize()));
            var extracted = InventoryUtils.attemptExtraction(
                    container,
                    stack1,
                    slot,
                    side,
                    golem.getItemStackMatcher(),
                    doit);
            if (!extracted.isEmpty()) {
                if (doit){
                    extracted = extracted.copy();
                }
                return extracted;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ArrayList<ItemStack> getItemsInHomeContainer(EntityGolemBase golem) {
        Direction facing = Direction.getOrientation(golem.homeFacing);
        ChunkCoordinates home = golem.getHomePosition();
        int cX = home.posX - facing.offsetX;
        int cY = home.posY - facing.offsetY;
        int cZ = home.posZ - facing.offsetZ;
        TileEntity tile = golem.level().getTileEntity(cX, cY, cZ);
        if (tile instanceof IInventory inv) {
            int[] aint = null;
            ArrayList<ItemStack> out = new ArrayList<>();
            if (tile instanceof ISidedInventory && facing.ordinal() > -1) {
                aint = ((ISidedInventory) inv).getAccessibleSlotsFromSide(facing.ordinal());
            } else {
                aint = new int[inv.getSizeInventory()];

                for (int a = 0; a < inv.getSizeInventory(); aint[a] = a++) {
                }
            }

            if (aint != null) {
                for (int i : aint) {
                    if (inv.getStackInSlot(i) != null) {
                        out.add(inv.getStackInSlot(i).copy());
                    }
                }
            }

            return out;
        } else {
            return null;
        }
    }

    public static class SortingItemTimeout implements Comparable<SortingItemTimeout> {
        ItemStack stack = null;
        int golemId = 0;
        long time = 0L;

        public SortingItemTimeout(int golemId, ItemStack stack, long time) {
            this.stack = stack;
            this.golemId = golemId;
            this.time = time;
        }

        public int compareTo(@NotNull SortingItemTimeout arg0) {
            return this.equals(arg0) ? 0 : -1;
        }

        public boolean equals(Object obj) {
            if (obj instanceof SortingItemTimeout t) {
                if (this.golemId != t.golemId) {
                    return false;
                }

                if (!this.stack.isItemEqual(t.stack)) {
                    return false;
                }

                return ItemStack.areItemStackTagsEqual(this.stack, t.stack);
            }

            return true;
        }
    }
}
