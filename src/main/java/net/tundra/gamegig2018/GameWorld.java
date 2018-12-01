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
import net.tundra.core.resources.models.Model;
import net.tundra.core.resources.sprites.Font;
import net.tundra.core.resources.sprites.Sprite;
import net.tundra.core.resources.sprites.SpriteSheet;
import net.tundra.core.scene.Camera;
import net.tundra.core.scene.FixedLight;
import net.tundra.core.scene.Light;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameWorld extends GameState {
  public static Font FONT;
  public static SpriteSheet SMOKE,
      EXPLOSION,
      TIM,
      ANDROID,
      GUN,
      ANDROID_PARTS,
      BUILDING,
      CRATE_PARTS;
  public static Sprite BULLET,
      CRATE,
      FILTER,
      BUILDING_10x10,
      BUILDING_5x10,
      BUILDING_8x10,
      BUILDING_12x10,
      BUILDING_16x10;
  private boolean timeSlowed = false;
  private Camera camera, shadow;
  private Player player;
  private Enemy triggered;
  private List<Enemy> enemies = new ArrayList<>();
  private List<Crate> crates = new ArrayList<>();
  private List<ForegroundBuilding> foregroundBuildings = new ArrayList<>();
  private List<List<BackgroundBuilding>> backgroundBuildingsList = new ArrayList<>();
  private float slowBarWidth;
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
    ANDROID_PARTS = new SpriteSheet("res/android.png", 8, 8);
    BUILDING = new SpriteSheet("res/buildings.png", 32, 32);
    GUN = new SpriteSheet("res/gun.png", 24, 24);
    BULLET = new Sprite("res/bullet.png");
    CRATE = new Sprite("res/crate.png");
    CRATE_PARTS = new SpriteSheet("res/crate.png", 8, 8);
    FILTER = new Sprite("res/filter.png");

    BUILDING_5x10 = new Sprite("res/building5x10.png");
    BUILDING_10x10 = new Sprite("res/building8x10.png");
    BUILDING_8x10 = new Sprite("res/building10x10.png");
    BUILDING_12x10 = new Sprite("res/building12x10.png");
    BUILDING_16x10 = new Sprite("res/building16x10.png");

    FILTER.enableSmoothing();

    player = new Player(this, new Vector2f(0f, 2f));
    addObject(player);
    // addObject(new Crate(this, new Vector3f(20f, 2f, 0f)));
    camera = new PlayerCamera(this);

    for (int i = -1; i < 2; i++) {
      ForegroundBuilding fb =
          new ForegroundBuilding(new Vector3f((float) i * 22, -10, 0), false, 10, 10, 10);
      addObject(fb);
      foregroundBuildings.add(fb);
    }

    for (int j = 0; j < 2; j++) {
      List<BackgroundBuilding> backgroundBuildings = new ArrayList<>();
      for (int i = -3; i < 2; i++) {
        int height = random.nextInt(5);
        BackgroundBuilding bb =
            new BackgroundBuilding(
                new Vector3f((float) i * 22 - j * 3, -10 + (1 + j) * height, (float) -30 * (j + 1)),
                10,
                10,
                10);
        addObject(bb);
        backgroundBuildings.add(bb);
      }
      backgroundBuildingsList.add(backgroundBuildings);
    }

    List<BackgroundBuilding> backgroundBuildings = new ArrayList<>();
    for (int i = -3; i < 2; i++) {
      int height = random.nextInt(5);
      BackgroundBuilding bb =
          new BackgroundBuilding(
              new Vector3f(
                  (float) i * 22 - 3 * backgroundBuildingsList.size(), height, (float) -90),
              10,
              20,
              10);
      addObject(bb);
      backgroundBuildings.add(bb);
    }
    backgroundBuildingsList.add(backgroundBuildings);

    addCamera(camera);
    activate(camera);

    main = new FixedLight(new Vector3f(-1f, -1f, -2f), new Vector3f(0.8f, 0.8f, 0.8f));
    addLight(main);
    shadow = new ShadowCamera(player, new Vector3f(12.5f, 12.5f, 25f));
    addCamera(shadow);
    enableShadowMapping(shadow, main);

    setLighting(true);
    // toggleDebug();
    togglePhysics();
  }

  @Override
  public void update(Game game, float delta) throws TundraException {
    List<ForegroundBuilding> fbToCull =
        foregroundBuildings
            .stream()
            .filter(
                b -> b.getPosition().x + (float) b.getWidth() < camera.getPosition().x - cullOffset)
            .collect(Collectors.toList());

    for (ForegroundBuilding fb : fbToCull) {
      foregroundBuildings.remove(fb);
      fb.kill();
    }

    ForegroundBuilding currentFore = foregroundBuildings.get(foregroundBuildings.size() - 1);
    if (currentFore.getPosition().x - currentFore.getWidth()
        < camera.getPosition().x + cullOffset) {
      boolean collapse = random.nextInt(5) == 0;
      int width = collapse ? 5 : 8 + 4 * random.nextInt(2);
      int height = random.nextInt(5) - 2;
      int gap = 4;
      if (currentFore.getPosition().y + height <= 5) {
        height = -height;
      }
      ForegroundBuilding fb =
          new ForegroundBuilding(
              new Vector3f(
                  currentFore.getPosition().x + currentFore.getWidth() + width + gap,
                  currentFore.getPosition().y + height,
                  0),
              collapse,
              width,
              10,
              10);
      addObject(fb);
      foregroundBuildings.add(fb);

      if (!collapse) {
        // spawn crate(s)
        int numCrates = random.nextInt(3) + 1;
        boolean pathCrate = (random.nextInt(8) == 0);
        for (int i = 0; i < numCrates; i++) {
          Crate crate =
              new Crate(
                  this,
                  new Vector3f(
                      fb.getPosition().x + (fb.getWidth() - 1) * (2 * random.nextFloat() - 1),
                      fb.getPosition().y + 10f,
                      fb.getPosition().z + 3f * (i % 2 == 0 ? 1f : -1f)),
                  random.nextFloat());
          addObject(crate);
          crates.add(crate);
        }

        if (pathCrate) {
          Crate crate =
              new Crate(
                  this,
                  new Vector3f(
                      fb.getPosition().x + (fb.getWidth() - 1) * (2 * random.nextFloat() - 1),
                      fb.getPosition().y + 10f,
                      fb.getPosition().z),
                  random.nextFloat());
          addObject(crate);
          crates.add(crate);
        }

        // spawn enemy
        if (!(random.nextInt(4) == 0) && !pathCrate) {
          Enemy enemy =
              new Enemy(
                  this,
                  new Vector2f(
                      fb.getPosition().x
                          + fb.getWidth() * (-1 + 0.25f + 0.75f * random.nextFloat()),
                      fb.getPosition().y + fb.getHeight() + 0.05f));
          addObject(enemy);
          enemies.add(enemy);
        }
      }
    }

    for (int j = 0; j < backgroundBuildingsList.size(); j++) {
      List<BackgroundBuilding> backgroundBuildings = backgroundBuildingsList.get(j);

      List<BackgroundBuilding> bbToCull =
          backgroundBuildings
              .stream()
              .filter(
                  b ->
                      b.getPosition().x + (float) b.getWidth()
                          < camera.getPosition().x - cullOffset)
              .collect(Collectors.toList());

      for (BackgroundBuilding bb : bbToCull) {
        backgroundBuildings.remove(bb);
        bb.kill();
      }

      BackgroundBuilding currentBack = backgroundBuildings.get(backgroundBuildings.size() - 1);
      if (currentBack.getPosition().x - currentBack.getWidth()
          < camera.getPosition().x + cullOffset) {
        int width = 4 * (2 + random.nextInt(3));
        int height = (2 * (1 + j) * random.nextInt(5)) + ((j == 3) ? 0 : -10);
        int gap = 4;
        BackgroundBuilding bb =
            new BackgroundBuilding(
                new Vector3f(
                    currentBack.getPosition().x + currentBack.getWidth() + width + gap,
                    height,
                    (float) -30 * (j + 1)),
                width,
                20,
                10);
        addObject(bb);
        backgroundBuildings.add(bb);
      }
    }

    Iterator<Enemy> iter = enemies.iterator();
    while (iter.hasNext()) {
      if (iter.next().dying()) iter.remove();
    }

    if (!timeSlowed) {
      if (!player.falling()) {
        for (Enemy enemy : enemies) {
          if (!enemy.used() && enemy.getPosition().sub(player.getPosition()).length() < 10f) {
            enemy.expend();
            triggered = enemy;
            lerp(
                50,
                f -> game.setTimescale(f),
                1f,
                0.2f,
                () -> {
                  timeSlowed = true;
                  lerp(
                      1000,
                      f -> {
                        slowBarWidth = f;
                      },
                      game.getWidth(),
                      0,
                      () -> {
                        snapOutTimeSlow(game);
                      });
                });
          }
        }
      }
    } else if (triggered.dying()) snapOutTimeSlow(game);
  }

  public void snapOutTimeSlow(Game game) {
    timeSlowed = false;
    game.setTimescale(1f);
  }

  public boolean timeSlowed() {
    return timeSlowed;
  }

  public Player getPlayer() {
    return player;
  }

  public void shake(float timeout) {
    ((PlayerCamera) camera).shake(timeout);
  }

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.setClearColour(new Vector3f(1f, 0.71f, 0.76f));
    graphics.drawImage(FILTER, 0, 0, game.getWidth(), game.getHeight());
    graphics.setColour(new Vector3f());
    graphics.drawModel(
        Model.PLANE,
        new Matrix4f()
            .translate(camera.getPosition().mul(1, 0, 1).add(0, -15f, 0))
            .rotateX(-(float) Math.PI / 2)
            .scale(500f, 500f, 500f));
    if (timeSlowed) {
      graphics.setColour(new Vector3f(1f, 1f, 1f));
      graphics.fillRect(
          Math.round(game.getWidth() / 2 - slowBarWidth / 2),
          game.getHeight() - 20,
          Math.round(slowBarWidth),
          20);
    }
  }

  @Override
  public int getID() {
    return 1;
  }
}
