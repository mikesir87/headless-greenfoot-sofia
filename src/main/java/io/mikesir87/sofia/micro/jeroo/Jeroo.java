package io.mikesir87.sofia.micro.jeroo;

import io.mikesir87.sofia.micro.Actor;
import io.mikesir87.sofia.micro.World;

import java.util.List;

/**
 * Represents a Jeroo on Santong Island.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class Jeroo extends Actor {

  private int flowers = 0;
  private CompassDirection direction = EAST;

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


  // Internal state to track intial conditions and cumulative moves
  private int initX;
  private int initY;
  private CompassDirection initDirection;
  private int initFlowers;
  private int totalHops   = 0;
  private int totalTurns  = 0;
  private int totalPicks  = 0;
  private int totalPlants = 0;
  private int totalTosses = 0;
  private int totalGives  = 0;

  /**
   * Create a new Jeroo at the origin (0, 0), facing east, with no flowers.
   */
  public Jeroo() {
    this(0, 0);
  }

  /**
   * Create a new Jeroo at the origin (0, 0), facing east.
   * @param flowers The number of flowers the Jeroo is holding.
   */
  public Jeroo(int flowers) {
    this(0, 0, flowers);
  }

  /**
   * Create a new Jeroo, facing east, with no flowers.
   * @param x  The x-coordinate of the Jeroo's location.
   * @param y  The y-coordinate of the Jeroo's location.
   */
  public Jeroo(int x, int y) {
    this(x, y, 0);
  }

  /**
   * Create a new Jeroo with no flowers.
   * @param x         The x-coordinate of the Jeroo's location.
   * @param y         The y-coordinate of the Jeroo's location.
   * @param direction The direction the Jeroo is facing.
   */
  public Jeroo(int x, int y, CompassDirection direction) {
    this(x, y, direction, 0);
  }

  /**
   * Create a new Jeroo facing east.
   * @param x         The x-coordinate of the Jeroo's location.
   * @param y         The y-coordinate of the Jeroo's location.
   * @param flowers   The number of flowers the Jeroo is holding.
   */
  public Jeroo(int x, int y, int flowers) {
    this(x, y, EAST, flowers);
  }

  /**
   * Create a new Jeroo.
   * @param x         The x-coordinate of the Jeroo's location.
   * @param y         The y-coordinate of the Jeroo's location.
   * @param direction The direction the Jeroo is facing.
   * @param flowers   The number of flowers the Jeroo is holding.
   */
  public Jeroo(int x, int y, CompassDirection direction, int flowers) {
    super();

    // save starting state for later internal testing
    initX = x;
    initY = y;
    initDirection = direction;
    initFlowers = flowers;

    setGridLocation(x, y);
    this.direction = direction;
    this.flowers = flowers;
  }

  public void hop() {
    if (!isInsideGrid(direction)) {
      incapacitate("attempted to move out of bounds and failed.");
    }

    setGridLocation(getGridX() + direction.getDeltaX(), getGridY() + direction.getDeltaY());
    totalHops++;

    if (getOneObjectAtOffset(0, 0, Net.class) != null) {
      incapacitate("is now trapped in a net.");
    }
    if (getOneObjectAtOffset(0, 0, Water.class) != null) {
      incapacitate("is now stuck in the water.");
    }

    List<Jeroo> others = getObjectsAtOffset(0, 0, Jeroo.class);
    if (others.size() > 1) {
      incapacitate("bumped into " + (others.size() - 1) + " other Jeroo"
          + (others.size() - 1 == 1 ? "" : "s") + ".");
    }
  }

  /**
   * Hop <i>number</i> times in a row, where <i>number</i> is a positive
   * integer.
   * @param number The number of spaces to move (greater than zero).
   */
  public void hop(int number) {
    for (int i = 0; i < number; i++) {
      hop();
    }
  }

  /**
   * Pick a flower from the current location.  Nothing happens if there is
   * no flower at the current location.
   */
  public void pick() {
    Flower flower = getOneObjectAtOffset(0, 0, Flower.class);
    if (flower != null) {
      flowers++;
      totalPicks++;
      flower.remove();
    }
  }

  /**
   * Plant a flower at the current location. Nothing happens if the Jeroo
   * does not have a flower to plant.
   */
  public void plant() {
    if (flowers > 0) {
      flowers--;
      getWorld().add(new Flower(), getGridX(), getGridY());
      totalPlants++;
    }
  }

  /**
   * Toss a flower one space ahead.  The tossed flower is lost forever.
   * If the flower lands on a net, the net is disabled.
   */
  public void toss() {
    if (flowers == 0) {
      return;
    }

    flowers--;
    totalTosses++;

    if (!isInsideGrid(direction)) {
      return;
    }

    Net net = getOneObjectAtOffset(direction.getDeltaX(), direction.getDeltaY(),
        Net.class);
    if (net != null) {
      net.remove();
    }
  }

  /**
   * Give a flower to a Jeroo in a neighboring cell in the indicated
   * direction. Nothing happens if the giving Jeroo has no flowers or if
   * there is no Jeroo in the indicated direction.
   * ({@code give(HERE);} is meaningless.)
   * @param direction The direction to give (LEFT, RIGHT, or AHEAD).
   */
  public void give(RelativeDirection direction) {
    if (flowers == 0 || direction == HERE || !isInsideGrid(this.direction))
      return;

    Jeroo buddy = getOneObjectAtOffset(this.direction.getDeltaX(),
        this.direction.getDeltaY(), Jeroo.class);
    if (buddy != null) {
      flowers--;
      buddy.flowers++;
      totalGives++;
    }
  }

  /**
   * Turn in the indicated direction, but stay in the same location.
   * ({@code turn(AHEAD);} and {@code turn(HERE);} are meaningless.)
   * @param direction The direction to turn (LEFT or RIGHT).
   */
  public void turn(RelativeDirection direction) {
    if (direction == AHEAD || direction == HERE) {
      return;
    }

    this.direction = this.direction.turn(direction);
    totalTurns++;
  }

  /**
   * Does this Jeroo have any flowers in its pouch?
   * @return True if this Jeroo has at least one flower.
   */
  public boolean hasFlower() {
    return flowers > 0;
  }

  /**
   * Is the Jeroo facing the indicated direction?
   * @param direction The direction to check (NORTH, SOUTH, EAST, or WEST).
   * @return True if this Jeroo is facing the specified direction.
   */
  public boolean isFacing(CompassDirection direction) {
    return this.direction == direction;
  }

  /**
   * Is there a flower in the indicated direction?
   * @param direction The direction to check.
   * @return True if there is a flower in the specified direction.
   */
  public boolean seesFlower(RelativeDirection direction) {
    return isInsideGrid(this.direction.turn(direction)) &&
        getObjectAtDirection(direction, Flower.class) != null;
  }

  /**
   * Is there a Jeroo in the indicated direction?
   * @param direction The direction to check.
   * @return True if there is a flower in the specified direction.
   */
  public boolean seesJeroo(RelativeDirection direction) {
    return isInsideGrid(this.direction.turn(direction)) &&
        getObjectAtDirection(direction, Jeroo.class) != null;
  }

  /**
   * Is there a net in the indicated direction?
   * @param direction The direction to check.
   * @return True if there is a net in the specified direction.
   */
  public boolean seesNet(RelativeDirection direction) {
    return isInsideGrid(this.direction.turn(direction)) &&
        getObjectAtDirection(direction, Net.class) != null;
  }

  /**
   * Is there water in the indicated direction?
   * @param direction The direction to check.
   * @return True if there is water in the specified direction.
   */
  public boolean seesWater(RelativeDirection direction) {
    return isInsideGrid(this.direction.turn(direction)) &&
        getObjectAtDirection(direction, Water.class) != null;
  }

  /**
   * Are there no obstacles (no net, no flower, no Jeroo, and no water) in
   * the indicated direction?
   * @param direction The direction to check.
   * @return True if there are no obstacles in the specified direction.
   */
  public boolean isClear(RelativeDirection direction) {
    return isInsideGrid(this.direction.turn(direction)) &&
        getObjectAtDirection(direction, Actor.class) == null;
  }

  private <T extends Actor> T getObjectAtDirection(RelativeDirection direction,
      Class<T> cls) {
    if (direction == HERE)
      return getOneObjectAtOffset(0, 0, cls);
    CompassDirection dir = this.direction.turn(direction);
    return getOneObjectAtOffset(dir.getDeltaX(), dir.getDeltaY(), cls);
  }

  public void incapacitate(String message) {
    throw new RuntimeException(message);
  }

  private boolean isInsideGrid(CompassDirection direction) {
    World world = getWorld();
    int x = getGridX() + direction.getDeltaX();
    int y = getGridY() + direction.getDeltaY();
    return x >= 0 && x < world.getWidth()
        && y >= 0 && y < world.getHeight();
  }
}
