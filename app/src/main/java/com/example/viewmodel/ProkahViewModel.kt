package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ProkahRepository
import com.example.model.AppLanguage
import com.example.model.ContactItem
import com.example.model.NotificationItem
import com.example.model.Transaction
import com.example.model.TransactionType
import com.example.model.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TransactionFilter {
    ALL,
    RECEIVED,
    SENT
}

enum class ActiveModal {
    NONE,
    SEND_MONEY,
    CASH_OUT,
    MOBILE_RECHARGE,
    ADD_MONEY,
    TRANSFER_MONEY,
    BILL_PAY,
    MERCHANT_PAY,
    QR_SCANNER,
    ZAKAT_CALCULATOR,
    LIMITS_AND_CHARGES,
    NOTIFICATIONS,
    CONTACT_US,
    TRANSACTION_RECEIPT
}

class ProkahViewModel(
    private val repository: ProkahRepository = ProkahRepository()
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val language: StateFlow<AppLanguage> = repository.language
    val contacts: StateFlow<List<ContactItem>> = repository.contacts
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications

    // Balance Tap-to-Reveal with auto-hide timer
    private val _isBalanceRevealed = MutableStateFlow(false)
    val isBalanceRevealed: StateFlow<Boolean> = _isBalanceRevealed.asStateFlow()

    private val _isRevealingAnimation = MutableStateFlow(false)
    val isRevealingAnimation: StateFlow<Boolean> = _isRevealingAnimation.asStateFlow()

    private var balanceAutoHideJob: Job? = null

    fun toggleBalanceReveal() {
        if (_isBalanceRevealed.value) {
            _isBalanceRevealed.value = false
            balanceAutoHideJob?.cancel()
        } else {
            _isRevealingAnimation.value = true
            _isBalanceRevealed.value = true
            balanceAutoHideJob?.cancel()
            balanceAutoHideJob = viewModelScope.launch {
                delay(300)
                _isRevealingAnimation.value = false
                delay(4000) // Show for 4 seconds then re-conceal
                _isBalanceRevealed.value = false
            }
        }
    }

    // Modal navigation
    private val _activeModal = MutableStateFlow(ActiveModal.NONE)
    val activeModal: StateFlow<ActiveModal> = _activeModal.asStateFlow()

    private val _lastCompletedTransaction = MutableStateFlow<Transaction?>(null)
    val lastCompletedTransaction: StateFlow<Transaction?> = _lastCompletedTransaction.asStateFlow()

    // Transaction search and filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(TransactionFilter.ALL)
    val selectedFilter: StateFlow<TransactionFilter> = _selectedFilter.asStateFlow()

    val filteredTransactions: StateFlow<List<Transaction>> = combine(
        repository.transactions,
        _searchQuery,
        _selectedFilter
    ) { list, query, filter ->
        list.filter { tx ->
            val matchesFilter = when (filter) {
                TransactionFilter.ALL -> true
                TransactionFilter.RECEIVED -> !tx.type.isDebit
                TransactionFilter.SENT -> tx.type.isDebit
            }
            val matchesQuery = query.isBlank() ||
                tx.counterPartyName.contains(query, ignoreCase = true) ||
                tx.counterPartyNumber.contains(query, ignoreCase = true) ||
                tx.id.contains(query, ignoreCase = true) ||
                tx.type.titleBn.contains(query, ignoreCase = true) ||
                tx.type.titleEn.contains(query, ignoreCase = true)
            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedFilter(filter: TransactionFilter) {
        _selectedFilter.value = filter
    }

    fun openModal(modal: ActiveModal) {
        _activeModal.value = modal
    }

    fun closeModal() {
        _activeModal.value = ActiveModal.NONE
    }

    fun showReceipt(transaction: Transaction) {
        _lastCompletedTransaction.value = transaction
        _activeModal.value = ActiveModal.TRANSACTION_RECEIPT
    }

    // Auth actions
    fun login(pin: String): Boolean {
        return repository.login(pin)
    }

    fun biometricLogin() {
        repository.quickBiometricLogin()
    }

    fun logout() {
        repository.logout()
    }

    fun toggleLanguage() {
        repository.toggleLanguage()
    }

    // Financial Actions
    fun executeSendMoney(name: String, phone: String, amount: Double, reference: String) {
        val tx = repository.sendMoney(name, phone, amount, reference)
        showReceipt(tx)
    }

    fun executeCashOut(agentPhone: String, agentName: String, amount: Double) {
        val tx = repository.cashOut(agentPhone, agentName, amount)
        showReceipt(tx)
    }

    fun executeMobileRecharge(phone: String, operator: String, amount: Double) {
        val tx = repository.mobileRecharge(phone, operator, amount)
        showReceipt(tx)
    }

    fun executeAddMoney(source: String, amount: Double) {
        val tx = repository.addMoney(source, amount)
        showReceipt(tx)
    }

    fun executeBillPay(billerName: String, billerId: String, amount: Double) {
        val tx = repository.payBill(billerName, billerId, amount)
        showReceipt(tx)
    }

    fun executeMerchantPay(name: String, number: String, amount: Double, ref: String) {
        val tx = repository.payMerchant(name, number, amount, ref)
        showReceipt(tx)
    }

    fun addContact(name: String, phone: String) {
        repository.addContact(name, phone)
    }
}
