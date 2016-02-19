package io.mikesir87.sofia.micro.jeroo;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import io.mikesir87.sofia.micro.World;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test case for the {@link Flower} class.
 *
 * @author Michael Irwin
 */
public class FlowerTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() { {
    setImposteriser(ClassImposteriser.INSTANCE);
  } };

  @Mock
  private World world;

  @Mock
  private Net net;

  private Flower flower = new Flower();

  @Before
  public void setUp() {
    flower.setGridLocation(2, 2);
  }

  @Test
  public void testAddedToWorldWithNet() {
    context.checking(worldObjectExpectations(2, 2, net));
    context.checking(new Expectations() { {
      oneOf(net).remove();
      oneOf(world).remove(flower);
    } });

    flower.addedToWorld(world);
  }

  @Test
  public void testAddedToWorldWithoutNet() {
    context.checking(worldObjectExpectations(2, 2, null));
    flower.addedToWorld(world);
  }

  private Expectations worldObjectExpectations(final int x, final int y,
      final Net net) {
    return new Expectations() { {
      allowing(world).getOneObjectAt(x, y, Net.class);
      will(returnValue(net));
    } };
  }
}