package com.alexkorotkikh.rps

import org.scalatest.{FlatSpec, Matchers}

class GameSpec extends FlatSpec with Matchers {
  "Game" should "be created with zero scores" in {
    val game = new Game

    game.score shouldEqual Map("Player1" -> 0, "Player2" -> 0)
  }
  it should "add a point to Player1 if his move wins" in {
    val game = new Game

    game.playRound(HumanMove(Rock), HumanMove(Scissors))

    game.score shouldEqual Map("Player1" -> 1, "Player2" -> 0)
  }
  it should "add a point to Player2 if his move wins" in {
    val game = new Game

    game.playRound(HumanMove(Paper), HumanMove(Scissors))

    game.score shouldEqual Map("Player1" -> 0, "Player2" -> 1)
  }
  it should "not change the score if round was draw" in {
    val game = new Game

    game.playRound(HumanMove(Paper), HumanMove(Paper))

    game.score shouldEqual Map("Player1" -> 0, "Player2" -> 0)
  }
}
