package com.alexkorotkikh.rps

import java.io.PrintStream

import scala.io.StdIn

/**
  * Console defines command line interface for a rock-scissors-paper game.
  * It consumes commands from stdin and prints result of each command to
  * defined java.io.PrintStream, by default - stdout.
  *
  * @param out java.io.PrintStream, defaults to System.out
  */
class Console(out: PrintStream = System.out) {
  private[rps] var game = new Game
  private val botsRegex = "bots (\\d+)".r

  def run(): Unit = {
    out.println("Welcome to the Rock-Paper-Scissors game. Type 'help' for more information.")

    readLines
      .map(readCommand)
      .takeWhile(_ != QuitCommand)
      .foreach(cmd => out.println(cmd.execute()))

    out.println(QuitCommand.execute())
  }

  protected def readCommand(strCmd: String): Command =
    Option(strCmd).map(_.trim.toLowerCase()).getOrElse("") match {
      case "rock"       => PlayRoundCommand(Rock)
      case "paper"      => PlayRoundCommand(Paper)
      case "scissors"   => PlayRoundCommand(Scissors)
      case "restart"    => RestartCommand
      case "score"      => ShowScoreCommand
      case botsRegex(n) => PlayBotsGameCommand(n.toInt)
      case "help"       => ShowHelpCommand
      case "quit"       => QuitCommand
      case _            => UnknownCommand(strCmd)
    }

  private[rps] def readLines =
    Iterator.continually(StdIn.readLine)

  trait Command {
    def execute(): String
  }

  case class PlayRoundCommand(shape: Shape) extends Command {
    override def execute(): String = {
      val move1     = HumanMove(shape)
      val move2     = BotMove()
      val winner    = game.playRound(move1, move2)
      val winnerStr = winner.map(w => s"${w.shape.toString} wins!").getOrElse("Draw!")
      s"""You played ${move1.shape.toString.toLowerCase()},
         |your opponent played ${move2.shape.toString.toLowerCase()}.
         |$winnerStr""".stripMargin
    }
  }

  case object RestartCommand extends Command {
    override def execute(): String = {
      game = new Game
      "New game started"
    }
  }

  case object ShowScoreCommand extends Command {
    override def execute(): String =
      "Current score: " + game.score.toList
        .sortBy(_._1)
        .map {
          case (name, score) => s"$name - $score"
        }
        .mkString(", ")
  }

  case class PlayBotsGameCommand(rounds: Int) extends Command {
    override def execute(): String = {
      1 to rounds foreach { _ =>
        game.playRound()
      }
      val str = ShowScoreCommand.execute()
      RestartCommand.execute()
      str
    }
  }

  case object ShowHelpCommand extends Command {
    override def execute(): String =
      """Type 'rock', 'paper' or 'scissors' to play a round.
        |Type 'score' to show current score.
        |Type 'restart' to reset the score.
        |Type 'bots N' to play N rounds between 2 bots.
        |Type 'help' to show this message.""".stripMargin
  }

  case object QuitCommand extends Command {
    override def execute(): String = "Bye-bye!"
  }

  case class UnknownCommand(strCmd: String) extends Command {
    override def execute(): String = s"Unknown command: $strCmd"
  }

}
