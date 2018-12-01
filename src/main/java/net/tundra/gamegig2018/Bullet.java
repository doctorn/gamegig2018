package net.tundra.gamegig2018;

import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.scene.PhysicsObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Bullet extends PhysicsObject {
  private GameWorld world;
  private Vector3f velocity;

  public Bullet(GameWorld world, Vector2f position, Vector2f velocity) {
    super(
        new Vector3f(position.x, position.y, 0),
        Model.CUBE,
        new Quaternionf(),
        new Vector3f(0.1f, 0.1f, 0.1f).mul(24f / 16f),
        0.1f,
        false);
    this.getBody().setGravity(new javax.vecmath.Vector3f());
    this.velocity = new Vector3f(velocity.x, velocity.y, 0).normalize(10f);
    this.world = world;
    world.after(10000, this::kill);
  }

  @Override
  public void configure(RigidBodyConstructionInfo info) {
    info.angularDamping = 1f;
  }

  @Override
  public void update(Game game, float delta) throws TundraException {
    javax.vecmath.Vector3f velocity = new javax.vecmath.Vector3f();
    getBody().setAngularVelocity(new javax.vecmath.Vector3f());
    getBody().getLinearVelocity(velocity);
    getBody().setLinearVelocity(new javax.vecmath.Vector3f(velocity.x, velocity.y, velocity.z));
  }

  @Override
  public void onCollision(PhysicsObject other) {
    if (other instanceof ForegroundBuilding) kill();
  }

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    boolean lighting = world.lightingEnabled();
    world.setLighting(false);
    graphics.drawModel(
        Model.PLANE,
        GameWorld.BULLET,
        new Matrix4f().translate(getPosition()).scale(0.5f * 12f / 16f, 0.5f * 8f / 16f, 1f));
    world.setLighting(lighting);
  }
}
