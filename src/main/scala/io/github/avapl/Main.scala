package io.github.avapl

import cats.effect.{ExitCode, IO, IOApp}
import cats.~>
import doobie.*
import doobie.implicits.*
import io.github.avapl.repository.postgres.{PostgresAccountRepository, PostgresPointsRepository}
import io.github.avapl.service.AccountManagementService

import java.util.UUID
import scala.util.Random

object Main extends IOApp {

  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://localhost:5432/postgres",
    user = "postgres",
    pass = "example"
  )
  given (ConnectionIO ~> IO) with
    def apply[A](connectionIO: ConnectionIO[A]): IO[A] = connectionIO.transact(xa)
  val accountManagementService: AccountManagementService[ConnectionIO, IO] =
    AccountManagementService(
      accountRepository = PostgresAccountRepository(),
      pointsRepository = PostgresPointsRepository()
    )

  override def run(args: List[String]): IO[ExitCode] = {
    val userId = UUID.randomUUID()
    println(s"userId = $userId")
    accountManagementService
      .addFunds(userId, 10)
      .map(_ => ExitCode.Success)
  }
}
