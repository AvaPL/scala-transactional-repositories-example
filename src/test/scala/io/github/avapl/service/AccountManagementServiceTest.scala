package io.github.avapl.service

import cats.{Id, ~>}
import cats.arrow.FunctionK
import io.github.avapl.repository.{AccountRepository, PointsRepository}
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.util.UUID

class AccountManagementServiceTest
    extends AnyWordSpec
    with Matchers
    with MockFactory {

  "addFunds" when {
    "given an user ID and an amount to add" should {
      "call the account repository with the amount and increment points in the points repository" in {
        val userId = UUID.randomUUID()
        val amountToAdd = 123

        val accountRepository = mock[AccountRepository[Id]]
        (accountRepository.addFunds _).expects(userId, amountToAdd).returning(())
        val pointsRepository = mock[PointsRepository[Id]]
        (pointsRepository.incrementPoints _).expects(userId).returning(())
        implicit val transactor: Id ~> Id = FunctionK.id

        val accountManagementService =
          new AccountManagementService(accountRepository, pointsRepository)

        accountManagementService.addFunds(userId, amountToAdd)
      }
    }
  }
}
