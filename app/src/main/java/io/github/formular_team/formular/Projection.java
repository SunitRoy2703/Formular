package io.github.formular_team.formular;

import io.github.formular_team.formular.math.Matrix4;
import io.github.formular_team.formular.math.Ray;
import io.github.formular_team.formular.math.Vector2;
import io.github.formular_team.formular.math.Vector3;

public final class Projection {
    private Projection() {}

    public static Matrix4 projection(final Matrix4 model, final Matrix4 view, final Matrix4 projection) {
        return model.clone().multiply(view).multiply(projection);
    }

    // return new Vector2((coords.x() + 1.0F) / 2.0F * viewport.width(), (1.0F - coords.y()) / 2.0F * viewport.height());
    public static Vector2 project(final Matrix4 mvp, final Vector3 point) {
        final Vector3 coords = point.clone().applyProjection(mvp);
        return new Vector2(coords.getX(), coords.getY());
    }

    public static Ray unproject(final Vector2 point, final Matrix4 projectionMatrix, final Matrix4 viewMatrix) {
        final Matrix4 viewProjMtx = projectionMatrix.clone().multiply(viewMatrix);
        return unproject(point, viewProjMtx);
    }

    // final float x = point.x() / viewport.width() * 2.0F - 1.0F;
    // final float y = 1.0F - viewport.height() / viewport.y() * 2.0F;
    public static Ray unproject(final Vector2 point, final Matrix4 viewProjMtx) {
        final Vector3 farScreenPoint = new Vector3(point.getX(), point.getY(), 1.0F);
        final Vector3 nearScreenPoint = new Vector3(point.getX(), point.getY(), -1.0F);
        final Matrix4 invertedProjectionMatrix = new Matrix4().getInverse(viewProjMtx);
        final Vector3 origin = nearScreenPoint.clone().applyProjection(invertedProjectionMatrix);
        final Vector3 direction = farScreenPoint.clone().applyProjection(invertedProjectionMatrix);
        return new Ray(origin, direction.sub(origin).normalize());
    }
}
