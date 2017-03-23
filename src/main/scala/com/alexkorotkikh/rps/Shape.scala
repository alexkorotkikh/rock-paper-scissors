package com.alexkorotkikh.rps

/**
  * Trait Shape defines possible choices player can take in the game.
  */
trait Shape {

  /**
    * Defines which other Shape(s) this Shape beats
    * @param other Shape
    * @return true if this Shape beats other Shape
    *         false if other Shape beats this Shape or if they draw
    */
  def beats(other: Shape): Boolean
}

case object Rock extends Shape {
  override def beats(other: Shape): Boolean = other == Scissors
}

case object Paper extends Shape {
  override def beats(other: Shape): Boolean = other == Rock
}

case object Scissors extends Shape {
  override def beats(other: Shape): Boolean = other == Paper
}
