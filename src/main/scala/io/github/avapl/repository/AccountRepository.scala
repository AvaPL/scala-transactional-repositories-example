package io.github.avapl.repository

import cats.effect.IO

import java.util.UUID

trait AccountRepository {

  def getBalance(userId: UUID): IO[Option[Int]]

  def addFunds(userId: UUID, amountToAdd: Int): IO[Unit]
}
