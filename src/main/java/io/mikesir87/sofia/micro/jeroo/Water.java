package io.mikesir87.sofia.micro.jeroo;

import io.mikesir87.sofia.micro.Actor;
import io.mikesir87.sofia.micro.World;

/**
 * Represents a patch of water on/around Santong Island.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class Water extends Actor {

  /**
   * This method is called when the water is added to the world.  It
   * removes any objects it is placed on other than Jeroos, and disables
   * any Jeroos it is placed on.
   */
  @Override
  public void addedToWorld(World world) {
    super.addedToWorld(world);
    for (Actor actor : getObjectsAtOffset(0, 0, Actor.class)) {
      if (actor instanceof Jeroo) {
        ((Jeroo)actor).incapacitate("is now stuck in the water.");
      }
      else if (actor != this) {
        actor.remove();
      }
    }
  }

}
