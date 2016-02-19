package io.mikesir87.sofia.micro.jeroo;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import io.mikesir87.sofia.micro.Actor;
import io.mikesir87.sofia.micro.World;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for the {@link Net} class
 *
 * @author Michael Irwin
 */
public class NetTest {

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

  private Net net = new Net();

  @Before
  public void setUp() {
    net.setGridLocation(2, 2);
  }

  @Test
  public void testAddedToWorldWithFlower() {
    context.checking(objExpectations(2, 2, Flower.class, flower));
    context.checking(new Expectations() { {
      oneOf(flower).remove();
      oneOf(world).remove(net);
    } });
    net.addedToWorld(world);
  }

  @Test
  public void testAddedToWorldWithJeroo() {
    context.checking(objExpectations(2, 2, Flower.class, null));
    context.checking(objExpectations(2, 2, Jeroo.class, jeroo));
    context.checking(new Expectations() { {
      oneOf(jeroo).incapacitate(with(any(String.class)));
    } });

    net.addedToWorld(world);
  }

  @Test
  public void testAddedToWorldWithNothing() {
    context.checking(objExpectations(2, 2, Flower.class, null));
    context.checking(objExpectations(2, 2, Jeroo.class, null));

    net.addedToWorld(world);
  }

  private Expectations objExpectations(final int x, final int y,
      final Class<? extends Actor> cls, final Actor actor) {
    return new Expectations() { {
      allowing(world).getOneObjectAt(x, y, cls);
      will(returnValue(actor));
    } };
  }
}