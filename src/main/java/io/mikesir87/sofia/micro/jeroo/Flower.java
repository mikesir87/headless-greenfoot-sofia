package io.mikesir87.sofia.micro.jeroo;

import io.mikesir87.sofia.micro.Actor;
import io.mikesir87.sofia.micro.World;

/**
 * Represents a winsum flower, the Jeroo's primary food source.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class Flower extends Actor {

  /**
   * This method is called when the flower is added to the world.  It
   * disables any net it lands on.
   */
  @Override
  public void addedToWorld(World world) {
    super.addedToWorld(world);
    Net net = getOneObjectAtOffset(0, 0, Net.class);
    if (net != null) {
      net.remove();
      this.remove();
    }
  }

}
