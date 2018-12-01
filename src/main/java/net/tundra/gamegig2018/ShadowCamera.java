package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.scene.Trackable;
import net.tundra.core.scene.TrackingCamera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ShadowCamera extends TrackingCamera {
  public ShadowCamera(Trackable tracked, Vector3f offset) {
    super(tracked, offset);
  }

  @Override
  public Matrix4f getViewProjectionMatrix(int width, int height) {
    return new Matrix4f()
        .ortho(-50f, 50f, -50f, 50f, 0.01f, 50f)
        .lookAt(getPosition(), getTarget(), getUp());
  }

  @Override
  public void update(Game game, float delta) {
    setPosition(getPosition());
  }
}
