package io.mikesir87.sofia.micro.lightbot;

/**
 * Represents a tile (possibly on top of a block) that can be lighted
 * by a Light-Bot.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public class LightableTile extends Tile {

  private boolean lit = false;

  /**
   * Turn this lightable tile "on" (it starts unlit).
   */
  public void turnLightOn() {
    lit = true;
  }

  /**
   * Determine if this tile's light is on.
   * @return True if this tile is lit up.
   */
  public boolean isOn() {
    return lit;
  }

}
