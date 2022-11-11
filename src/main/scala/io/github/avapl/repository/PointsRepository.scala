package io.github.avapl.repository

import java.util.UUID

trait PointsRepository[F[_]] {

  def getPoints(userId: UUID): F[Option[Int]]

  def incrementPoints(userId: UUID): F[Unit]
}
