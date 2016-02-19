package io.mikesir87.sofia.micro.jeroo;

import io.mikesir87.sofia.micro.World;

/**
 * Represents an island where Jeroos live (e.g., Santong Island).
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class Island extends World {

  // While static imports would be better, here we do this for simpler
  // beginner programs
  /** A constant that means facing east, or to the right. */
  public static final CompassDirection EAST  = CompassDirection.EAST;
  /** A constant that means facing south, or down. */
  public static final CompassDirection SOUTH = CompassDirection.SOUTH;
  /** A constant that means facing west, or to the left. */
  public static final CompassDirection WEST  = CompassDirection.WEST;
  /** A constant that means facing north, or up. */
  public static final CompassDirection NORTH = CompassDirection.NORTH;

  /** A constant that represents the direction to the left of a Jeroo's
   * current location or direction.
   */
  public static final RelativeDirection LEFT = RelativeDirection.LEFT;

  /** A constant that represents the direction to the right of a Jeroo's
   * current location or direction.
   */
  public static final RelativeDirection RIGHT = RelativeDirection.RIGHT;

  /** A constant that represents the direction straight in front of the
   * Jeroo, in the direction it is currently facing.
   */
  public static final RelativeDirection AHEAD = RelativeDirection.AHEAD;

  /** A constant that represents the location where the Jeroo is currently
   * standing.
   */
  public static final RelativeDirection HERE = RelativeDirection.HERE;

  /**
   * Construct a new island with a default size of 12 x 12 cells.
   */
  public Island() {
    this(12, 12);
  }

  /**
   * Construct a new world. The size of the world (in number of cells)
   * must be specified.
   *
   * @param width  The width of the world (in cells).
   * @param height The height of the world (in cells).
   */
  public Island(int width, int height) {
    super(width, height);
  }

  /**
   * This method lays out the geography of the island.  The default
   * implementation draws a square island with a 1-cell wide border of
   * water.  Subclasses should define this appropriately.  Be sure to
   * call {@code super.prepare()} if you want to build on the default
   * layout.
   */
  @Override
  protected void prepareWorld() {
    for (int x = 0; x < this.getWidth(); x++) {
      this.add(new Water(), x, 0);
      this.add(new Water(), x, this.getHeight() - 1);
    }
    for (int y = 1; y < getHeight() - 1; y++) {
      this.add(new Water(), 0, y);
      this.add(new Water(), this.getWidth() - 1, y);
    }
  }

}
