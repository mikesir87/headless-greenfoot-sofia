package io.mikesir87.sofia.micro;

import java.util.List;

/**
 * An Actor is an object (or character) that exists in a "microworld".
 * Every Actor has a location in the world.
 *
 * <p>An Actor is not normally instantiated directly, but instead used as a
 * superclass.  Create a subclass to represent more specific objects in the
 * world. Every object that is intended to appear in the world must extend
 * Actor. Subclasses can then define their own appearance and behavior.</p>
 *
 * <p>One of the most important aspects of this class is the 'act' method.
 * The method here is empty, and subclasses normally provide
 * their own implementations.</p>
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: edwards $
 * @version $Date: 2012/08/06 11:13 $
 */
public abstract class Actor {

  /**
   * Error message to display when trying to use methods that requires the
   * actor be in a world.
   */
  private static final String ACTOR_NOT_IN_WORLD = "Actor not in world. "
      + "An attempt was made to use the actor's location while it is not "
      + "in the world. Either it has not yet been inserted, or it has been "
      + "removed.";

  private World world;
  private int gridX;
  private int gridY;

  /**
   * Create a new Actor
   */
  public Actor() {
    // Nothing to do here
  }

  /**
   * Return the world that this actor lives in.
   * @return The world.
   */
  public World getWorld() {
    return world;
  }

  /**
   * Method called when the actor is added to the world
   * @param world The world the actor has been added to
   */
  public void addedToWorld(World world) {
    this.world = world;
  }

  /**
   * Method called when the actor is removed from the world
   * @param world The world the actor has been removed from
   */
  public void removedFromWorld(World world) {
    this.world = null;
  }

  /**
   * Get the x-coordinate for the actor
   * @return The x-coordinate for the actor
   */
  public int getGridX() {
    return gridX;
  }

  /**
   * Set the x-coordinate for the actor
   * @param x The x-coordinate for the actor
   */
  public void setGridX(int x) {
    this.gridX = x;
  }

  /**
   * Get the y-coordinate for the actor
   * @return The y-coordinate for the actor
   */
  public int getGridY() {
    return gridY;
  }

  /**
   * Set the y-coordinate for the actor
   * @param gridY The y-coordinate for the actor
   */
  public void setGridY(int gridY) {
    this.gridY = gridY;
  }

  /**
   * Set the new coordinates for the actor
   * @param x The new x-coordinate
   * @param y The new y-coordinate
   */
  public void setGridLocation(int x, int y) {
    setGridX(x);
    setGridY(y);
  }

  /**
   * Return all objects that intersect the center of the given location
   * (relative to this object's location).
   *
   * @param dx X-coordinate relative to this object's location.
   * @param dy Y-coordinate relative to this object's location.
   * @param cls Class of objects to look for (passing 'null' will find all
   *            objects).
   * @param <MyActor> The type of actor to look for, as specified
   *                  in the cls parameter.
   * @return A collection of objects at the given offset. The set will
   *         include this object, if the offset is zero.
   */
  public <MyActor extends Actor> List<MyActor> getObjectsAtOffset(
      int dx, int dy, Class<MyActor> cls)
  {
    failIfNotInWorld();
    return getWorld().getObjectsAt(getGridX() + dx, getGridY() + dy, cls);
  }

  /**
   * Return one object that is located at the specified cell (relative to
   * this object's location). Objects found can be restricted to a specific
   * class (and its subclasses) by supplying the 'cls' parameter. If more
   * than one object of the specified class resides at that location, one
   * of them will be chosen and returned.
   *
   * @param dx X-coordinate relative to this object's location.
   * @param dy Y-coordinate relative to this object's location.
   * @param cls Class of objects to look for (passing 'null' will find all
   *            objects).
   * @param <MyActor> The type of actor to look for, as specified
   *                  in the cls parameter.
   * @return An object at the given location, or null if none found.
   */
  public <MyActor extends Actor> MyActor getOneObjectAtOffset(
      int dx, int dy, Class<MyActor> cls) {
    failIfNotInWorld();
    return getWorld().getOneObjectAt(getGridX() + dx, getGridY() + dy, cls);
  }

  /**
   * Remove this actor from the world
   */
  public void remove() {
    failIfNotInWorld();
    getWorld().remove(this);
  }

  /**
   * Returns a human-readable string representation of the actor.
   * @return A human-readable string representation of the actor.
   */
  @Override
  public String toString() {
    return getClass().getSimpleName() + " at (" + getGridX() + ", " + getGridY() + ")";
  }


  /**
   * Throws an exception if the actor is not in a world.
   * @throws IllegalStateException If not in world.
   */
  private void failIfNotInWorld() {
    World world = getWorld();
    if (world == null) {
      throw new IllegalStateException(ACTOR_NOT_IN_WORLD);
    }
  }

}
