package io.mikesir87.sofia.micro.lightbot;

/**
 * Represents Level 3 of a Light-Bot-style game.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class Level3 extends Level {

  @Override
  protected void prepareWorld() {
    for (int i = 0; i < getHeight(); i++) {
      add(new Block(), 2, i);
    }

    for (int i = 2; i < 5; i++) {
      add(new LightableTile(), 4, i);
    }
  }
}
