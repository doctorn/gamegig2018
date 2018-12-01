package net.tundra.gamegig2018;

import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.resources.sprites.Animation;
import net.tundra.core.scene.PhysicsObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Player extends PhysicsObject {
  private int jumps = 0;
  private float gunAngle = 0f;
  private Animation running;
  private GameWorld world;

  public Player(GameWorld world, Vector3f position) {
    super(
        position,
        Model.CUBE,
        new Quaternionf(),
        new Vector3f(0.3f, 0.5f, 0.5f).mul(24f / 16f),
        1f,
        false);
    running = new Animation(GameWorld.TIM, 0, 3, 5, 3, true, 12);
    running.start();
    this.world = world;
    lerpGun();
  }

  private void lerpGun() {
    world.lerp(
        200,
        f -> {
          gunAngle = f;
        },
        -(float) Math.PI / 16,
        (float) Math.PI / 16,
        this::reverseLerpGun);
  }

  private void reverseLerpGun() {
    world.lerp(
        800,
        f -> {
          gunAngle = f;
        },
        (float) Math.PI / 16,
        -(float) Math.PI / 16,
        this::lerpGun);
  }

  @Override
  public void configure(RigidBodyConstructionInfo info) {
    info.angularDamping = 1f;
  }

  @Override
  public void update(Game game, float delta) throws TundraException {
    running.update(delta);
    javax.vecmath.Vector3f velocity = new javax.vecmath.Vector3f();
    getBody().setAngularVelocity(new javax.vecmath.Vector3f());
    getBody().getLinearVelocity(velocity);
    getBody().setLinearVelocity(new javax.vecmath.Vector3f(8f, velocity.y, 0f));

    if (game.getInput().isKeyPressed(org.lwjgl.input.Keyboard.KEY_SPACE) && jumps < 2) {
      getBody().applyCentralImpulse(new javax.vecmath.Vector3f(0f, 5f, 0f));
      jumps++;
    }
  }

  @Override
  public void onCollision(PhysicsObject other) {
    if (other instanceof ForegroundBuilding) {
      jumps = 0;
      ForegroundBuilding building = (ForegroundBuilding) other;
      if (building.getCollapsable())
        world.after(
            500,
            () -> {
              building.setToCollapse(true);
            });
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
        GameWorld.GUN.getSprite(0, 0),
        new Matrix4f()
            .translate(getPosition().add(0, 0, 0.005f))
            .scale(0.5f * 24f / 16f)
            .rotateZ(gunAngle));
  }
}
