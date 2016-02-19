package io.mikesir87.sofia.micro.jeroo;

import io.mikesir87.sofia.micro.Actor;
import io.mikesir87.sofia.micro.World;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

/**
 * Unit test for the {@link Water} class
 *
 * @author Michael Irwin
 */
public class WaterTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() { {
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  private World world;

  @Mock
  private Flower flower;

  @Mock
  private Jeroo jeroo;

  private Water water = new Water();

  @Before
  public void setUp() {
    water.setGridLocation(2, 2);
  }

  @Test
  public void testAddedToWorldWithJeroo() {
    context.checking(objExpectations(2, 2, Actor.class, flower, jeroo));
    context.checking(new Expectations() { {
      oneOf(jeroo).incapacitate(with(any(String.class)));
      oneOf(flower).remove();
    } });
    water.addedToWorld(world);
  }

  private Expectations objExpectations(final int x, final int y,
      final Class<? extends Actor> cls, final Actor... actors) {
    return new Expectations() { {
      allowing(world).getObjectsAt(x, y, cls);
      will(returnValue(Arrays.asList(actors)));
    } };
  }
}