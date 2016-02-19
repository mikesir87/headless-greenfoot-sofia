package io.mikesir87.sofia.micro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a "microworld" containing Actors.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class World {

  private static final int DEFAULT_WIDTH  = 20;
  private static final int DEFAULT_HEIGHT = 12;

  private final int width;
  private final int height;
  private List<Actor> actors = new ArrayList<>();

  /**
   * Create a new world using default dimensions.
   */
  public World() {
    this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  /**
   * Create a new world using the dimensions specified.
   * @param width The width of the new world
   * @param height The height of the new world
   */
  public World(int width, int height) {
    this.width = width;
    this.height = height;
    prepareWorld();
  }

  /**
   * Perform whatever preparations are needed to create the world
   */
  protected void prepareWorld() {

  }

  /**
   * Get the width of the world
   * @return The width of the world
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the height of the world
   * @return The height of the world
   */
  public int getHeight() {
    return height;
  }

  /**
   * Add an Actor to the world.
   * @param actor The Actor to add.
   */
  public void add(Actor actor) {
    actor.addedToWorld(this);
    actors.add(actor);
  }

  /**
   * Add an Actor to the world at a specified location.  This is a
   * convenience method that is equivalent to calling {@code add()} on
   * an actor, and then calling {@code setGridLocation()} on the actor
   * to specify its position.
   *
   * @param actor The Actor to add.
   * @param x The x coordinate of the location where the actor is added.
   * @param y The y coordinate of the location where the actor is added.
   */
  public void add(Actor actor, int x, int y) {
    actor.setGridLocation(x, y);
    add(actor);
  }

  /**
   * Remove an Actor from the world.
   * @param actor The Actor to remove.
   */
  public void remove(Actor actor) {
    actors.remove(actor);
    actor.removedFromWorld(this);
  }

  /**
   * Get the number of actors currently in the world.
   * @return The number of actors in the world.
   */
  public int numberOfObjects() {
    return actors.size();
  }

  /**
   * Get all the actors in the world.
   * @return A collection of all the actors in this world.
   */
  public List<? extends Actor> getObjects() {
    return actors;
  }

  /**
   * Get all the actors of the specified type in this world.
   *
   * @param cls Class of actors to look for (passing 'null' will find all
   *            actors).
   * @param <MyActor> The type of actor to look for, as specified
   *                  in the cls parameter.
   * @return A collection of all the actors of the specified type (or any of
   *         its subtypes) in the world.
   */
  public <MyActor extends Actor> List<MyActor> getObjects(final Class<MyActor> cls) {
    return actors.stream()
        .filter(actor -> cls.isAssignableFrom(actor.getClass()))
        .map(cls::cast)
        .collect(Collectors.toList());
  }

  /**
   * Return all actors of the specified type at a given cell.
   * <p>
   * An object is defined to be at that cell if its graphical representation
   * overlaps the center of the cell.</p>
   *
   * @param x X-coordinate of the cell to be checked.
   * @param y Y-coordinate of the cell to be checked.
   * @param cls Class of actors to find ('null' will return all
   *            actors).
   * @param <MyActor> The type of actor to look for, as specified
   *                      in the cls parameter.
   * @return A collection of actors at the specified location.
   */
  public <MyActor extends Actor> List<MyActor> getObjectsAt(int x, int y,
      Class<MyActor> cls) {
    return getObjects(cls)
        .stream()
        .filter(actor -> actor.getGridX() == x && actor.getGridY() == y)
        .collect(Collectors.toList());
  }

  /**
   * Return one actor that is located at the specified cell.  If more than
   * one actor resides at that location, one of them will be chosen and
   * returned.
   *
   * @param x   X-coordinate.
   * @param y   Y-coordinate.
   * @return An actor at the given location, or null if none found.
   */
  public Actor getOneObjectAt(int x, int y) {
    return getOneObjectAt(x, y, Actor.class);
  }

  /**
   * Return one actor that is located at the specified cell. Objects found
   * can be restricted to a specific class (and its subclasses) by supplying
   * the 'cls' parameter.  If more than one actor of the specified class
   * resides at that location, one of them will be chosen and returned.
   *
   * @param x   X-coordinate.
   * @param y   Y-coordinate.
   * @param cls Class of actors to look for (passing 'null' will find all
   *            actors).
   * @param <MyActor> The type of actor to look for, as specified
   *                  in the cls parameter.
   * @return An actor at the given location, or null if none found.
   */
  public <MyActor extends Actor> MyActor getOneObjectAt(int x, int y,
      Class<MyActor> cls) {
    List<MyActor> actors = getObjectsAt(x, y, cls);
    return (actors != null && actors.size() > 0) ? actors.get(0) : null;
  }
}
