package io.mikesir87.sofia.micro.jeroo;

import io.mikesir87.sofia.micro.Actor;
import io.mikesir87.sofia.micro.World;

/**
 * Represents a net on Santong Island.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class Net extends Actor {

  /**
   * This method is called when the flower is added to the world.  It
   * disables itself if placed on a flower, or captures a Jeroo if it
   * lands on one.
   */
  @Override
  public void addedToWorld(World world) {
    super.addedToWorld(world);
    Flower flower = getOneObjectAtOffset(0, 0, Flower.class);
    if (flower != null) {
      flower.remove();
      this.remove();
    }
    else {
      Jeroo jeroo = getOneObjectAtOffset(0, 0, Jeroo.class);
      if (jeroo != null) {
        jeroo.incapacitate("is now trapped in a net");
      }
    }
  }
}
