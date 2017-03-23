package com.alexkorotkikh.rps

import java.io.PrintStream

import org.mockito.ArgumentCaptor
import org.mockito.Mockito._
import org.mockito.{ArgumentMatchers => ArgMatchers}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class ConsoleSpec extends FlatSpec with Matchers with MockitoSugar {
  "Console" should "not fall in case of unknown command" in {
    val (console, out) = prepareMocks("unknown")

    console.run()

    verify(out).println("Unknown command: unknown")
  }

  it should "quit the game when player type 'quit'" in {
    val (console, out) = prepareMocks("quit")

    console.run()

    verify(out).println("Bye-bye!")
  }

  it should "allow player to play rock" in {
    val (console, out) = prepareMocks("rock")

    console.run()

    verify(console.game)
      .playRound(ArgMatchers.eq(HumanMove(Rock)), ArgMatchers.any(classOf[BotMove]))

    val argCaptor = ArgumentCaptor.forClass(classOf[String]).asInstanceOf[ArgumentCaptor[String]]
    verify(out, times(3)).println(argCaptor.capture())
    argCaptor.getAllValues.get(1) should startWith("You played rock")
  }

  it should "allow player to play paper" in {
    val (console, out) = prepareMocks("paper")

    console.run()

    verify(console.game)
      .playRound(ArgMatchers.eq(HumanMove(Paper)), ArgMatchers.any(classOf[BotMove]))

    val argCaptor = ArgumentCaptor.forClass(classOf[String]).asInstanceOf[ArgumentCaptor[String]]
    verify(out, times(3)).println(argCaptor.capture())
    argCaptor.getAllValues.get(1) should startWith("You played paper")

  }
  it should "allow player to play scissors" in {
    val (console, out) = prepareMocks("scissors")

    console.run()

    verify(console.game)
      .playRound(ArgMatchers.eq(HumanMove(Scissors)), ArgMatchers.any(classOf[BotMove]))

    val argCaptor = ArgumentCaptor.forClass(classOf[String]).asInstanceOf[ArgumentCaptor[String]]
    verify(out, times(3)).println(argCaptor.capture())
    argCaptor.getAllValues.get(1) should startWith("You played scissors")
  }

  it should "allow player to start a game between two bots" in {
    val (console, _) = prepareMocks("bots 10")
    val botsGame     = console.game

    console.run()

    verify(botsGame, times(10))
      .playRound(ArgMatchers.any(classOf[BotMove]), ArgMatchers.any(classOf[BotMove]))
  }

  it should "allow player to show current score" in {
    val (console, out) = prepareMocks("score")

    console.run()

    verify(console.game).score
    verify(out).println("Current score: Player1 - 0, Player2 - 0")
  }

  it should "allow player to restart a game" in {
    val (console, out) = prepareMocks("restart")

    console.run()

    console.game.score shouldEqual new Game().score
    verify(out).println("New game started")
  }

  it should "allow player to show a help message" in {
    val (console, out) = prepareMocks("help")

    console.run()

    verify(out).println(
      """Type 'rock', 'paper' or 'scissors' to play a round.
        |Type 'score' to show current score.
        |Type 'restart' to reset the score.
        |Type 'bots N' to play N rounds between 2 bots.
        |Type 'help' to show this message.""".stripMargin
    )
  }

  it should "understand commands in different cases" in {
    val (console, _) = prepareMocks("ROCK", "Paper", "ScIssORs")

    console.run()

    verify(console.game)
      .playRound(ArgMatchers.eq(HumanMove(Rock)), ArgMatchers.any(classOf[BotMove]))
    verify(console.game)
      .playRound(ArgMatchers.eq(HumanMove(Paper)), ArgMatchers.any(classOf[BotMove]))
    verify(console.game)
      .playRound(ArgMatchers.eq(HumanMove(Scissors)), ArgMatchers.any(classOf[BotMove]))
  }

  it should "understand commands with leading or trailing spaces" in {
    val (console, _) = prepareMocks("rock ", " Paper", " ScIssORs ")

    console.run()

    verify(console.game)
      .playRound(ArgMatchers.eq(HumanMove(Rock)), ArgMatchers.any(classOf[BotMove]))
    verify(console.game)
      .playRound(ArgMatchers.eq(HumanMove(Paper)), ArgMatchers.any(classOf[BotMove]))
    verify(console.game)
      .playRound(ArgMatchers.eq(HumanMove(Scissors)), ArgMatchers.any(classOf[BotMove]))
  }

  private def prepareMocks(cmds: String*) = {
    val out     = mock[PrintStream]
    val console = spy(new Console(out))
    console.game = spy(console.game)
    when(console.readLines).thenReturn(cmds.iterator)
    (console, out)
  }
}
