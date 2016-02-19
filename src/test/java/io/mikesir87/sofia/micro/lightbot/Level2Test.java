package io.mikesir87.sofia.micro.lightbot;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link Level2} world.
 */
public class Level2Test {

  private Level2 level2 = new Level2();

  @Test
  public void testExecution() {
    assertThat(level2.getObjects(LightableTile.class), hasSize(1));
    assertThat(level2.getObjects(LightableTile.class).get(0).isOn(), is(false));

    LightBot andy = new LightBot();
    level2.add(andy, 0, 2);
    andy.move();
    andy.move();
    andy.move();
    andy.move();
    andy.move();
    andy.move();
    andy.turnRight();
    andy.move();
    andy.turnLightOn();

    assertThat(andy.getGridX(), is(6));
    assertThat(andy.getGridY(), is(3));
    assertThat(level2.getObjects(LightableTile.class).get(0).isOn(), is(true));
  }

}
