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
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit tests for the {@link Jeroo} class.
 *
 * @author Michael Irwin
 */
public class JerooTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() { {
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private World world;

  @Mock
  private Net net;

  @Mock
  private Flower flower;

  @Mock
  private Water water;

  private Jeroo jeroo = new Jeroo();

  @Before
  public void setUp() {
    jeroo.addedToWorld(world);
  }

  @Test
  public void testDefaultConstructor() {
    Jeroo jeroo = new Jeroo();
    assertThat(jeroo.getGridX(), is(0));
    assertThat(jeroo.getGridY(), is(0));
    assertThat(jeroo.hasFlower(), is(false));
  }

  @Test
  public void testFlowerConstructor() {
    Jeroo jeroo = new Jeroo(1);
    assertThat(jeroo.getGridX(), is(0));
    assertThat(jeroo.getGridY(), is(0));
    assertThat(jeroo.hasFlower(), is(true));
  }

  @Test
  public void testLocationAndFlowerConstructor() {
    Jeroo jeroo = new Jeroo(2, 2, 1);
    assertThat(jeroo.getGridX(), is(2));
    assertThat(jeroo.getGridY(), is(2));
    assertThat(jeroo.hasFlower(), is(true));
  }

  @Test
  public void testLocationAndDirectionConstructor() {
    Jeroo jeroo = new Jeroo(2, 2, CompassDirection.NORTH);
    assertThat(jeroo.getGridX(), is(2));
    assertThat(jeroo.getGridY(), is(2));
    assertThat(jeroo.hasFlower(), is(false));
    assertThat(jeroo.isFacing(CompassDirection.NORTH), is(true));
  }

  @Test
  public void testFullConstructor() {
    Jeroo jeroo = new Jeroo(2, 2, CompassDirection.NORTH, 1);
    assertThat(jeroo.getGridX(), is(2));
    assertThat(jeroo.getGridY(), is(2));
    assertThat(jeroo.hasFlower(), is(true));
    assertThat(jeroo.isFacing(CompassDirection.NORTH), is(true));
  }

  @Test
  public void testHoppingInsideOfTheWorld() {
    context.checking(worldSizeExpectations(4, 4));
    context.checking(hereExpectationsForHopping(3, 2, null, null, Collections.emptyList()));
    jeroo.setGridLocation(2, 2);

    jeroo.hop();

    assertThat(jeroo.getGridX(), is(3));
    assertThat(jeroo.getGridY(), is(2));
  }

  @Test
  public void testHoppingOutsideOfTheWorldCausesIncapcitation() {
    context.checking(worldSizeExpectations(4, 4));
    jeroo.setGridLocation(3, 3);
    expectingIncapcitation("move out of bounds");

    jeroo.hop();
  }

  @Test
  public void testHoppingIntoANetCausesIncapcitation() {
    context.checking(worldSizeExpectations(4, 4));
    context.checking(hereExpectationsForHopping(3, 2, net, null, Collections.emptyList()));
    jeroo.setGridLocation(2, 2);
    expectingIncapcitation("trapped in a net");

    jeroo.hop();
  }

  @Test
  public void testHoppingIntoWaterCausesIncapcitation() {
    context.checking(worldSizeExpectations(4, 4));
    context.checking(hereExpectationsForHopping(3, 2, null, water, Collections.emptyList()));
    jeroo.setGridLocation(2, 2);
    expectingIncapcitation("stuck in the water");

    jeroo.hop();
  }

  @Test
  public void testHoppingIntoAnotherJerooCausesIncapcitation() {
    Jeroo anotherJeroo = context.mock(Jeroo.class, "anotherJeroo");
    context.checking(worldSizeExpectations(4, 4));
    context.checking(hereExpectationsForHopping(3, 2, null, null, Arrays.asList(jeroo, anotherJeroo)));
    jeroo.setGridLocation(2, 2);
    expectingIncapcitation("bumped into 1 other Jeroo");

    jeroo.hop();
  }

  @Test
  public void testMultipleHops() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectationsForHopping(3, 2, null, null, Collections.emptyList()));
    context.checking(hereExpectationsForHopping(4, 2, null, null, Collections.emptyList()));

    jeroo.setGridLocation(2, 2);
    jeroo.hop(2);
    assertThat(jeroo.getGridX(), is(4));
    assertThat(jeroo.getGridY(), is(2));
  }

  @Test
  public void testPickWhenFlowerIsPresent() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(2, 2, Flower.class, flower));
    context.checking(new Expectations() { {
      oneOf(flower).remove();
    } });

    jeroo.setGridLocation(2, 2);
    jeroo.pick();
    assertThat(jeroo.hasFlower(), is(true));
  }

  @Test
  public void testPickWhenFlowerNotPresent() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(2, 2, Flower.class, null));

    jeroo.setGridLocation(2, 2);
    jeroo.pick();
  }

  @Test
  public void testPlantWhenHasFlowers() throws Exception {
    Field flowersField = setJerooFlowersField(1);

    context.checking(worldSizeExpectations(5, 5));
    context.checking(new Expectations() { {
      oneOf(world).add(with(any(Flower.class)), with(2), with(2));
    } });

    jeroo.setGridLocation(2, 2);
    jeroo.plant();
  }

  @Test
  public void testPlantWhenHasNoFlowers() {
    context.checking(worldSizeExpectations(5, 5));
    jeroo.setGridLocation(2, 2);
    jeroo.plant();
  }

  @Test
  public void testTossWhenNoFlowers() {
    context.checking(worldSizeExpectations(5, 5));
    jeroo.setGridLocation(2, 2);
    jeroo.toss();
  }

  @Test
  public void testTossWhenNextToBoundary() throws Exception {
    Field flowersField = setJerooFlowersField(1);

    context.checking(worldSizeExpectations(5, 5));
    jeroo.setGridLocation(4, 4);
    jeroo.toss();
    assertThat(flowersField.get(jeroo), is(0));
    assertThat(jeroo.hasFlower(), is(false));
  }

  @Test
  public void testTossWhenNextToNet() throws Exception {
    Field flowersField = setJerooFlowersField(1);

    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(3, 2, Net.class, net));
    context.checking(new Expectations() { {
      oneOf(net).remove();
    } });

    jeroo.setGridLocation(2, 2);
    jeroo.toss();
    assertThat(flowersField.get(jeroo), is(0));
    assertThat(jeroo.hasFlower(), is(false));
  }

  @Test
  public void testTossWhenNoNetThere() throws Exception {
    Field flowersField = setJerooFlowersField(1);

    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(3, 2, Net.class, null));

    jeroo.setGridLocation(2, 2);
    jeroo.toss();
    assertThat(flowersField.get(jeroo), is(0));
    assertThat(jeroo.hasFlower(), is(false));
  }

  @Test
  public void testGiveWhenNoFlowers() {
    context.checking(worldSizeExpectations(5, 5));

    jeroo.setGridLocation(2, 2);
    jeroo.give(RelativeDirection.LEFT);
  }

  @Test
  public void testGiveWhenDirectionIsHere() throws Exception {
    setJerooFlowersField(1);
    context.checking(worldSizeExpectations(5, 5));

    jeroo.setGridLocation(2, 2);
    jeroo.give(RelativeDirection.HERE);
  }

  @Test
  public void testGiveWhenJerooExists() throws Exception {
    Jeroo buddy = context.mock(Jeroo.class, "buddy");

    Field flowersField = setJerooFlowersField(1);
    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(3, 2, Jeroo.class, buddy));

    jeroo.setGridLocation(2, 2);
    jeroo.give(RelativeDirection.AHEAD);
    assertThat(jeroo.hasFlower(), is(false));
    assertThat(flowersField.get(buddy), is(1));
  }

  @Test
  public void testTurningAndFacing() {
    context.checking(worldSizeExpectations(5, 5));
    jeroo.setGridLocation(2, 2);

    assertThat(jeroo.isFacing(CompassDirection.EAST), is(true));
    jeroo.turn(RelativeDirection.AHEAD);
    assertThat(jeroo.isFacing(CompassDirection.EAST), is(true));
    jeroo.turn(RelativeDirection.HERE);
    assertThat(jeroo.isFacing(CompassDirection.EAST), is(true));

    // Turning left
    assertThat(jeroo.isFacing(CompassDirection.EAST), is(true));
    jeroo.turn(RelativeDirection.LEFT);
    assertThat(jeroo.isFacing(CompassDirection.NORTH), is(true));
    jeroo.turn(RelativeDirection.LEFT);
    assertThat(jeroo.isFacing(CompassDirection.WEST), is(true));
    jeroo.turn(RelativeDirection.LEFT);
    assertThat(jeroo.isFacing(CompassDirection.SOUTH), is(true));

    // Turning right
    jeroo.turn(RelativeDirection.RIGHT);
    assertThat(jeroo.isFacing(CompassDirection.WEST), is(true));
    jeroo.turn(RelativeDirection.RIGHT);
    assertThat(jeroo.isFacing(CompassDirection.NORTH), is(true));
    jeroo.turn(RelativeDirection.RIGHT);
    assertThat(jeroo.isFacing(CompassDirection.EAST), is(true));
    jeroo.turn(RelativeDirection.RIGHT);
    assertThat(jeroo.isFacing(CompassDirection.SOUTH), is(true));
  }

  @Test
  public void testSeesFlower() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(1, 0, Flower.class, flower));
    context.checking(hereExpectations(0, 1, Flower.class, null));
    jeroo.setGridLocation(0, 0);

    assertThat(jeroo.seesFlower(RelativeDirection.AHEAD), is(true));
    assertThat(jeroo.seesFlower(RelativeDirection.LEFT), is(false));
    assertThat(jeroo.seesFlower(RelativeDirection.RIGHT), is(false));
  }

  @Test
  public void testSeesJeroo() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(1, 0, Jeroo.class, jeroo));
    context.checking(hereExpectations(0, 1, Jeroo.class, null));
    jeroo.setGridLocation(0, 0);

    assertThat(jeroo.seesJeroo(RelativeDirection.AHEAD), is(true));
    assertThat(jeroo.seesJeroo(RelativeDirection.LEFT), is(false));
    assertThat(jeroo.seesJeroo(RelativeDirection.RIGHT), is(false));
  }

  @Test
  public void testSeesNet() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(1, 0, Net.class, net));
    context.checking(hereExpectations(0, 1, Net.class, null));
    jeroo.setGridLocation(0, 0);

    assertThat(jeroo.seesNet(RelativeDirection.AHEAD), is(true));
    assertThat(jeroo.seesNet(RelativeDirection.LEFT), is(false));
    assertThat(jeroo.seesNet(RelativeDirection.RIGHT), is(false));
  }

  @Test
  public void testSeesWater() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(1, 0, Water.class, water));
    context.checking(hereExpectations(0, 1, Water.class, null));
    jeroo.setGridLocation(0, 0);

    assertThat(jeroo.seesWater(RelativeDirection.AHEAD), is(true));
    assertThat(jeroo.seesWater(RelativeDirection.LEFT), is(false));
    assertThat(jeroo.seesWater(RelativeDirection.RIGHT), is(false));
  }

  @Test
  public void testIsClear() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(hereExpectations(1, 0, Actor.class, water));
    context.checking(hereExpectations(0, 1, Actor.class, null));
    jeroo.setGridLocation(0, 0);

    assertThat(jeroo.isClear(RelativeDirection.AHEAD), is(false));
    assertThat(jeroo.isClear(RelativeDirection.LEFT), is(false));
    assertThat(jeroo.isClear(RelativeDirection.RIGHT), is(true));
  }

  @Test
  public void testIncapicitate() {
    String message = "This is a message";
    expectingIncapcitation(message);
    jeroo.incapacitate(message);
  }

  private Expectations worldSizeExpectations(final int height, final int width) {
    return new Expectations() { {
      allowing(world).getHeight();
      will(returnValue(height));
      allowing(world).getWidth();
      will(returnValue(width));
    } };
  }

  private Expectations hereExpectationsForHopping(final int x, final int y,
      final Net net, final Water water, final List<Jeroo> jeroos) {
    return new Expectations() { {
      allowing(world).getOneObjectAt(x, y, Net.class);
      will(returnValue(net));
      allowing(world).getOneObjectAt(x, y, Water.class);
      will(returnValue(water));
      allowing(world).getObjectsAt(x, y, Jeroo.class);
      will(returnValue(jeroos));
    } };
  }

  private Expectations hereExpectations(final int x, final int y,
      final Class<? extends Actor> cls, Actor actor) {
    return new Expectations() { {
      allowing(world).getOneObjectAt(x, y, cls);
      will(returnValue(actor));
    } };
  }

  private void expectingIncapcitation(String message) {
    expectedException.expect(RuntimeException.class);
    expectedException.expectMessage(message);
  }

  private Field setJerooFlowersField(int numFlowers) throws NoSuchFieldException, IllegalAccessException {
    Field flowersField = Jeroo.class.getDeclaredField("flowers");
    flowersField.setAccessible(true);
    flowersField.setInt(jeroo, numFlowers);
    return flowersField;
  }

}