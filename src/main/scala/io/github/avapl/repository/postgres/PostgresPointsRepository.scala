package io.github.avapl.repository.postgres

import cats.effect.IO
import doobie.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import io.github.avapl.repository.PointsRepository

import java.util.UUID

class PostgresPointsRepository extends PointsRepository[ConnectionIO] {

  override def getPoints(userId: UUID): ConnectionIO[Option[Int]] =
    getPointsConnectionIO(userId)

  private def getPointsConnectionIO(userId: UUID): ConnectionIO[Option[Int]] =
    sql"select points from points where user_id = $userId"
      .query[Int]
      .option

  override def incrementPoints(userId: UUID): ConnectionIO[Unit] = 
    for {
      currentPoints <- getPointsConnectionIO(userId)
      newPoints = currentPoints.getOrElse(0) + 1
      _ <- setPointsConnectionIO(userId, newPoints)
    } yield ()

  private def setPointsConnectionIO(userId: UUID, points: Int): ConnectionIO[Int] =
    sql"""
      insert into points values ($userId, $points)
      on conflict (user_id)
      do update set points = $points
    """.update.run
}
