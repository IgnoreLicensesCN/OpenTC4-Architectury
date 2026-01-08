package thaumcraft.common.multiparts;

import com.linearity.opentc4.VecTransformations;

import java.util.Objects;

public record MultipartMatchInfo(VecTransformations.Rotation3D usingRotation, VecTransformations.Mirror3D usingMirror) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MultipartMatchInfo(VecTransformations.Rotation3D rotation, VecTransformations.Mirror3D mirror))) return false;
        return usingMirror == mirror && usingRotation == rotation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usingRotation, usingMirror);
    }
}
