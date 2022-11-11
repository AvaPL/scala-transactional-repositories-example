package io.github.avapl.service

import cats.effect.IO
import cats.syntax.all.*
import cats.{Monad, ~>}
import io.github.avapl.repository.{AccountRepository, PointsRepository}

import java.util.UUID

class AccountManagementService[F[_]: Monad, G[_]](
    accountRepository: AccountRepository[F],
    pointsRepository: PointsRepository[F]
)(using transactor: F ~> G) {

  def addFunds(userId: UUID, amountToAdd: Int): G[Unit] =
    transactor {
      for {
        _ <- accountRepository.addFunds(userId, amountToAdd)
        _ <- pointsRepository.incrementPoints(userId)
      } yield ()
    }
}
