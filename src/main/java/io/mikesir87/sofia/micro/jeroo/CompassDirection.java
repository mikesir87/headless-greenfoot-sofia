package io.mikesir87.sofia.micro.jeroo;

/**
 * Represents the four cardinal directions of the compass.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public enum CompassDirection {

  NORTH(0, -1),
  SOUTH(0, 1),
  EAST(1, 0),
  WEST(-1, 0);

  private final int deltaX;
  private final int deltaY;

  private CompassDirection(int deltaX, int deltaY) {
    this.deltaX = deltaX;
    this.deltaY = deltaY;
  }

  public CompassDirection turn(RelativeDirection direction) {
    if (direction != RelativeDirection.RIGHT && direction != RelativeDirection.LEFT)
      return this;

    switch (this) {
      case NORTH:
        return (direction == RelativeDirection.RIGHT) ? EAST : WEST;
      case SOUTH:
        return (direction == RelativeDirection.RIGHT) ? WEST : EAST;
      case EAST:
        return (direction == RelativeDirection.RIGHT) ? SOUTH : NORTH;
      default:
        return (direction == RelativeDirection.RIGHT) ? NORTH: SOUTH;
    }
  }

  public int getDeltaX() {
    return deltaX;
  }

  public int getDeltaY() {
    return deltaY;
  }
}
