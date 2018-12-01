package net.tundra.gamegig2018;

import java.util.ArrayList;
import java.util.List;
import net.tundra.core.Game;
import net.tundra.core.GameState;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.sprites.Font;
import net.tundra.core.resources.sprites.SpriteSheet;
import net.tundra.core.scene.Camera;
import org.joml.Vector3f;

public class GameWorld extends GameState {
  public static Font FONT;
  private Camera camera;
  private Player player;
  private List<Enemy> enemies = new ArrayList<>();

  @Override
  public void init(Game game) throws TundraException {
    FONT = new Font(new SpriteSheet("res/font.png", 20, 22));
    player = new Player(new Vector3f());
    addObject(player);
    camera = new net.tundra.core.scene.OrbitalCamera(player, 10f);
    addCamera(camera);
    activate(camera);
    toggleDebug();
  }

  @Override
  public void update(Game game, float delta) throws TundraException {}

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.setClearColour(new Vector3f(0f, 0f, 0f));
  }

  @Override
  public int getID() {
    return 1;
  }
}
