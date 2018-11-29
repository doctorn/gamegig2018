package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.GameState;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.sprites.Font;
import net.tundra.core.resources.sprites.SpriteSheet;
import org.joml.Vector3f;

public class GameWorld extends GameState {
  public static Font FONT;

  @Override
  public void init(Game game) throws TundraException {
    FONT = new Font(new SpriteSheet("res/font.png", 20, 22));
  }

  @Override
  public void update(Game game, float delta) throws TundraException {}

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.setClearColour(new Vector3f(0f, 0f, 0f));
    graphics.drawString("GAMEGIG2018", FONT, 20, 20);
  }

  @Override
  public int getID() {
    return 1;
  }
}
