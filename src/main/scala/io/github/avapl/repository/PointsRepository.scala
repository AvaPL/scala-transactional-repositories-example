package io.github.avapl.repository

import cats.effect.IO

import java.util.UUID

trait PointsRepository {

  def getPoints(userId: UUID): IO[Option[Int]]

  def incrementPoints(userId: UUID): IO[Unit]
}
