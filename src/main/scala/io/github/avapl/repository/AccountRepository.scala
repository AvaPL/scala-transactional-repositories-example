package io.github.avapl.repository

import java.util.UUID

trait AccountRepository[F[_]] {

  def getBalance(userId: UUID): F[Option[Int]]

  def addFunds(userId: UUID, amountToAdd: Int): F[Unit]
}
