package io.mikesir87.sofia.micro.lightbot;

/**
 * Represents Level 2 of a Light-Bot-style game.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class Level2 extends Level {

  @Override
  protected void prepareWorld() {
    for (int i = 0; i < getWidth(); i++) {
      add(new Block(), i, 0);
      if (i < 2 || i > getWidth() - 3) {
        add(new Block(), i, 0);
        add(new Block(), i, getHeight() - 1);
        if (i < 1 || i > getWidth() - 2) {
          add(new Block(), i, 0);
          add(new Block(), i, getHeight() - 1);
        }
      }

      add(new Block(), i, getHeight() - 1);
    }

    add(new Block(), 4, 3);
    add(new Block(), 4, 3);

    add(new LightableTile(), 6, 3);
  }
}
