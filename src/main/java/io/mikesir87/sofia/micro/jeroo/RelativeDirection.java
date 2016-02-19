package io.mikesir87.sofia.micro.jeroo;

/**
 * Represents the four relative directions for turning or looking.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public enum RelativeDirection {

  /**
   * To the left of a Jeroo's current location or direction.
   */
  LEFT,

  /**
   * To the right of a Jeroo's current location or direction.
   */
  RIGHT,

  /**
   * Straight in front of the Jeroo, in the direction it is currently
   * facing.
   */
  AHEAD,

  /**
   * The location where the Jeroo is currently standing.
   */
  HERE
}
