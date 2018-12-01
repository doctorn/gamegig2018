package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.scene.ShakableCamera;
import org.joml.Vector3f;

public class PlayerCamera extends ShakableCamera {
  private GameWorld world;
  private Vector3f lastTarget;

  public PlayerCamera(GameWorld world) {
    this.world = world;
    setPosition(
        new Vector3f(
            world.getPlayer().getPosition().x - 10f, world.getPlayer().getPosition().y + 2f, 20f));
  }

  @Override
  public Vector3f getTarget() {
    if (world.getPlayer().getPosition().y > -5f) lastTarget = world.getPlayer().getPosition();
    return lastTarget;
  }

  @Override
  public void update(Game game, float delta) {
    super.update(game, delta);
    if (world.getPlayer().getPosition().y > -5f) {
      setPosition(
          getPosition()
              .add(
                  world
                      .getPlayer()
                      .getPosition()
                      .add(0, 2f, 0)
                      .sub(getPosition())
                      .mul(1, 1, 0)
                      .mul(0.01f)));
      setTarget(world.getPlayer().getPosition());
    }
  }
}
