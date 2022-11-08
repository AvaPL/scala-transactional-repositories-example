package io.github.avapl.service

import cats.effect.IO
import io.github.avapl.repository.{AccountRepository, PointsRepository}

import java.util.UUID

class AccountManagementService(
    accountRepository: AccountRepository,
    pointsRepository: PointsRepository
) {

  def addFunds(userId: UUID, amountToAdd: Int): IO[Unit] =
    for {
      _ <- accountRepository.addFunds(userId, amountToAdd)
      _ <- pointsRepository.incrementPoints(userId)
    } yield ()
}
