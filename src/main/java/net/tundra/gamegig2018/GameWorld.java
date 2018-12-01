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
  private List<ForegroundBuilding> foregroundBuildings = new ArrayList<>();

  @Override
  public void init(Game game) throws TundraException {
    FONT = new Font(new SpriteSheet("res/font.png", 20, 22));
    player = new Player(new Vector3f());
    addObject(player);
    camera = new net.tundra.core.scene.OrbitalCamera(player, 10f);

    for(int i = -1; i < 2; i++) {
      ForegroundBuilding fb = new ForegroundBuilding(new Vector3f((float)i * 3, -2, 0), i == 0, 1, 1);
      addObject(fb);
      foregroundBuildings.add(fb);
    }

    addCamera(camera);
    activate(camera);
    togglePhysics();
    toggleDebug();
  }

  @Override
  public void update(Game game, float delta) throws TundraException {
    if(game.getInput().isKeyPressed(org.lwjgl.input.Keyboard.KEY_C)) {
      System.out.println("Collapsing buildings");
      for(ForegroundBuilding fb : foregroundBuildings) {
        if (fb.getCollapsable()) {
          fb.setToCollapse(true);
        }
      }
    }
  }

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.setClearColour(new Vector3f(0f, 0f, 0f));
  }

  @Override
  public int getID() {
    return 1;
  }
}
