package net.tundra.gamegig2018;

import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.resources.sprites.Animation;
import net.tundra.core.scene.PhysicsObject;
import net.tundra.gamegig2018.particles.Explosion;
import net.tundra.gamegig2018.particles.Part;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Enemy extends PhysicsObject {
  private float gunAngle = 0f;
  private Animation running;
  private GameWorld world;
  private boolean slowed = false;

  public Enemy(GameWorld world, Vector2f position) {
    super(
        new Vector3f(position.x, position.y, 0),
        Model.CUBE,
        new Quaternionf(),
        new Vector3f(0.3f, 0.5f, 0.5f).mul(24f / 16f),
        1f,
        false);
    running = new Animation(GameWorld.ANDROID, 0, 0, 5, 0, true, 3);
    running.start();
    this.world = world;
  }

  @Override
  public void configure(RigidBodyConstructionInfo info) {
    info.angularDamping = 1f;
  }

  @Override
  public void update(Game game, float delta) throws TundraException {
    if (getPosition().z > 0.05) {
        System.out.println(getPosition().z);
        this.kill();
    }
    if( Math.abs(world.getPlayer().getPosition().x - getPosition().x) < 20) {
      running.update(delta);
      javax.vecmath.Vector3f velocity = new javax.vecmath.Vector3f();
      getBody().setAngularVelocity(new javax.vecmath.Vector3f());
      getBody().getLinearVelocity(velocity);
      getBody().setLinearVelocity(new javax.vecmath.Vector3f(-4f, velocity.y, 0f));
    }
  }

  @Override
  public void onCollision(PhysicsObject other) {
    if (other instanceof Player || other instanceof Bullet) {
      world.addObject(new Explosion(world, new Vector2f(getPosition().x, getPosition().y)));
      for (int i = 0; i < 5; i++)
        world.addObject(
            new Part(
                world, new Vector2f(getPosition().x, getPosition().y), GameWorld.ANDROID_PARTS));
      kill();
    }
  }

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.drawModel(
        Model.PLANE,
        running.currentFrame(),
        new Matrix4f().translate(getPosition()).scale(0.5f * 24f / 16f));
    graphics.drawModel(
        Model.PLANE,
        GameWorld.GUN.getSprite(1, 0),
        new Matrix4f()
            .translate(getPosition().add(0, 0, 0.005f))
            .scale(0.5f * 24f / 16f)
            .rotateZ(-gunAngle));
  }

  public boolean used() {
    return slowed;
  }

  public void expend() {
    slowed = true;
  }
}
