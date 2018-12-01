package net.tundra.gamegig2018;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.tundra.core.Game;
import net.tundra.core.GameState;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import net.tundra.core.resources.sprites.Font;
import net.tundra.core.resources.sprites.Sprite;
import net.tundra.core.resources.sprites.SpriteSheet;
import net.tundra.core.scene.Camera;
import net.tundra.core.scene.FixedLight;
import net.tundra.core.scene.Light;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameWorld extends GameState {
  public static Font FONT;
  public static SpriteSheet SMOKE, EXPLOSION, TIM, ANDROID, GUN;
  public static Sprite BULLET;
  private boolean timeSlowed = false;
  private Camera camera, shadow;
  private Player player;
  private List<Enemy> enemies = new ArrayList<>();
  private List<ForegroundBuilding> foregroundBuildings = new ArrayList<>();
  private List<List<BackgroundBuilding>> backgroundBuildingsList = new ArrayList<>();
  private Light main;
  private static final float cullOffset = 100;
  private static final Random random = new Random();

  @Override
  public void init(Game game) throws TundraException {
    FONT = new Font(new SpriteSheet("res/font.png", 20, 22));
    SMOKE = new SpriteSheet("res/smoke.png", 16, 16);
    EXPLOSION = new SpriteSheet("res/explosion.png", 128, 128);
    TIM = new SpriteSheet("res/timothy.png", 24, 24);
    ANDROID = new SpriteSheet("res/android.png", 24, 24);
    GUN = new SpriteSheet("res/gun.png", 24, 24);
    BULLET = new Sprite("res/bullet.png");

    player = new Player(this, new Vector2f(0f, 2f));
    addObject(player);
    Enemy test = new Enemy(this, new Vector2f(20f, 2f));
    addObject(test);
    enemies.add(test);
    camera = new net.tundra.core.scene.OrbitalCamera(player, 30f);

    for (int i = -1; i < 2; i++) {
      ForegroundBuilding fb =
          new ForegroundBuilding(new Vector3f((float) i * 22, -10, 0), false, 10, 10, 10);
      addObject(fb);
      foregroundBuildings.add(fb);
    }

    for(int j = 0; j < 2; j++) {
      List<BackgroundBuilding> backgroundBuildings = new ArrayList<>();
      for (int i = -1; i < 2; i++) {
        BackgroundBuilding bb =
            new BackgroundBuilding(new Vector3f((float) i * 22, -10, (float)-30*(j + 1)), 10, 10, 10);
        addObject(bb);
        backgroundBuildings.add(bb);
      }
      backgroundBuildingsList.add(backgroundBuildings);
    }

    addCamera(camera);
    activate(camera);

    main = new FixedLight(new Vector3f(1f, 1f, -1f), new Vector3f(0.5f, 0.5f, 0.5f));
    addLight(main);
    shadow = new ShadowCamera(player, new Vector3f(25, 25f, 25f));
    addCamera(shadow);
    enableShadowMapping(shadow, main);

    setLighting(true);
    toggleDebug();
    togglePhysics();
  }

  @Override
  public void update(Game game, float delta) throws TundraException {
    List<ForegroundBuilding> fbToCull = foregroundBuildings.stream()
        .filter(b -> b.getPosition().x + (float)b.getWidth() < camera.getPosition().x - cullOffset)
        .collect(Collectors.toList());

    for(ForegroundBuilding fb : fbToCull) {
      foregroundBuildings.remove(fb);
      fb.kill();
    }

    ForegroundBuilding currentFore = foregroundBuildings.get(foregroundBuildings.size() - 1);
    if(currentFore.getPosition().x - currentFore.getWidth() < camera.getPosition().x + cullOffset) {
      int width = random.nextInt(5) == 0 ? 7 : 10 * (1 + random.nextInt(2));
      int gap = 4;
      System.out.println(width);
      ForegroundBuilding fb =
          new ForegroundBuilding(new Vector3f( currentFore.getPosition().x + currentFore.getWidth() + width + gap, -10, 0), width == 7, width, 10, 10);
      addObject(fb);
      foregroundBuildings.add(fb);
    }

    for(List<BackgroundBuilding> backgroundBuildings : backgroundBuildingsList) {
      List<BackgroundBuilding> bbToCull = backgroundBuildings.stream()
          .filter(b -> b.getPosition().x + (float) b.getWidth() < camera.getPosition().x - cullOffset)
          .collect(Collectors.toList());

      for (BackgroundBuilding bb : bbToCull) {
        backgroundBuildings.remove(bb);
        bb.kill();
      }

      BackgroundBuilding currentBack = backgroundBuildings.get(backgroundBuildings.size() - 1);
      if (currentBack.getPosition().x - currentBack.getWidth() < camera.getPosition().x + cullOffset) {
        for (int j = 0; j < 4; j++) {
          BackgroundBuilding bb =
              new BackgroundBuilding(new Vector3f(currentBack.getPosition().x + 22, -10, (float) -30 * (j + 1)), 10, 10, 10);
          addObject(bb);
          backgroundBuildings.add(bb);
        }
      }
    }

    Iterator<Enemy> iter = enemies.iterator();
    while (iter.hasNext()) {
      if (iter.next().dying()) iter.remove();
    }

    if (!timeSlowed) {
      for (Enemy enemy : enemies) {
        if (enemy.getPosition().sub(player.getPosition()).length() < 10f) {
          timeSlowed = true;
          lerp(50, f -> game.setTimescale(f), 1f, 0.2f);
          after(
              /* TODO */ 1000,
              () -> {
                timeSlowed = false;
                lerp(50, f -> game.setTimescale(f), 0.2f, 1f);
              });
        }
      }
    } else {
      boolean found = false;
      for (Enemy enemy : enemies) {
        if (enemy.getPosition().sub(player.getPosition()).length() < 7.5f) {
          found = true;
          break;
        }
      }
      if (!found) {
        timeSlowed = false;
        lerp(50, f -> game.setTimescale(f), 0.2f, 1f);
      }
    }
  }

  public boolean timeSlowed() {
    return timeSlowed();
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
