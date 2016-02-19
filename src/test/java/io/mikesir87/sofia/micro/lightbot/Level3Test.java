package io.mikesir87.sofia.micro.lightbot;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Test case for the {@link Level3} world.
 */
public class Level3Test {

  private Level3 level3 = new Level3();

  @Test
  public void testExecution() {
    assertThat(level3.getObjects(LightableTile.class), hasSize(3));
    assertThat(level3.getObjects(LightableTile.class).get(0).isOn(), is(false));
    assertThat(level3.getObjects(LightableTile.class).get(1).isOn(), is(false));
    assertThat(level3.getObjects(LightableTile.class).get(2).isOn(), is(false));

    LightBot andy = new LightBot();
    level3.add(andy, 0, 2);
    andy.move();
    andy.move();

    // Validate he was blocked by the block
    assertThat(andy.getGridX(), is(1));

    // Jump up on the block and validate he moved
    andy.jump();
    assertThat(andy.getGridX(), is(2));

    // Shouldn't move if on a block and trying to get down
    andy.move();
    assertThat(andy.getGridX(), is(2));

    // Jump off the block and validate he moved
    andy.jump();
    assertThat(andy.getGridX(), is(3));

    andy.move();
    andy.turnRight();
    andy.turnLightOn();
    andy.move();
    andy.turnLightOn();
    andy.move();
    andy.turnLightOn();

    assertThat(andy.getGridX(), is(4));
    assertThat(andy.getGridY(), is(4));
    assertThat(level3.getObjects(LightableTile.class).get(0).isOn(), is(true));
    assertThat(level3.getObjects(LightableTile.class).get(1).isOn(), is(true));
    assertThat(level3.getObjects(LightableTile.class).get(2).isOn(), is(true));
  }

}
