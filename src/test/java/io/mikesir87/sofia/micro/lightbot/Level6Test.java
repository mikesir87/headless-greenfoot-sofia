package io.mikesir87.sofia.micro.lightbot;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link Level6} world.
 */
public class Level6Test {

  private Level6 level6 = new Level6();

  @Test
  public void testExecution() {
    assertThat(level6.getObjects(LightableTile.class), hasSize(3));
    assertThat(level6.getObjects(LightableTile.class).get(0).isOn(), is(false));
    assertThat(level6.getObjects(LightableTile.class).get(1).isOn(), is(false));
    assertThat(level6.getObjects(LightableTile.class).get(2).isOn(), is(false));

    LightBot andy = new LightBot();
    level6.add(andy, 0, 2);

    andy.turnRight();
    andy.move();
    andy.move();
    andy.turnLeft();
    andy.move();
    andy.jump();
    andy.turnLeft();
    andy.jump();
    andy.turnRight();
    andy.jump();
    andy.jump();
    andy.turnLightOn();

    andy.jump();
    andy.turnLeft();
    andy.move();
    andy.move();
    andy.move();
    andy.turnLightOn();
    andy.turnRight();
    andy.turnRight();
    for (int i = 0; i < 6; i++)
      andy.move();
    andy.turnLightOn();
    andy.move();

    assertThat(andy.getGridX(), is(5));
    assertThat(andy.getGridY(), is(6));
    assertThat(level6.getObjects(LightableTile.class).get(0).isOn(), is(true));
    assertThat(level6.getObjects(LightableTile.class).get(1).isOn(), is(true));
    assertThat(level6.getObjects(LightableTile.class).get(2).isOn(), is(true));
  }

}
