package io.github.avapl.repository.postgres

import cats.effect.IO
import doobie.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import io.github.avapl.repository.AccountRepository

import java.util.UUID

class PostgresAccountRepository(
    xa: Transactor[IO]
) extends AccountRepository {

  override def getBalance(userId: UUID): IO[Option[Int]] =
    sql"select balance from account where user_id = $userId"
      .query[Int]
      .option
      .transact(xa)

  override def addFunds(userId: UUID, amountToAdd: Int): IO[Unit] =
    for {
      currentBalance <- getBalance(userId)
      newBalance = currentBalance.getOrElse(0) + amountToAdd
      _ <- setBalance(userId, newBalance)
    } yield ()

  private def setBalance(userId: UUID, balance: Int): IO[Int] =
    sql"""
      insert into account values ($userId, $balance)
      on conflict (user_id)
      do update set balance = $balance
    """.update.run
      .transact(xa)
}
