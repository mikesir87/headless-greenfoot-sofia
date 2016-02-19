package io.mikesir87.sofia.micro.lightbot;

import io.mikesir87.sofia.micro.Actor;
import io.mikesir87.sofia.micro.World;

/**
 * Represents a Light-Bot.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class LightBot extends Actor {

  private static enum CompassDirection {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

    private final int deltaX;
    private final int deltaY;
    private CompassDirection(int deltaX, int deltaY) {
      this.deltaX = deltaX;
      this.deltaY = deltaY;
    }

    public int getDeltaX() { return deltaX; }
    public int getDeltaY() { return deltaY; }
  }

  private CompassDirection direction = CompassDirection.EAST;

  /**
   * Create a new Light-Bot at the origin (0, 0), facing east.
   */
  public LightBot() {
    super();
  }

  /**
   * Create a new Light-Bot at the specified location, facing east.
   * @param x  The x-coordinate of the Light-Bot's location.
   * @param y  The y-coordinate of the Light-Bot's location.
   */
  public LightBot(int x, int y) {
    super();
    setGridLocation(x, y);
  }

  /**
   * Move forward one space.
   */
  public void move() {
    if (!isInsideGrid(direction))
      return;
    if (getHeightHere() != getHeightAtOffset(direction))
      return;

    setGridLocation(getGridX() + direction.getDeltaX(), getGridY() + direction.getDeltaY());
  }

  /**
   * Turn the Light-bot to the right
   */
  public void turnRight() {
    switch (this.direction) {
      case NORTH:
        this.direction = CompassDirection.EAST;
        break;
      case EAST:
        this.direction = CompassDirection.SOUTH;
        break;
      case SOUTH:
        this.direction = CompassDirection.WEST;
        break;
      case WEST:
        this.direction = CompassDirection.NORTH;
        break;
    }
  }

  /**
   * Turn the Light-bot to the left
   */
  public void turnLeft() {
    switch (this.direction) {
      case NORTH:
        this.direction = CompassDirection.WEST;
        break;
      case EAST:
        this.direction = CompassDirection.NORTH;
        break;
      case SOUTH:
        this.direction = CompassDirection.EAST;
        break;
      case WEST:
        this.direction = CompassDirection.SOUTH;
        break;
    }
  }

  /**
   * Move forward one space while jumping up one block, or jumping
   * down one or more blocks.
   */
  public void jump() {
    if (!isInsideGrid(direction))
      return;
    int myHeight = getHeightHere();
    int destHeight = getHeightAtOffset(direction);
    if (destHeight == myHeight + 1 || destHeight < myHeight) {
      setGridLocation(getGridX() + direction.getDeltaX(), getGridY() + direction.getDeltaY());
    }
  }

  /**
   * Turn on the blue tile the robot where the robot is standing.
   * This operation does nothing if the robot is not standing on a
   * blue tile.
   */
  public void turnLightOn() {
    LightableTile tile = getOneObjectAtOffset(0, 0, LightableTile.class);
    if (tile != null) {
      tile.turnLightOn();
    }
  }

  private boolean isInsideGrid(CompassDirection direction) {
    World world = getWorld();
    int x = getGridX() + direction.getDeltaX();
    int y = getGridY() + direction.getDeltaY();
    return x >= 0 && x < world.getWidth()
        && y >= 0 && y < world.getHeight();
  }

  private int getHeightHere() {
    return getObjectsAtOffset(0, 0, Block.class).size();
  }

  private int getHeightAtOffset(CompassDirection direction) {
    return getObjectsAtOffset(direction.getDeltaX(), direction.getDeltaY(),
        Block.class).size();
  }
}
