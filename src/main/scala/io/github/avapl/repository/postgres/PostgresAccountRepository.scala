package io.github.avapl.repository.postgres

import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import io.github.avapl.repository.AccountRepository

import java.util.UUID

class PostgresAccountRepository extends AccountRepository[ConnectionIO] {

  override def getBalance(userId: UUID): ConnectionIO[Option[Int]] =
    getBalanceConnectionIO(userId)

  private def getBalanceConnectionIO(userId: UUID): ConnectionIO[Option[Int]] =
    sql"select balance from account where user_id = $userId"
      .query[Int]
      .option

  override def addFunds(userId: UUID, amountToAdd: Int): ConnectionIO[Unit] =
    for {
      currentBalance <- getBalanceConnectionIO(userId)
      newBalance = currentBalance.getOrElse(0) + amountToAdd
      _ <- setBalanceConnectionIO(userId, newBalance)
    } yield ()

  private def setBalanceConnectionIO(
      userId: UUID,
      balance: Int
  ): ConnectionIO[Int] =
    sql"""
      insert into account values ($userId, $balance)
      on conflict (user_id)
      do update set balance = $balance
    """.update.run
}
