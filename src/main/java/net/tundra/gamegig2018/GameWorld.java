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
import net.tundra.core.scene.FixedLight;
import net.tundra.core.scene.Light;
import net.tundra.gamegig2018.particles.Explosion;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameWorld extends GameState {
  public static Font FONT;
  public static SpriteSheet SMOKE, EXPLOSION;
  private Camera camera, shadow;
  private Player player;
  private List<Enemy> enemies = new ArrayList<>();
  private Light main;

  @Override
  public void init(Game game) throws TundraException {
    FONT = new Font(new SpriteSheet("res/font.png", 20, 22));
    SMOKE = new SpriteSheet("res/smoke.png", 16, 16);
    EXPLOSION = new SpriteSheet("res/explosion.png", 128, 128);

    player = new Player(new Vector3f());
    addObject(player);
    camera = new net.tundra.core.scene.OrbitalCamera(player, 10f);
    addCamera(camera);
    activate(camera);

    main = new FixedLight(new Vector3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
    addLight(main);
    shadow = new ShadowCamera(player, new Vector3f(25, 25f, 25f));
    addCamera(shadow);
    enableShadowMapping(shadow, main);

    setLighting(true);
    toggleDebug();
  }

  @Override
  public void update(Game game, float delta) throws TundraException {
    if (game.getInput().isKeyPressed(org.lwjgl.input.Keyboard.KEY_SPACE)) {
      addObject(new Explosion(this, new Vector2f()));
    }
  }

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.setClearColour(new Vector3f(0.8f, 0.8f, 0.8f));
  }

  @Override
  public int getID() {
    return 1;
  }
}
