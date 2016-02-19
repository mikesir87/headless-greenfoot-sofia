package io.mikesir87.sofia.micro;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import io.mikesir87.sofia.micro.jeroo.Flower;
import io.mikesir87.sofia.micro.jeroo.Net;
import io.mikesir87.sofia.micro.jeroo.Water;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.Test;

/**
 * Basic testing for the base {@link World} class.  More extensive testing is
 * done by testing actual levels.
 *
 * @author Michael Irwin
 */
public class WorldTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() { {
    setImposteriser(ClassImposteriser.INSTANCE);
  } };

  @Mock
  private Net net;

  @Mock
  private Flower flower;

  @Mock
  private Water water;

  private World world = new World();

  @Test
  public void testConstructor() {
    assertThat(world.getHeight(), is(equalTo(12)));
    assertThat(world.getWidth(), is(equalTo(20)));
  }

  @Test
  public void testCustomSizedConstructor() {
    World customWorld = new World(5, 7);
    assertThat(customWorld.getHeight(), is(equalTo(7)));
    assertThat(customWorld.getWidth(), is(equalTo(5)));
  }

  @Test
  public void testActorAddingAndRemoving() {
    context.checking(addingActorExpectations(net, flower, water));
    context.checking(removingActorExpectations(water));

    world.add(net);
    world.add(flower);
    world.add(water);

    assertThat(world.numberOfObjects(), is(3));
    assertThat(world.getObjects(), containsInAnyOrder(net, flower, water));
    assertThat(world.getObjects(Net.class), contains(net));
    assertThat(world.getObjects(Flower.class), contains(flower));
    assertThat(world.getObjects(Water.class), contains(water));

    world.remove(water);
    assertThat(world.numberOfObjects(), is(2));
    assertThat(world.getObjects(Water.class), is(empty()));
  }

  @Test
  public void testGetObjectsAtLocation() {
    context.checking(addingActorExpectations(net, flower, water));
    context.checking(actorLocationExpectations(net, 0, 0));
    context.checking(actorLocationExpectations(flower, 0, 0));
    context.checking(actorLocationExpectations(water, 0, 0));

    world.add(net);
    world.add(flower);
    world.add(water);

    assertThat(world.getObjectsAt(0, 0, Actor.class), containsInAnyOrder(net, flower, water));
    assertThat(world.getObjectsAt(0, 0, Net.class), contains(net));
    assertThat(world.getObjectsAt(1, 0, Net.class), is(empty()));
    assertThat(world.getOneObjectAt(0, 0), is(isOneOf(net, flower, water)));
    assertThat(world.getOneObjectAt(0, 0, Net.class), is(isOneOf(net)));
    assertThat(world.getOneObjectAt(1, 0), is(nullValue()));
  }

  private Expectations addingActorExpectations(final Actor... actors) {
    return new Expectations() { {
      for (Actor actor : actors) {
        oneOf(actor).addedToWorld(world);
      }
    } };
  }

  private Expectations removingActorExpectations(final Actor... actors) {
    return new Expectations() { {
      for (Actor actor : actors) {
        oneOf(actor).removedFromWorld(world);
      }
    } };
  }

  private Expectations actorLocationExpectations(final Actor actor, final int x,
      final int y) {
    return new Expectations() { {
      allowing(actor).getGridX();
      will(returnValue(x));
      allowing(actor).getGridY();
      will(returnValue(y));
    } };
  }
}