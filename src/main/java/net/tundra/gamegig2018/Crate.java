package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.scene.PhysicsObject;
import net.tundra.gamegig2018.particles.Explosion;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Crate extends PhysicsObject {
  private GameWorld world;

  public Crate(GameWorld world, Vector3f position) {
    super(position, Model.CUBE, new Quaternionf(), new Vector3f(1f, 1f, 1f), 3f, false);
    this.world = world;
  }

  @Override
  public void update(Game game, float delta) throws TundraException {}

  @Override
  public void onCollision(PhysicsObject other) {
    // TODO parts
    if (other instanceof Player) kill();
    else if (other instanceof Bullet) {
      world.addObject(new Explosion(world, new Vector2f(getPosition().x, getPosition().y)));
      kill();
    }
  }

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.drawModel(
        Model.CUBE, GameWorld.CRATE, new Matrix4f().translate(getPosition()).rotate(getRotation()));
  }
}
