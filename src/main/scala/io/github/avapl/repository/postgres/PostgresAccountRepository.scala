package io.github.avapl.repository.postgres

import cats.effect.IO
import doobie.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import io.github.avapl.repository.AccountRepository

import java.util.UUID

class PostgresAccountRepository(xa: Transactor[IO]) extends AccountRepository {

  override def getBalance(userId: UUID): IO[Option[Int]] =
    getBalanceConnectionIO(userId).transact(xa)

  private def getBalanceConnectionIO(userId: UUID): ConnectionIO[Option[Int]] =
    sql"select balance from account where user_id = $userId"
      .query[Int]
      .option

  override def addFunds(userId: UUID, amountToAdd: Int): IO[Unit] = {
    for {
      currentBalance <- getBalanceConnectionIO(userId)
      newBalance = currentBalance.getOrElse(0) + amountToAdd
      _ <- setBalanceConnectionIO(userId, newBalance)
    } yield ()
  }.transact(xa)

  private def setBalanceConnectionIO(userId: UUID, balance: Int): ConnectionIO[Int] =
    sql"""
      insert into account values ($userId, $balance)
      on conflict (user_id)
      do update set balance = $balance
    """.update.run
}
