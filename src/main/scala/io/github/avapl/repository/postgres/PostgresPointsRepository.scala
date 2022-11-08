package io.github.avapl.repository.postgres

import cats.effect.IO
import doobie.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import io.github.avapl.repository.PointsRepository

import java.util.UUID

class PostgresPointsRepository(
    xa: Transactor[IO]
) extends PointsRepository {

  override def getPoints(userId: UUID): IO[Option[Int]] =
    sql"select points from points where user_id = $userId"
      .query[Int]
      .option
      .transact(xa)

  override def incrementPoints(userId: UUID): IO[Unit] =
    for {
      currentPoints <- getPoints(userId)
      newPoints = currentPoints.getOrElse(0) + 1
      _ <- setPoints(userId, newPoints)
    } yield ()

  private def setPoints(userId: UUID, points: Int): IO[Int] =
    sql"""
      insert into points values ($userId, $points)
      on conflict (user_id)
      do update set points = $points
    """.update.run
      .transact(xa)
}
