package net.tundra.gamegig2018.particles;

import java.util.Random;
import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.resources.sprites.Animation;
import net.tundra.core.scene.GameObject;
import net.tundra.gamegig2018.GameWorld;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Smoke extends GameObject {
  private static final Random RANDOM = new Random();
  private Vector3f position;
  private Vector2f velocity;
  private Animation smoke;

  public Smoke(GameWorld world, Vector2f position) {
    this(world, 0, new Vector3f(position.x, position.y, 0f));
  }

  public Smoke(GameWorld world, int start, Vector2f position) {
    this(world, start, new Vector3f(position.x, position.y, 0f));
  }

  public Smoke(GameWorld world, int start, Vector3f position) {
    this.position = new Vector3f(position);
    velocity = new Vector2f(-0.005f + RANDOM.nextFloat() * 0.01f, RANDOM.nextFloat() * 0.01f);
    if (start != 0) {
      velocity = new Vector2f(0);
      world.after(200, this::kill);
    }

    smoke = new Animation(GameWorld.SMOKE, start + RANDOM.nextInt(6 - start), 0, 7, 0, false, 12);
    smoke.start();
  }

  @Override
  public void update(Game game, float delta) throws TundraException {
    position.add(new Vector3f(velocity.x, velocity.y, 0).mul(delta));
    smoke.update(delta);
    if (!smoke.isPlaying()) kill();
  }

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.drawModel(
        Model.PLANE, smoke.currentFrame(), new Matrix4f().translate(position).scale(0.5f));
  }
}
