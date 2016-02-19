package io.mikesir87.sofia.micro.jeroo;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for the {@link Island} class
 *
 * @author Michael Irwin
 */
public class IslandTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Test
  public void testDefaultIslandCreation() {
    Island island = new Island();
    assertThat(island.getWidth(), is(12));
    assertThat(island.getHeight(), is(12));

    assertThat(island.getObjects(Water.class).size(), is(44));
  }

  @Test
  public void testCustomIslandSizeConstructor() {
    Island island = new Island(10, 15);
    assertThat(island.getWidth(), is(10));
    assertThat(island.getHeight(), is(15));
    assertThat(island.getObjects(Water.class).size(), is(46));
  }
}