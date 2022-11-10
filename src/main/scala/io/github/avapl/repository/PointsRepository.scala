package io.github.avapl.repository

import cats.effect.IO

import java.util.UUID
import doobie.ConnectionIO

trait PointsRepository {

  def getPoints(userId: UUID): ConnectionIO[Option[Int]]

  def incrementPoints(userId: UUID): ConnectionIO[Unit]
}
