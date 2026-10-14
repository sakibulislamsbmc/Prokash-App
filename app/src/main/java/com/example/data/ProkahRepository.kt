package com.example.data

import com.example.model.AppLanguage
import com.example.model.ContactItem
import com.example.model.NotificationItem
import com.example.model.Transaction
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ProkahRepository {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.BANGLA)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    // Pre-populate realistic MFS transactions
    private val _transactions = MutableStateFlow<List<Transaction>>(
        listOf(
            Transaction(
                id = "TRX9A83K1P",
                type = TransactionType.RECEIVED_MONEY,
                title = "Money Received",
                counterPartyName = "Abrar Fahim",
                counterPartyNumber = "01812987654",
                amount = 3500.00,
                fee = 0.0,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 45, // 45 mins ago
                reference = "Freelance Project",
                status = TransactionStatus.SUCCESS,
                balanceAfter = 24850.50
            ),
            Transaction(
                id = "TRX88B72LZ",
                type = TransactionType.MOBILE_RECHARGE,
                title = "Mobile Recharge",
                counterPartyName = "Grameenphone Pre-paid",
                counterPartyNumber = "01712345678",
                amount = 200.00,
                fee = 0.0,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 4, // 4 hours ago
                reference = "Data Bundle",
                status = TransactionStatus.SUCCESS,
                balanceAfter = 21350.50
            ),
            Transaction(
                id = "TRX76X41MQ",
                type = TransactionType.MERCHANT_PAY,
                title = "Merchant Payment",
                counterPartyName = "Aarong Bashundhara Outlet",
                counterPartyNumber = "01999887766",
                amount = 1850.00,
                fee = 0.0,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 22, // Yesterday
                reference = "Invoice #8821",
                status = TransactionStatus.SUCCESS,
                balanceAfter = 21550.50
            ),
            Transaction(
                id = "TRX65N39KL",
                type = TransactionType.SEND_MONEY,
                title = "Send Money",
                counterPartyName = "Tanvir Ahmed",
                counterPartyNumber = "01688776655",
                amount = 1200.00,
                fee = 0.0,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 36,
                reference = "Dinner share",
                status = TransactionStatus.SUCCESS,
                balanceAfter = 23400.50
            ),
            Transaction(
                id = "TRX54J28GH",
                type = TransactionType.BILL_PAY,
                title = "Electricity Bill",
                counterPartyName = "DESCO Prepaid Token",
                counterPartyNumber = "Meter #42008819",
                amount = 1450.00,
                fee = 5.0,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 52,
                reference = "October Bill",
                status = TransactionStatus.SUCCESS,
                balanceAfter = 24600.50
            ),
            Transaction(
                id = "TRX43H17DF",
                type = TransactionType.ADD_MONEY,
                title = "Add Money from Bank",
                counterPartyName = "BRAC Bank Ltd (A/C ***4821)",
                counterPartyNumber = "BRAC-4821",
                amount = 10000.00,
                fee = 0.0,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 75,
                reference = "Salary deposit",
                status = TransactionStatus.SUCCESS,
                balanceAfter = 26055.50
            ),
            Transaction(
                id = "TRX32G06SA",
                type = TransactionType.CASH_OUT,
                title = "Cash Out (Uddokta)",
                counterPartyName = "Maa Telecom Agent",
                counterPartyNumber = "01755443322",
                amount = 2500.00,
                fee = 31.25, // 1.25% fee
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 98,
                reference = "Emergency cash",
                status = TransactionStatus.SUCCESS,
                balanceAfter = 16055.50
            )
        )
    )
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _contacts = MutableStateFlow<List<ContactItem>>(
        listOf(
            ContactItem("1", "Abrar Fahim", "01812987654", isFavorite = true, avatarColorHex = 0xFFE52E2D),
            ContactItem("2", "Tanvir Ahmed", "01688776655", isFavorite = true, avatarColorHex = 0xFFF04D23),
            ContactItem("3", "Nusrat Jahan", "01722334455", isFavorite = true, avatarColorHex = 0xFF8E24AA),
            ContactItem("4", "Mahmudul Hasan", "01911223344", isFavorite = false, avatarColorHex = 0xFF1976D2),
            ContactItem("5", "Rafiqul Islam", "01744556677", isFavorite = false, avatarColorHex = 0xFF388E3C),
            ContactItem("6", "Sadia Karim", "01555667788", isFavorite = false, avatarColorHex = 0xFFE64A19),
            ContactItem("7", "Gulshan Cash Agent", "01755443322", isFavorite = false, avatarColorHex = 0xFF00796B)
        )
    )
    val contacts: StateFlow<List<ContactItem>> = _contacts.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "1",
                titleBn = "ক্যাশব্যাক অফার!",
                titleEn = "Cashback Offer!",
                messageBn = "যেকোনো মোবাইল রিচার্জে পাচ্ছেন ১০% ইনস্ট্যান্ট ক্যাশব্যাক!",
                messageEn = "Enjoy 10% instant cashback on any mobile recharge today!",
                timeAgo = "10m ago"
            ),
            NotificationItem(
                id = "2",
                titleBn = "টাকা স্থানান্তর সফল",
                titleEn = "Money Transfer Successful",
                messageBn = "তানভীর আহমেদকে ৳ ১,২০০ সফলভাবে পাঠানো হয়েছে।",
                messageEn = "Successfully sent ৳ 1,200 to Tanvir Ahmed.",
                timeAgo = "2h ago"
            ),
            NotificationItem(
                id = "3",
                titleBn = "সিকিউরিটি আপডেট",
                titleEn = "Security Notice",
                messageBn = "আপনার প্রোকাহ পিন কারও সাথে শেয়ার করবেন না।",
                messageEn = "Never share your Prokah PIN or OTP with anyone.",
                timeAgo = "1d ago"
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Correct demo PIN
    var storedPin = "1234"

    fun login(pin: String): Boolean {
        if (pin == storedPin || pin == "0000") {
            _isLoggedIn.value = true
            return true
        }
        return false
    }

    fun quickBiometricLogin() {
        _isLoggedIn.value = true
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.BANGLA) AppLanguage.ENGLISH else AppLanguage.BANGLA
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun sendMoney(recipientName: String, recipientPhone: String, amount: Double, reference: String): Transaction {
        val currentBalance = _userProfile.value.balance
        val fee = 0.0 // Free peer-to-peer in demo
        val newBalance = currentBalance - amount - fee
        _userProfile.value = _userProfile.value.copy(
            balance = newBalance,
            dailyLimitRemaining = (_userProfile.value.dailyLimitRemaining - amount).coerceAtLeast(0.0)
        )

        val trx = Transaction(
            id = "TRX" + UUID.randomUUID().toString().take(7).uppercase(),
            type = TransactionType.SEND_MONEY,
            title = "Send Money",
            counterPartyName = recipientName.ifBlank { "Prokah User" },
            counterPartyNumber = recipientPhone,
            amount = amount,
            fee = fee,
            reference = reference,
            balanceAfter = newBalance
        )
        _transactions.value = listOf(trx) + _transactions.value
        return trx
    }

    fun cashOut(agentNumber: String, agentName: String, amount: Double): Transaction {
        val currentBalance = _userProfile.value.balance
        val fee = (amount * 0.0125) // 1.25% fee (৳12.5 per thousand)
        val newBalance = currentBalance - amount - fee
        _userProfile.value = _userProfile.value.copy(
            balance = newBalance,
            dailyLimitRemaining = (_userProfile.value.dailyLimitRemaining - amount).coerceAtLeast(0.0)
        )

        val trx = Transaction(
            id = "TRX" + UUID.randomUUID().toString().take(7).uppercase(),
            type = TransactionType.CASH_OUT,
            title = "Cash Out",
            counterPartyName = agentName.ifBlank { "Cash Out Agent" },
            counterPartyNumber = agentNumber,
            amount = amount,
            fee = fee,
            reference = "Cash withdrawal",
            balanceAfter = newBalance
        )
        _transactions.value = listOf(trx) + _transactions.value
        return trx
    }

    fun mobileRecharge(phone: String, operator: String, amount: Double): Transaction {
        val currentBalance = _userProfile.value.balance
        val newBalance = currentBalance - amount
        _userProfile.value = _userProfile.value.copy(balance = newBalance)

        val trx = Transaction(
            id = "TRX" + UUID.randomUUID().toString().take(7).uppercase(),
            type = TransactionType.MOBILE_RECHARGE,
            title = "Mobile Recharge",
            counterPartyName = "$operator Recharge",
            counterPartyNumber = phone,
            amount = amount,
            fee = 0.0,
            reference = "Airtime top-up",
            balanceAfter = newBalance
        )
        _transactions.value = listOf(trx) + _transactions.value
        return trx
    }

    fun addMoney(sourceName: String, amount: Double): Transaction {
        val currentBalance = _userProfile.value.balance
        val newBalance = currentBalance + amount
        _userProfile.value = _userProfile.value.copy(balance = newBalance)

        val trx = Transaction(
            id = "TRX" + UUID.randomUUID().toString().take(7).uppercase(),
            type = TransactionType.ADD_MONEY,
            title = "Add Money",
            counterPartyName = sourceName,
            counterPartyNumber = "Direct Top-up",
            amount = amount,
            fee = 0.0,
            reference = "Card / Bank Inflow",
            balanceAfter = newBalance
        )
        _transactions.value = listOf(trx) + _transactions.value
        return trx
    }

    fun payBill(billerName: String, billerId: String, amount: Double, fee: Double = 5.0): Transaction {
        val currentBalance = _userProfile.value.balance
        val newBalance = currentBalance - amount - fee
        _userProfile.value = _userProfile.value.copy(balance = newBalance)

        val trx = Transaction(
            id = "TRX" + UUID.randomUUID().toString().take(7).uppercase(),
            type = TransactionType.BILL_PAY,
            title = "Bill Payment",
            counterPartyName = billerName,
            counterPartyNumber = billerId,
            amount = amount,
            fee = fee,
            reference = "Utility Bill",
            balanceAfter = newBalance
        )
        _transactions.value = listOf(trx) + _transactions.value
        return trx
    }

    fun payMerchant(merchantName: String, merchantNumber: String, amount: Double, ref: String): Transaction {
        val currentBalance = _userProfile.value.balance
        val newBalance = currentBalance - amount
        _userProfile.value = _userProfile.value.copy(balance = newBalance)

        val trx = Transaction(
            id = "TRX" + UUID.randomUUID().toString().take(7).uppercase(),
            type = TransactionType.MERCHANT_PAY,
            title = "Merchant Payment",
            counterPartyName = merchantName,
            counterPartyNumber = merchantNumber,
            amount = amount,
            fee = 0.0,
            reference = ref.ifBlank { "Counter Pay" },
            balanceAfter = newBalance
        )
        _transactions.value = listOf(trx) + _transactions.value
        return trx
    }

    fun addContact(name: String, phone: String) {
        val newContact = ContactItem(
            id = UUID.randomUUID().toString(),
            name = name,
            phoneNumber = phone,
            isFavorite = true,
            avatarColorHex = 0xFFE52E2D
        )
        _contacts.value = listOf(newContact) + _contacts.value
    }
}
