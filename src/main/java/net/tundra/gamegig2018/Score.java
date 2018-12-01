package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.GameState;
import net.tundra.core.TundraException;
import net.tundra.core.graphics.Graphics;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Score extends GameState {
  private static final String HELP = "[SPACE] TO RESTART", LASTED = "YOU BORROWED";
  private float opacity = 0f;
  private String scoreMessage;

  public Score(float score) {
    int seconds = (int) Math.floor(score / 1000f);
    int millis = (int) Math.floor((score % 1000f) / 10f);
    scoreMessage = seconds + ":" + ((Integer.toString(millis).length() == 1) ? "0" : "") + millis;
  }

  @Override
  public void init(Game game) throws TundraException {
    lerpOpacity();
  }

  private void lerpOpacity() {
    lerp(
        1000,
        f -> {
          opacity = f;
        },
        1f,
        0f,
        () -> {
          reverseLerpOpacity();
        });
  }

  private void reverseLerpOpacity() {
    lerp(
        1000,
        f -> {
          opacity = f;
        },
        0f,
        1f,
        () -> {
          lerpOpacity();
        });
  }

  @Override
  public void update(Game game, float delta) throws TundraException {
    if (game.getInput().isKeyPressed(org.lwjgl.input.Keyboard.KEY_SPACE)) {
      game.addState(new GameWorld());
      game.enterState(1);
    }
  }

  @Override
  public void render(Game game, Graphics graphics) throws TundraException {
    graphics.setClearColour(new Vector3f(1f, 1f, 1f));
    graphics.drawStringFlash("LIVING", GameWorld.FONT_BIG, 20, 20, new Vector4f(0f, 0f, 0f, 1f));
    graphics.drawStringFlash("ON", GameWorld.FONT_BIG, 20, 90, new Vector4f(0f, 0f, 0f, 1f));
    graphics.drawStringFlash("BORROWED", GameWorld.FONT_BIG, 20, 160, new Vector4f(0f, 0f, 0f, 1f));
    graphics.drawStringFlash("TIME", GameWorld.FONT_BIG, 20, 230, new Vector4f(0f, 0f, 0f, 1f));

    graphics.drawStringFlash(
        LASTED,
        GameWorld.FONT,
        game.getWidth() / 2 - LASTED.length() * GameWorld.FONT.getCharacterWidth() / 2,
        game.getHeight() / 2 - 100,
        new Vector4f(0f, 0f, 0f, 1f));

    graphics.drawStringFlash(
        scoreMessage,
        GameWorld.FONT_BIG,
        game.getWidth() / 2 - scoreMessage.length() * GameWorld.FONT_BIG.getCharacterWidth() / 2,
        game.getHeight() / 2 - 70,
        new Vector4f(0f, 0f, 0f, 1f));

    graphics.drawStringFlash(
        HELP,
        GameWorld.FONT,
        game.getWidth() / 2 - HELP.length() * GameWorld.FONT.getCharacterWidth() / 2,
        game.getHeight() / 2,
        new Vector4f(0f, 0f, 0f, opacity));
  }

  @Override
  public int getID() {
    return 3;
  }
}
