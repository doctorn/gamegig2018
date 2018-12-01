package net.tundra.gamegig2018;

import net.tundra.core.Game;
import net.tundra.core.TundraException;

public class Main extends Game {
  public Main() {
    super(800, 600, "gamegig2018", false);
  }

  @Override
  public void initStates() throws TundraException {
    addState(new Tundra());
    addState(new GameWorld());
    enterState(1);
  }

  public static void main(String args[]) throws TundraException {
    Game game = new Main();
    game.start();
  }
}
