package thaumcraft.client.fx.migrated.bolt;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.client.fx.migrated.WRVector3;

import java.util.*;

public class FXLightningBoltCommon {
    ArrayList<FXLightningBoltCommon.Segment> segments;
    WRVector3 start;
    WRVector3 end;
    HashMap<Integer,Integer> splitparents;
    public float multiplier;
    public float length;
    public int numsegments0;
    public int increment;
    public int type;
    public boolean nonLethal;
    private int numsplits;
    private boolean finalized;
    private Random random;
    public long seed;
    public int age;
    public int lifetime;
    private ClientLevel world;
    public static final float speed = 3.0F;
    public static final int fadetime = 20;

    public FXLightningBoltCommon(ClientLevel world, WRVector3 jammervec, WRVector3 targetvec, long seed) {
        this.type = 0;
        this.nonLethal = false;
        this.segments = new ArrayList<>();
        this.splitparents = new HashMap<>();
        this.world = world;
        this.start = jammervec;
        this.end = targetvec;
        this.seed = seed;
        this.random = new Random(seed);
        this.numsegments0 = 1;
        this.increment = 1;
        this.length = this.end.copy()
                .sub(this.start)
                .length();
        this.lifetime = 3 + this.random.nextInt(3) - 1;
        this.multiplier = 1.0F;
        this.age = -((int) (this.length * 3.0F));
        this.segments.add(new FXLightningBoltCommon.Segment(this.start, this.end));
    }

    public FXLightningBoltCommon(ClientLevel world, Entity detonator, Entity target, long seed) {
        this(world, new WRVector3(detonator), new WRVector3(target), seed);
    }

    public FXLightningBoltCommon(ClientLevel world, Entity detonator, Entity target, long seed, int speed) {
        this(world, new WRVector3(detonator), new WRVector3(target.getX(), target.getY() + (double)target.getEyeHeight() - (double)0.7F, target.getZ()), seed);
        this.increment = speed;
        this.multiplier = 0.4F;
    }

    public FXLightningBoltCommon(ClientLevel world, BlockEntity detonator, Entity target, long seed) {
        this(world, new WRVector3(detonator), new WRVector3(target), seed);
    }

    public FXLightningBoltCommon(ClientLevel world, BlockEntity detonator, double x, double y, double z, long seed) {
        this(world, new WRVector3(detonator), new WRVector3(x, y, z), seed);
    }

    public FXLightningBoltCommon(ClientLevel world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi) {
        this(world, new WRVector3(x1, y1, z1), new WRVector3(x, y, z), seed);
        this.lifetime = duration + this.random.nextInt(duration) - duration / 2;
        this.multiplier = multi;
    }

    public FXLightningBoltCommon(ClientLevel world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed) {
        this(world, new WRVector3(x1, y1, z1), new WRVector3(x, y, z), seed);
        this.lifetime = duration + this.random.nextInt(duration) - duration / 2;
        this.multiplier = multi;
        this.increment = speed;
    }

    public void setMultiplier(float m) {
        this.multiplier = m;
    }

    public void fractal(int splits, float amount, float splitchance, float splitlength, float splitangle) {
        if (!this.finalized) {
            ArrayList<Segment> oldsegments = this.segments;
            this.segments = new ArrayList<>();
            FXLightningBoltCommon.Segment prev = null;

            for(FXLightningBoltCommon.Segment segment : oldsegments) {
                prev = segment.prev;
                WRVector3 subsegment = segment.diff.copy().scale(1.0F / (float)splits);
                FXLightningBoltCommon.BoltPoint[] newpoints = new FXLightningBoltCommon.BoltPoint[splits + 1];
                WRVector3 startpoint = segment.startpoint.point;
                newpoints[0] = segment.startpoint;
                newpoints[splits] = segment.endpoint;

                for(int i = 1; i < splits; ++i) {
                    WRVector3 randoff = WRVector3.getPerpendicular(segment.diff).rotate(
                            this.random.nextFloat() * 360.0F, segment.diff);
                    randoff.scale((this.random.nextFloat() - 0.5F) * amount);
                    WRVector3 basepoint = startpoint.copy().add(subsegment.copy().scale((float)i));
                    newpoints[i] = new FXLightningBoltCommon.BoltPoint(basepoint, randoff);
                }

                for(int i = 0; i < splits; ++i) {
                    FXLightningBoltCommon.Segment next = new FXLightningBoltCommon.Segment(
                            newpoints[i], newpoints[i + 1], segment.light, segment.segmentno * splits + i,
                            segment.splitno
                    );
                    next.prev = prev;
                    if (prev != null) {
                        prev.next = next;
                    }

                    if (i != 0 && this.random.nextFloat() < splitchance) {
                        WRVector3 splitrot = WRVector3.xCrossProduct(next.diff).rotate(
                                this.random.nextFloat() * 360.0F, next.diff);
                        WRVector3 diff = next.diff.copy().rotate((this.random.nextFloat() * 0.66F + 0.33F) * splitangle, splitrot).scale(splitlength);
                        ++this.numsplits;
                        this.splitparents.put(this.numsplits, next.splitno);
                        FXLightningBoltCommon.Segment split = new FXLightningBoltCommon.Segment(
                                newpoints[i], new BoltPoint(
                                newpoints[i + 1].basepoint, newpoints[i + 1].offsetvec.copy()
                                .add(diff)
                        ), segment.light / 2.0F, next.segmentno, this.numsplits
                        );
                        split.prev = prev;
                        this.segments.add(split);
                    }

                    prev = next;
                    this.segments.add(next);
                }

                if (segment.next != null) {
                    segment.next.prev = prev;
                }
            }

            this.numsegments0 *= splits;
        }
    }

    public void defaultFractal() {
        this.fractal(2, this.length * this.multiplier / 8.0F, 0.7F, 0.1F, 45.0F);
        this.fractal(2, this.length * this.multiplier / 12.0F, 0.5F, 0.1F, 50.0F);
        this.fractal(2, this.length * this.multiplier / 17.0F, 0.5F, 0.1F, 55.0F);
        this.fractal(2, this.length * this.multiplier / 23.0F, 0.5F, 0.1F, 60.0F);
        this.fractal(2, this.length * this.multiplier / 30.0F, 0.0F, 0.0F, 0.0F);
        this.fractal(2, this.length * this.multiplier / 34.0F, 0.0F, 0.0F, 0.0F);
        this.fractal(2, this.length * this.multiplier / 40.0F, 0.0F, 0.0F, 0.0F);
    }

    private void calculateCollisionAndDiffs() {
        HashMap lastactivesegment = new HashMap<>();
        this.segments.sort(new FXLightningBoltCommon.SegmentSorter());
        int lastsplitcalc = 0;
        int lastactiveseg = 0;

        for (Segment segment : this.segments) {
            if (segment.splitno > lastsplitcalc) {
                lastactivesegment.put(lastsplitcalc, lastactiveseg);
                lastsplitcalc = segment.splitno;
                lastactiveseg = (Integer) lastactivesegment.get(this.splitparents.get(segment.splitno));
            }

            lastactiveseg = segment.segmentno;
        }

        lastactivesegment.put(lastsplitcalc, lastactiveseg);
        lastsplitcalc = 0;
        lastactiveseg = (Integer)lastactivesegment.get(0);

        FXLightningBoltCommon.Segment segment;
        for(Iterator iterator = this.segments.iterator(); iterator.hasNext(); segment.calcEndDiffs()) {
            segment = (FXLightningBoltCommon.Segment)iterator.next();
            if (lastsplitcalc != segment.splitno) {
                lastsplitcalc = segment.splitno;
                lastactiveseg = (Integer)lastactivesegment.get(segment.splitno);
            }

            if (segment.segmentno > lastactiveseg) {
                iterator.remove();
            }
        }

    }

    public void finalizeBolt() {
        if (!this.finalized) {
            this.finalized = true;
            this.calculateCollisionAndDiffs();
            this.segments.sort(new FXLightningBoltCommon.SegmentLightSorter());
        }
    }

    public void onUpdate() {
        this.age += this.increment;
        if (this.age > this.lifetime) {
            this.age = this.lifetime;
        }

    }

    public class BoltPoint {
        WRVector3 point;
        WRVector3 basepoint;
        WRVector3 offsetvec;
        final FXLightningBoltCommon this$0 = FXLightningBoltCommon.this;

        public BoltPoint(WRVector3 basepoint, WRVector3 offsetvec) {
            this.point = basepoint.copy()
                    .add(offsetvec);
            this.basepoint = basepoint;
            this.offsetvec = offsetvec;
        }
    }

    public class Segment {
        public FXLightningBoltCommon.BoltPoint startpoint;
        public FXLightningBoltCommon.BoltPoint endpoint;
        public WRVector3 diff;
        public FXLightningBoltCommon.Segment prev;
        public FXLightningBoltCommon.Segment next;
        public WRVector3 nextdiff;
        public WRVector3 prevdiff;
        public float sinprev;
        public float sinnext;
        public float light;
        public int segmentno;
        public int splitno;
        final FXLightningBoltCommon this$0;

        public void calcDiff() {
            this.diff = this.endpoint.point.copy()
                    .sub(this.startpoint.point);
        }

        public void calcEndDiffs() {
            if (this.prev != null) {
                WRVector3 prevdiffnorm = this.prev.diff.copy().normalize();
                WRVector3 thisdiffnorm = this.diff.copy().normalize();
                this.prevdiff = thisdiffnorm.add(prevdiffnorm)
                        .normalize();
                this.sinprev = (float) Math.sin(WRVector3.anglePreNorm(thisdiffnorm, prevdiffnorm.scale(-1.0F)) / 2.0F);
            } else {
                this.prevdiff = this.diff.copy()
                        .normalize();
                this.sinprev = 1.0F;
            }

            if (this.next != null) {
                WRVector3 nextdiffnorm = this.next.diff.copy().normalize();
                WRVector3 thisdiffnorm = this.diff.copy().normalize();
                this.nextdiff = thisdiffnorm.add(nextdiffnorm)
                        .normalize();
                this.sinnext = (float) Math.sin(WRVector3.anglePreNorm(thisdiffnorm, nextdiffnorm.scale(-1.0F)) / 2.0F);
            } else {
                this.nextdiff = this.diff.copy()
                        .normalize();
                this.sinnext = 1.0F;
            }

        }

        public String toString() {
            return this.startpoint.point.toString() + " " + this.endpoint.point.toString();
        }

        public Segment(FXLightningBoltCommon.BoltPoint start, FXLightningBoltCommon.BoltPoint end, float light, int segmentnumber, int splitnumber) {
            this.this$0 = FXLightningBoltCommon.this;
            this.startpoint = start;
            this.endpoint = end;
            this.light = light;
            this.segmentno = segmentnumber;
            this.splitno = splitnumber;
            this.calcDiff();
        }

        public Segment(WRVector3 start, WRVector3 end) {
            this(FXLightningBoltCommon.this.new BoltPoint(start, new WRVector3(0.0F, 0.0F, 0.0F)), FXLightningBoltCommon.this.new BoltPoint(end, new WRVector3(0.0F, 0.0F, 0.0F)), 1.0F, 0, 0);
        }
    }

    public class SegmentLightSorter implements Comparator {
        final FXLightningBoltCommon this$0 = FXLightningBoltCommon.this;

        public int compare(FXLightningBoltCommon.Segment o1, FXLightningBoltCommon.Segment o2) {
            return Float.compare(o2.light, o1.light);
        }

        public int compare(Object obj, Object obj1) {
            return this.compare((FXLightningBoltCommon.Segment)obj, (FXLightningBoltCommon.Segment)obj1);
        }
    }

    public class SegmentSorter implements Comparator {
        final FXLightningBoltCommon this$0 = FXLightningBoltCommon.this;

        public int compare(FXLightningBoltCommon.Segment o1, FXLightningBoltCommon.Segment o2) {
            int comp = Integer.compare(o1.splitno, o2.splitno);
            return comp == 0 ? Integer.compare(o1.segmentno, o2.segmentno) : comp;
        }

        public int compare(Object obj, Object obj1) {
            return this.compare((FXLightningBoltCommon.Segment)obj, (FXLightningBoltCommon.Segment)obj1);
        }
    }
}
