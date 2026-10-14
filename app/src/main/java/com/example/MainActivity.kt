package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.ContactItem
import com.example.ui.components.BottomNavBarWithQr
import com.example.ui.components.BottomTab
import com.example.ui.modals.FundTransferModalManager
import com.example.ui.screens.ContactsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MyProkahScreen
import com.example.ui.screens.TransactionHistoryScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ActiveModal
import com.example.viewmodel.ProkahViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ProkahApp()
            }
        }
    }
}

@Composable
fun ProkahApp(
    viewModel: ProkahViewModel = viewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val language by viewModel.language.collectAsState()
    val isBalanceRevealed by viewModel.isBalanceRevealed.collectAsState()
    val transactions by viewModel.filteredTransactions.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val activeModal by viewModel.activeModal.collectAsState()
    val lastCompletedTransaction by viewModel.lastCompletedTransaction.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }

    // Handle back button smoothly
    BackHandler(enabled = activeModal != ActiveModal.NONE || selectedTab != BottomTab.HOME) {
        if (activeModal != ActiveModal.NONE) {
            viewModel.closeModal()
        } else if (selectedTab != BottomTab.HOME) {
            selectedTab = BottomTab.HOME
        }
    }

    if (!isLoggedIn) {
        LoginScreen(
            userProfile = userProfile,
            language = language,
            onLogin = { pin -> viewModel.login(pin) },
            onBiometricLogin = { viewModel.biometricLogin() },
            onToggleLanguage = { viewModel.toggleLanguage() }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavBarWithQr(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    onQrClick = { viewModel.openModal(ActiveModal.QR_SCANNER) },
                    language = language
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    BottomTab.HOME -> {
                        HomeScreen(
                            userProfile = userProfile,
                            isBalanceRevealed = isBalanceRevealed,
                            language = language,
                            onTapBalance = { viewModel.toggleBalanceReveal() },
                            onOpenModal = { modal -> viewModel.openModal(modal) },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            unreadNotificationCount = 99,
                            contentPadding = paddingValues
                        )
                    }
                    BottomTab.TRANSACTIONS -> {
                        TransactionHistoryScreen(
                            transactions = transactions,
                            searchQuery = searchQuery,
                            selectedFilter = selectedFilter,
                            language = language,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                            onFilterSelect = { viewModel.setSelectedFilter(it) },
                            onTransactionClick = { tx -> viewModel.showReceipt(tx) },
                            contentPadding = paddingValues
                        )
                    }
                    BottomTab.CONTACTS -> {
                        ContactsScreen(
                            contacts = contacts,
                            language = language,
                            onContactSelectForTransfer = { contact ->
                                viewModel.openModal(ActiveModal.SEND_MONEY)
                            },
                            onAddNewContact = { name, phone ->
                                viewModel.addContact(name, phone)
                            },
                            contentPadding = paddingValues
                        )
                    }
                    BottomTab.MY_PROKAH -> {
                        MyProkahScreen(
                            userProfile = userProfile,
                            language = language,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onOpenModal = { modal -> viewModel.openModal(modal) },
                            onLogout = { viewModel.logout() },
                            contentPadding = paddingValues
                        )
                    }
                }
            }

            // Interactive Modal Sheets (Send Money, Cash Out, Recharge, Add Money, Bill Pay, QR, Receipt, etc.)
            FundTransferModalManager(
                activeModal = activeModal,
                userProfile = userProfile,
                contacts = contacts,
                notifications = notifications,
                lastTransaction = lastCompletedTransaction,
                language = language,
                onDismiss = { viewModel.closeModal() },
                onSendMoney = { name, phone, amount, ref ->
                    viewModel.executeSendMoney(name, phone, amount, ref)
                },
                onCashOut = { phone, name, amount ->
                    viewModel.executeCashOut(phone, name, amount)
                },
                onMobileRecharge = { phone, op, amount ->
                    viewModel.executeMobileRecharge(phone, op, amount)
                },
                onAddMoney = { src, amount ->
                    viewModel.executeAddMoney(src, amount)
                },
                onBillPay = { biller, meter, amount ->
                    viewModel.executeBillPay(biller, meter, amount)
                },
                onMerchantPay = { name, num, amount, ref ->
                    viewModel.executeMerchantPay(name, num, amount, ref)
                }
            )
        }
    }
}
