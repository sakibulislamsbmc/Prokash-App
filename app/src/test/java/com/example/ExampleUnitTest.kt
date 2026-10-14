package com.example

import com.example.data.ProkahRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testAuthentication() {
        val repo = ProkahRepository()
        assertFalse(repo.isLoggedIn.value)

        // Invalid pin
        val fail = repo.login("9999")
        assertFalse(fail)
        assertFalse(repo.isLoggedIn.value)

        // Correct pin
        val success = repo.login("1234")
        assertTrue(success)
        assertTrue(repo.isLoggedIn.value)

        // Logout
        repo.logout()
        assertFalse(repo.isLoggedIn.value)
    }

    @Test
    fun testSendMoneyUpdatesBalanceAndHistory() {
        val repo = ProkahRepository()
        val initialBalance = repo.userProfile.value.balance
        val initialHistoryCount = repo.transactions.value.size

        val tx = repo.sendMoney("Abrar Fahim", "01812987654", 500.0, "Gift")

        assertEquals(initialBalance - 500.0, repo.userProfile.value.balance, 0.001)
        assertEquals(initialHistoryCount + 1, repo.transactions.value.size)
        assertEquals("Abrar Fahim", tx.counterPartyName)
        assertEquals(500.0, tx.amount, 0.001)
    }

    @Test
    fun testCashOutCalculatesFee() {
        val repo = ProkahRepository()
        val initialBalance = repo.userProfile.value.balance

        val tx = repo.cashOut("01755443322", "Agent", 1000.0)

        // 1.25% of 1000 = 12.50
        assertEquals(12.50, tx.fee, 0.01)
        assertEquals(initialBalance - 1000.0 - 12.50, repo.userProfile.value.balance, 0.01)
    }
}
