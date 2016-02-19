package io.mikesir87.sofia.micro.lightbot;

/**
 * Represents Level 6 of a Light-Bot-style game.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class Level6 extends Level {

  @Override
  protected void prepareWorld() {
    for (int i = 0; i < getHeight(); i++) {
      if (i < getHeight() - 1) {
        add(new Block(), 5, i);
        add(new Block(), 5, i);
      }
    }
    add(new LightableTile(), 5, 0);
    add(new LightableTile(), 5, getHeight() - 2);

    add(new Block(), 4, 3);
    add(new Block(), 4, 3);
    add(new Block(), 4, 3);
    add(new Block(), 4, 3);
    add(new LightableTile(), 4, 3);

    add(new Block(), 3, 3);
    add(new Block(), 3, 3);
    add(new Block(), 3, 3);

    add(new Block(), 2, 3);
    add(new Block(), 2, 3);

    add(new Block(), 2, 4);
  }
}
