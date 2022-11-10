package io.github.avapl.repository

import cats.effect.IO
import doobie.ConnectionIO

import java.util.UUID

trait AccountRepository {

  def getBalance(userId: UUID): ConnectionIO[Option[Int]]

  def addFunds(userId: UUID, amountToAdd: Int): ConnectionIO[Unit]
}
