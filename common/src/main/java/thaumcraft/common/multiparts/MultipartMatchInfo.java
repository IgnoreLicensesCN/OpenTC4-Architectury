package thaumcraft.common.multiparts;

import com.linearity.opentc4.VecTransformations;

public class MultipartMatchInfo {

    private final VecTransformations.Rotation3D usingRotation;
    private final VecTransformations.Mirror3D usingMirror;
    public VecTransformations.Rotation3D usingRotation() {
        return usingRotation;
    }
    public VecTransformations.Mirror3D usingMirror() {
        return usingMirror;
    }

    private MultipartMatchInfo(VecTransformations.Rotation3D usingRotation, VecTransformations.Mirror3D usingMirror){
        this.usingRotation = usingRotation;
        this.usingMirror = usingMirror;
    }

    private static final MultipartMatchInfo[][] CACHE =
            new MultipartMatchInfo[
                    VecTransformations.Rotation3D.values().length
                    ][
                    VecTransformations.Mirror3D.values().length
                    ];
    static {
        for (int i = 0; i < CACHE.length; i++) {
            for (int j = 0; j < CACHE[i].length; j++) {
                CACHE[i][j] = new MultipartMatchInfo(VecTransformations.Rotation3D.values()[i], VecTransformations.Mirror3D.values()[j]);
            }
        }
    }
    public static MultipartMatchInfo of(VecTransformations.Rotation3D r, VecTransformations.Mirror3D m) {
        return CACHE[r.ordinal()][m.ordinal()];
    }

}
