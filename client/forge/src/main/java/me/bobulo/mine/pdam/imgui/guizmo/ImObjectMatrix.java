package me.bobulo.mine.pdam.imgui.guizmo;

import java.util.Arrays;
import java.util.Objects;

/**
 * A simple wrapper for a 4x4 matrix to be used with the Guizmo manipulation functions.
 * <p>
 * This class is necessary because the Guizmo functions require a float array of length 16 to
 * represent the matrix.
 */
public final class ImObjectMatrix {

    private final float[] objectMat = new float[16];

    public float[] getData() {
        return objectMat;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImObjectMatrix)) return false;
        ImObjectMatrix that = (ImObjectMatrix) o;
        return Objects.deepEquals(objectMat, that.objectMat);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(objectMat);
    }

    @Override
    public String toString() {
        return "ImObjectMatrix{" +
          "objectMat=" + Arrays.toString(objectMat) +
          '}';
    }

}
