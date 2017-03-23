name := "rock-paper-scissors"

version := "1.0"

scalaVersion := "2.12.1"

coverageEnabled := true

mainClass in Compile := Some("com.alexkorotkikh.rps.Main")

libraryDependencies ++= Seq(
  "org.scalatest"  %% "scalatest"   % "3.0.1"  % "test",
  "org.mockito"    % "mockito-core" % "2.7.17" % "test"
)
