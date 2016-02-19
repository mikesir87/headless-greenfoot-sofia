package io.mikesir87.sofia.micro.lightbot;

import io.mikesir87.sofia.micro.World;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for the {@link LightBot}
 *
 * @author Michael Irwin
 */
public class LightBotTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() { {
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  private World world;

  @Mock
  private Block block;

  @Mock
  private LightableTile lightableTile;

  private LightBot lightBot = new LightBot();

  @Before
  public void setUp() {
    lightBot.addedToWorld(world);
  }

  @Test
  public void testLightBotConstructorSettingLocation() {
    lightBot = new LightBot(3, 3);
    assertThat(lightBot.getGridX(), is(3));
    assertThat(lightBot.getGridY(), is(3));
  }

  @Test
  public void testMoveWhenOnFlatLand() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(blockExpectations(2, 2, Collections.emptyList()));
    context.checking(blockExpectations(3, 2, Collections.emptyList()));

    lightBot.setGridLocation(2, 2);
    lightBot.move();
    assertThat(lightBot.getGridX(), is(3));
  }

  @Test
  public void testMoveWhenNextToABlock() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(blockExpectations(2, 2, Collections.emptyList()));
    context.checking(blockExpectations(3, 2, Arrays.asList(block)));

    lightBot.setGridLocation(2, 2);
    lightBot.move();
    assertThat(lightBot.getGridX(), is(2));
  }

  @Test
  public void testMoveWhenOnABlock() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(blockExpectations(2, 2, Arrays.asList(block)));
    context.checking(blockExpectations(3, 2, Collections.emptyList()));

    lightBot.setGridLocation(2, 2);
    lightBot.move();
    assertThat(lightBot.getGridX(), is(2));
  }

  @Test
  public void testMoveWhenOnABlockAndNextToOneOfSameHeight() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(blockExpectations(2, 2, Arrays.asList(block)));
    context.checking(blockExpectations(3, 2, Arrays.asList(block)));

    lightBot.setGridLocation(2, 2);
    lightBot.move();
    assertThat(lightBot.getGridX(), is(3));
  }

  @Test
  public void testMoveWhenOnWorldBoundary() {
    context.checking(worldSizeExpectations(5, 5));
    lightBot.setGridLocation(4, 4);
    lightBot.move();
    assertAtLocation(lightBot, 4, 4);
  }

  @Test
  public void testTurningRightAndMoving() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(new Expectations() { {
      allowing(world).getObjectsAt(with(any(int.class)),
          with(any(int.class)), with(Block.class));
      will(returnValue(Collections.emptyList()));
    } });
    lightBot.setGridLocation(2, 2);

    // Starting out facing EAST. Turning SOUTH
    lightBot.turnRight();
    lightBot.move();
    assertAtLocation(lightBot, 2, 3);

    // Turning WEST
    lightBot.turnRight();
    lightBot.move();
    assertAtLocation(lightBot, 1, 3);

    // Turning NORTH
    lightBot.turnRight();
    lightBot.move();
    assertAtLocation(lightBot, 1, 2);

    // Turning EAST
    lightBot.turnRight();
    lightBot.move();
    assertAtLocation(lightBot, 2, 2);
  }

  @Test
  public void testTurningLeftAndMoving() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(new Expectations() { {
      allowing(world).getObjectsAt(with(any(int.class)),
          with(any(int.class)), with(Block.class));
      will(returnValue(Collections.emptyList()));
    } });
    lightBot.setGridLocation(2, 2);

    // Starting out facing EAST. Turning NORTH
    lightBot.turnLeft();
    lightBot.move();
    assertAtLocation(lightBot, 2, 1);

    // Turning WEST
    lightBot.turnLeft();
    lightBot.move();
    assertAtLocation(lightBot, 1, 1);

    // Turning SOUTH
    lightBot.turnLeft();
    lightBot.move();
    assertAtLocation(lightBot, 1, 2);

    // Turning EAST
    lightBot.turnLeft();
    lightBot.move();
    assertAtLocation(lightBot, 2, 2);
  }

  @Test
  public void testJumpUpToBlock() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(blockExpectations(2, 2, Collections.emptyList()));
    context.checking(blockExpectations(3, 2, Arrays.asList(block)));

    lightBot.setGridLocation(2, 2);
    lightBot.jump();
    assertAtLocation(lightBot, 3, 2);
  }

  @Test
  public void testJumpDownFromBlock() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(blockExpectations(2, 2, Arrays.asList(block)));
    context.checking(blockExpectations(3, 2, Collections.emptyList()));

    lightBot.setGridLocation(2, 2);
    lightBot.jump();
    assertAtLocation(lightBot, 3, 2);
  }

  @Test
  public void testJumpWhenAtEdgeOfWorld() {
    context.checking(worldSizeExpectations(5, 5));

    lightBot.setGridLocation(4, 4);
    lightBot.jump();
    assertAtLocation(lightBot, 4, 4);
  }

  @Test
  public void testTurnLightOn() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(lightableTileExpectations(2, 2, lightableTile));
    context.checking(new Expectations() { {
      oneOf(lightableTile).turnLightOn();
    } });

    lightBot.setGridLocation(2, 2);
    lightBot.turnLightOn();
  }

  @Test
  public void testTurnLightOnWhenNoLightableTileThere() {
    context.checking(worldSizeExpectations(5, 5));
    context.checking(lightableTileExpectations(2, 2, null));

    lightBot.setGridLocation(2, 2);
    lightBot.turnLightOn();
  }

  private void assertAtLocation(LightBot bot, int x, int y) {
    assertThat(bot.getGridX(), is(x));
    assertThat(bot.getGridY(), is(y));
  }

  private Expectations worldSizeExpectations(final int width,
      final int height) {
    return new Expectations() { {
      allowing(world).getHeight();
      will(returnValue(height));
      allowing(world).getWidth();
      will(returnValue(width));
    } };
  }

  private Expectations blockExpectations(final int x, final int y,
      final List<?> objects) {
    return new Expectations() { {
      allowing(world).getObjectsAt(x, y, Block.class);
      will(returnValue(objects));
    } };
  }

  private Expectations lightableTileExpectations(final int x, final int y,
      final Object objectAtLocation) {
    return new Expectations() { {
      oneOf(world).getOneObjectAt(x, y, LightableTile.class);
      will(returnValue(objectAtLocation));
    } };
  }
}
