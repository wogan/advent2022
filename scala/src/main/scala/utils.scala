package dev.wogan.advent.scala

import cats.Order
import cats.collections.Heap
import cats.effect.IO
import cats.syntax.all.*
import fs2.Stream

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

given Conversion[Stream[IO, Int], Stream[IO, String]] =
  _.map(_.toString)

extension [A: Order](heap: Heap[A])
  def offer(a: A, limit: Int): Heap[A] =
    if heap.size < limit then
      heap.add(a)
    else if heap.getMin.exists(_ > a) then
      heap
    else
      heap.add(a).remove
