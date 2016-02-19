package io.mikesir87.sofia.micro.lightbot;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link Level2} world.
 */
public class Level1Test {

  private Level1 level1 = new Level1();

  @Test
  public void testExecution() {
    assertThat(level1.getObjects(LightableTile.class), hasSize(1));
    assertThat(level1.getObjects(LightableTile.class).get(0).isOn(), is(false));

    LightBot andy = new LightBot();
    level1.add(andy, 0, 2);
    andy.move();
    andy.move();
    andy.move();
    andy.move();
    andy.turnRight();
    andy.move();
    andy.turnLightOn();

    assertThat(andy.getGridX(), is(4));
    assertThat(andy.getGridY(), is(3));
    assertThat(level1.getObjects(LightableTile.class).get(0).isOn(), is(true));
  }

}
