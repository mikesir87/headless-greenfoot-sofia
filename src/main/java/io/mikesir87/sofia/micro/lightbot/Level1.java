package io.mikesir87.sofia.micro.lightbot;

/**
 * Represents Level 1 of a Light-Bot-style game.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class Level1 extends Level {

  @Override
  protected void prepareWorld() {
    for (int i = 0; i < getWidth(); i++) {
      add(new Block(), i, 0);
      add(new Block(), i, 0);

      add(new Block(), i, getHeight() - 1);
      add(new Block(), i, getHeight() - 1);
    }
    add(new LightableTile(), 4, 3);
  }

}
