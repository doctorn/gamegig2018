package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.models.Model;
import net.tundra.core.scene.PhysicsObject;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Player extends PhysicsObject {
  public Player(Vector3f position) {
    super(position, Model.CUBE, new Quaternionf(), new Vector3f(0.5f, 0.5f, 0.5f), 1f);
  }

  @Override
  public void update(Game game, float delta) throws TundraException {}

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {}
}
