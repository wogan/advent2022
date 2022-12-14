package dev.wogan.advent

import day01.Day01

import cats.effect.IO
import fs2.Stream
import munit.CatsEffectSuite

abstract class DayExample extends CatsEffectSuite {

  extension (example: String)
    def input: Stream[IO, String] =
      Stream.emits[IO, String](example.split("\n"))

    def run(f: Input => Output): IO[String] =
      f(example.input).compile.lastOrError

  class DayTester(day: Day, example: String):
    private def registerTest(f: Input => Output, part: Int, expected: Any): Unit =
      test(s"Day ${day.number} part ${part}") {
        assertIO(example.run(f), expected.toString, s"Day ${day.number} part 1 did not match expected value")
      }

    def part1(expected: Any): DayTester =
      registerTest(day.part1, 1, expected)
      this

    def part2(expected: Any): DayTester =
      registerTest(day.part2, 2, expected)
      this

  extension (day: Day)
    def example(example: String): DayTester =
      DayTester(day, example)
}
