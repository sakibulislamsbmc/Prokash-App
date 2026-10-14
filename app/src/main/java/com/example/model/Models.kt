package com.example.model

enum class TransactionType(
    val titleBn: String,
    val titleEn: String,
    val isDebit: Boolean
) {
    SEND_MONEY("সেন্ড মানি", "Send Money", true),
    CASH_OUT("ক্যাশ আউট", "Cash Out", true),
    MOBILE_RECHARGE("মোবাইল রিচার্জ", "Mobile Recharge", true),
    ADD_MONEY("অ্যাড মানি", "Add Money", false),
    TRANSFER_MONEY("ট্রান্সফার মানি", "Transfer Money", true),
    BILL_PAY("বিল পে", "Bill Pay", true),
    MERCHANT_PAY("মার্চেন্ট পে", "Merchant Pay", true),
    EMI_PAYMENT("ইএমআই পেমেন্ট", "EMI Payment", true),
    RECEIVED_MONEY("মানি রিসিভড", "Received Money", false)
}

enum class TransactionStatus {
    SUCCESS,
    PENDING,
    FAILED
}

data class Transaction(
    val id: String,
    val type: TransactionType,
    val title: String,
    val counterPartyName: String,
    val counterPartyNumber: String,
    val amount: Double,
    val fee: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    val reference: String = "",
    val status: TransactionStatus = TransactionStatus.SUCCESS,
    val balanceAfter: Double
)

data class ContactItem(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val isFavorite: Boolean = false,
    val avatarColorHex: Long = 0xFFE52E2D
)

data class NotificationItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val messageBn: String,
    val messageEn: String,
    val timeAgo: String,
    val isRead: Boolean = false
)

data class UserProfile(
    val name: String = "Sabbir",
    val phoneNumber: String = "+880 1712 345678",
    val balance: Double = 24850.50,
    val accountNumber: String = "01712-345678",
    val kycVerified: Boolean = true,
    val dailyLimitRemaining: Double = 45149.50,
    val dailyLimitTotal: Double = 70000.00,
    val monthlyLimitRemaining: Double = 185000.00,
    val monthlyLimitTotal: Double = 250000.00
)

enum class AppLanguage {
    BANGLA,
    ENGLISH
}
