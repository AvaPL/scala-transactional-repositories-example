package io.github.avapl.service

import cats.{Id, ~>}
import cats.arrow.FunctionK
import io.github.avapl.repository.{AccountRepository, PointsRepository}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.util.UUID

class AccountManagementServiceTest extends AnyWordSpec with Matchers {

  "addFunds" when {
    "given an user ID and an amount to add" should {
      "call the account repository with the amount and increment points in the points repository" in {
        val testUserId = UUID.randomUUID()
        val testAmountToAdd = 123

        val accountRepository = new AccountRepository[Id] {
          def addFunds(userId: UUID, amountToAdd: Int): Unit = {
            userId shouldBe testUserId
            amountToAdd shouldBe testAmountToAdd
            ()
          }

          def getBalance(userId: UUID): Option[Int] = ???
        }
        val pointsRepository = new PointsRepository[Id] {
          def incrementPoints(userId: UUID): Unit = {
            userId shouldBe testUserId
            ()
          }

          def getPoints(userId: UUID): Option[Int] = ???
        }
        given (Id ~> Id) = FunctionK.id[Id]

        val accountManagementService =
          AccountManagementService(accountRepository, pointsRepository)

        accountManagementService.addFunds(testUserId, testAmountToAdd)
      }
    }
  }
}
