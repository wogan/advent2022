package dev.wogan.advent

import cats.Order
import cats.collections.Heap
import cats.effect.IO
import cats.syntax.all.*
import fs2.Stream
import fs2.io.file.{Files, Path}

type Input = Stream[IO, String]
type Output = Stream[IO, String]

def loadFile(filename: String): Input =
  Files[IO].readUtf8Lines(Path("src/main/resources").absolute / Path(filename))

extension (s: String)
  def cleave(): (String, String) =
    s.splitAt(s.length / 2)

extension (r: Range)
  def contains(other: Range): Boolean =
    r.start <= other.start && r.end >= other.end

  def overlaps(other: Range): Boolean =
    (r.start >= other.start && r.start <= other.end)
      || (r.end >= other.start && r.end <= other.end)

extension (stream: Stream[IO, Int])
  def max: Stream[IO, Int] =
    stream.reduce(Math.max)

  def sum: Stream[IO, Int] =
    stream.reduce(_ + _)

  def asString: Stream[IO, String] =
    stream.map(_.toString)

implicit class LimitHeap[A: Order](heap: Heap[A]) {
  def offer(a: A, limit: Int): Heap[A] = {
    if heap.size < limit then
      heap.add(a)
    else if heap.getMin.exists(_ > a) then
      heap
    else
      heap.add(a).remove
  }
}
