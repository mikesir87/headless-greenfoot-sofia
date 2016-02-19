package io.mikesir87.sofia.micro;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import io.mikesir87.sofia.micro.jeroo.Jeroo;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test validating many of the methods on the base {@link Actor}
 * class.
 *
 * @author Michael Irwin
 */
public class ActorTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() { {
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  private World world;

  private Actor actor = new Jeroo();

  @Test
  public void testAddingAndRemovingEventsFromWorld() {
    actor.addedToWorld(world);
    assertThat(actor.getWorld(), is(sameInstance(world)));

    actor.removedFromWorld(world);
    assertThat(actor.getWorld(), is(nullValue()));
  }

  @Test
  public void testLocationSetting() {
    actor.setGridLocation(2, 3);
    assertThat(actor.getGridX(), is(2));
    assertThat(actor.getGridY(), is(3));

    actor.setGridX(4);
    assertThat(actor.getGridX(), is(4));

    actor.setGridY(1);
    assertThat(actor.getGridY(), is(1));
  }

  @Test(expected = IllegalStateException.class)
  public void testRemoveFailsIfNotInWorld() {
    actor.remove();
  }

  @Test
  public void testToString() {
    assertThat(actor.toString(), containsString("Jeroo at (0, 0)"));
  }
}