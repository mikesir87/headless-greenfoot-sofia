package io.mikesir87.sofia.micro;

/**
 * Represents a program (or script)--that is, a sequence of actions on a
 * programmable micro-world object to control its behavior.
 *
 * @author  Stephen Edwards
 * @author  Michael Irwin
 * @version 2016.02.17
 */
public interface Program {

  /**
   * Represents a sequence of actions to carry out, one turn at a time.
   */
  void myProgram();
}
