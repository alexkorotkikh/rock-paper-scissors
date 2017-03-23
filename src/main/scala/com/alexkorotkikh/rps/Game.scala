package com.alexkorotkikh.rps

import scala.collection.mutable
import scala.util.Random

/**
  * Game encapsulates the logic of rock-scissor-paper game and is responsible for
  * defining winning move of each round and keeping score between rounds
  */
class Game {
  private val scoreMap = mutable.Map("Player1" -> 0, "Player2" -> 0)

  def playRound(player1Move: Move = BotMove(), player2Move: Move = BotMove()): Option[Move] = {
    val p1Shape = player1Move.shape
    val p2Shape = player2Move.shape

    if (p1Shape beats p2Shape) {
      scoreMap.put("Player1", scoreMap("Player1") + 1)
      Option(player1Move)
    } else if (p2Shape beats p1Shape) {
      scoreMap.put("Player2", scoreMap("Player2") + 1)
      Option(player2Move)
    } else None
  }

  def score: Map[String, Int] = scoreMap.toMap
}

trait Move {
  def shape: Shape
}

case class HumanMove(nextShape: Shape) extends Move {
  override def shape: Shape = nextShape
}

case class BotMove() extends Move {
  private val shapes    = Seq(Rock, Paper, Scissors)
  private val nextShape = Random.shuffle(shapes).head

  override def shape: Shape = nextShape

}
