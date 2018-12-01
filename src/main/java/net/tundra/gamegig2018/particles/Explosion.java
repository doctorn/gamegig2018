package net.tundra.gamegig2018.particles;

import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.resources.sprites.Animation;
import net.tundra.core.scene.FixedLight;
import net.tundra.core.scene.GameObject;
import net.tundra.core.scene.Light;
import net.tundra.gamegig2018.GameWorld;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Explosion extends GameObject {
  private Vector3f position;
  private Animation explosion;
  private GameWorld world;
  private Light light;

  public Explosion(GameWorld world, Vector2f position) {
    this.world = world;
    this.position = new Vector3f(position.x, position.y, 1f);
    // TODO screenshake
    explosion = new Animation(GameWorld.EXPLOSION, 0, 0, 1, 0, false, 12);
    explosion.start();
    light = new FixedLight(position.x, position.y, 1f, 1f, 1f, 1f);
    world.addLight(light);
    world.shake(500);
  }

  @Override
  public void update(Game game, float delta) {
    explosion.update(delta);
    if (!explosion.isPlaying()) {
      for (int i = 0; i < 25; i++)
        world.addObject(new Smoke(world, new Vector2f(position.x, position.y)));
      light.kill();
      kill();
    }
  }

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    boolean lighting = world.lightingEnabled();
    world.setLighting(false);
    graphics.drawModel(
        Model.PLANE, explosion.currentFrame(), new Matrix4f().translate(position).scale(4f));
    world.setLighting(lighting);
  }
}
