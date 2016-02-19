package io.mikesir87.sofia.micro.lightbot;

import io.mikesir87.sofia.micro.World;

/**
 * Represents a level of a Light-Bot-style game.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public abstract class Level extends World {

  /**
   * Construct a new level of size 8 x 8.
   */
  public Level() {
    super(8, 8);
  }

}
