package net.tundra.gamegig2018.particles;

import java.util.Random;
import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.resources.sprites.Sprite;
import net.tundra.core.resources.sprites.SpriteSheet;
import net.tundra.core.scene.Event;
import net.tundra.core.scene.PhysicsObject;
import net.tundra.gamegig2018.GameWorld;
import net.tundra.gamegig2018.Player;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Part extends PhysicsObject {
  private static final Random RANDOM = new Random();
  private Sprite sprite;

  public Part(GameWorld world, Vector2f position, SpriteSheet sheet) {
    super(
        new Vector3f(position.x, position.y, 0f),
        Model.CUBE,
        new Quaternionf(),
        new Vector3f(0.1f, 0.1f, 0.1f),
        0.1f);
    sprite = sheet.getSprite(RANDOM.nextInt(sheet.getRows()), RANDOM.nextInt(sheet.getColumns()));
    getBody()
        .applyCentralImpulse(
            new javax.vecmath.Vector3f(
                RANDOM.nextFloat() * 1f - 0.5f,
                0.5f + 0.5f * RANDOM.nextFloat(),
                RANDOM.nextFloat() * 1f - 0.5f));
    Event event =
        world.every(
            100,
            () -> {
              world.addObject(new Smoke(world, 4, getPosition()));
            });
    world.after(2000, event::kill);
    world.after(20000, this::kill);
  }

  @Override
  public void onCollision(PhysicsObject other) {
    if (other instanceof Player) kill();
  }

  @Override
  public void update(Game game, float delta) throws TundraException {}

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.drawModel(
        Model.PLANE,
        sprite,
        new Matrix4f()
            .translate(getPosition())
            .scale(sprite.getWidth() / 32f, sprite.getHeight() / 32f, 1f));
  }
}
