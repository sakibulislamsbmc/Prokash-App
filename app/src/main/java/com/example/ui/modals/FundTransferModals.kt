package com.example.ui.modals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ContactItem
import com.example.model.NotificationItem
import com.example.model.Transaction
import com.example.model.UserProfile
import com.example.ui.components.HoldToConfirmButton
import com.example.ui.theme.ProkahCardBorder
import com.example.ui.theme.ProkahError
import com.example.ui.theme.ProkahGold
import com.example.ui.theme.ProkahOrange
import com.example.ui.theme.ProkahRed
import com.example.ui.theme.ProkahSuccess
import com.example.ui.theme.ProkahTextPrimary
import com.example.ui.theme.ProkahTextSecondary
import com.example.viewmodel.ActiveModal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundTransferModalManager(
    activeModal: ActiveModal,
    userProfile: UserProfile,
    contacts: List<ContactItem>,
    notifications: List<NotificationItem>,
    lastTransaction: Transaction?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSendMoney: (String, String, Double, String) -> Unit,
    onCashOut: (String, String, Double) -> Unit,
    onMobileRecharge: (String, String, Double) -> Unit,
    onAddMoney: (String, Double) -> Unit,
    onBillPay: (String, String, Double) -> Unit,
    onMerchantPay: (String, String, Double, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (activeModal != ActiveModal.NONE) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            when (activeModal) {
                ActiveModal.SEND_MONEY -> SendMoneySheet(contacts, userProfile, language, onDismiss, onSendMoney)
                ActiveModal.CASH_OUT -> CashOutSheet(userProfile, language, onDismiss, onCashOut)
                ActiveModal.MOBILE_RECHARGE -> MobileRechargeSheet(userProfile, language, onDismiss, onMobileRecharge)
                ActiveModal.ADD_MONEY -> AddMoneySheet(language, onDismiss, onAddMoney)
                ActiveModal.TRANSFER_MONEY -> TransferMoneySheet(userProfile, language, onDismiss, onSendMoney)
                ActiveModal.BILL_PAY -> BillPaySheet(userProfile, language, onDismiss, onBillPay)
                ActiveModal.MERCHANT_PAY -> MerchantPaySheet(userProfile, language, onDismiss, onMerchantPay)
                ActiveModal.QR_SCANNER -> QrScannerSheet(language, onDismiss, onMerchantPay, onCashOut)
                ActiveModal.TRANSACTION_RECEIPT -> TransactionReceiptSheet(lastTransaction, language, onDismiss)
                ActiveModal.ZAKAT_CALCULATOR -> ZakatCalculatorSheet(language, onDismiss)
                ActiveModal.LIMITS_AND_CHARGES -> LimitsChargesSheet(language, onDismiss)
                ActiveModal.NOTIFICATIONS -> NotificationsSheet(notifications, language, onDismiss)
                ActiveModal.CONTACT_US -> ContactUsSheet(language, onDismiss)
                ActiveModal.NONE -> {}
            }
        }
    }
}

// 1. Send Money Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneySheet(
    contacts: List<ContactItem>,
    userProfile: UserProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSend: (String, String, Double, String) -> Unit
) {
    var recipientPhone by remember { mutableStateOf("") }
    var recipientName by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var referenceText by remember { mutableStateOf("") }
    var pinText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    val amount = amountText.toDoubleOrNull() ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState())
            .testTag("send_money_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "সেন্ড মানি (টাকা পাঠান)" else "Send Money",
            subtitle = if (language == AppLanguage.BANGLA) "যেকোনো প্রোকাহ একাউন্টে ফ্রি ট্রান্সফার" else "Free transfer to any Prokah account",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Contacts Selector
        Text(
            text = if (language == AppLanguage.BANGLA) "সাম্প্রতিক প্রাপক" else "Recent Contacts",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ProkahTextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            contacts.take(4).forEach { contact ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            recipientPhone = contact.phoneNumber
                            recipientName = contact.name
                        },
                    color = if (recipientPhone == contact.phoneNumber) Color(0xFFFFECEB) else Color(0xFFF7F5F3),
                    border = BorderStroke(1.dp, if (recipientPhone == contact.phoneNumber) ProkahRed else ProkahCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(contact.name.take(8), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ProkahTextPrimary)
                        Text(contact.phoneNumber.takeLast(4), fontSize = 10.sp, color = ProkahTextSecondary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = recipientPhone,
            onValueChange = { recipientPhone = it; errorText = null },
            label = { Text(if (language == AppLanguage.BANGLA) "প্রাপকের নম্বর" else "Recipient Number") },
            placeholder = { Text("017XXXXXXXX") },
            modifier = Modifier.fillMaxWidth().testTag("send_money_recipient_input"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it; errorText = null },
            label = { Text(if (language == AppLanguage.BANGLA) "টাকার পরিমাণ (৳)" else "Amount (৳)") },
            placeholder = { Text("৳ 500") },
            modifier = Modifier.fillMaxWidth().testTag("send_money_amount_input"),
            singleLine = true
        )

        // Quick Amount Chips
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("500", "1000", "2000", "5000").forEach { chip ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { amountText = chip },
                    color = Color(0xFFF7F5F3),
                    border = BorderStroke(1.dp, ProkahCardBorder)
                ) {
                    Text(
                        text = "+৳$chip",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ProkahRed,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = referenceText,
            onValueChange = { referenceText = it },
            label = { Text(if (language == AppLanguage.BANGLA) "রেফারেন্স (ঐচ্ছিক)" else "Reference (Optional)") },
            placeholder = { Text("Dinner, Rent, etc.") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = pinText,
            onValueChange = { pinText = it; errorText = null },
            label = { Text(if (language == AppLanguage.BANGLA) "প্রোকাহ পিন (৪ ডিজিট)" else "Prokah PIN (4 digits)") },
            placeholder = { Text("1234") },
            modifier = Modifier.fillMaxWidth().testTag("send_money_pin_input"),
            singleLine = true
        )

        if (errorText != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(errorText!!, color = ProkahError, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Hold to confirm button
        HoldToConfirmButton(
            language = language,
            labelBn = "সেন্ড মানি করতে চেপে ধরে রাখুন",
            labelEn = "Hold to confirm Send Money",
            onConfirmed = {
                if (recipientPhone.isBlank()) {
                    errorText = if (language == AppLanguage.BANGLA) "প্রাপকের নম্বর দিন" else "Enter recipient number"
                } else if (amount <= 0) {
                    errorText = if (language == AppLanguage.BANGLA) "সঠিক পরিমাণ লিখুন" else "Enter valid amount"
                } else if (amount > userProfile.balance) {
                    errorText = if (language == AppLanguage.BANGLA) "অপর্যাপ্ত ব্যালেন্স!" else "Insufficient balance!"
                } else if (pinText != "1234" && pinText != "0000" && pinText.isNotEmpty() && pinText.length != 4) {
                    errorText = if (language == AppLanguage.BANGLA) "সঠিক ৪ ডিজিট পিন দিন" else "Enter 4-digit PIN"
                } else {
                    onSend(recipientName.ifBlank { "017${recipientPhone.takeLast(7)}" }, recipientPhone, amount, referenceText)
                }
            }
        )
    }
}

// 2. Cash Out Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashOutSheet(
    userProfile: UserProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onCashOut: (String, String, Double) -> Unit
) {
    var agentPhone by remember { mutableStateOf("01755443322") }
    var agentName by remember { mutableStateOf("Maa Telecom Agent") }
    var amountText by remember { mutableStateOf("") }
    var pinText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    val amount = amountText.toDoubleOrNull() ?: 0.0
    val fee = amount * 0.0125 // 1.25% fee

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState())
            .testTag("cash_out_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "ক্যাশ আউট (উদ্যোক্তা)" else "Cash Out (Agent)",
            subtitle = if (language == AppLanguage.BANGLA) "দেশজুড়ে মাত্র ১.২৫% রেটে ক্যাশ আউট করুন" else "Lowest 1.25% fee nationwide",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = agentPhone,
            onValueChange = { agentPhone = it },
            label = { Text(if (language == AppLanguage.BANGLA) "এজেন্ট / উদ্যোক্তা নম্বর" else "Agent Number") },
            modifier = Modifier.fillMaxWidth().testTag("cash_out_agent_input"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text(if (language == AppLanguage.BANGLA) "টাকার পরিমাণ (৳)" else "Amount (৳)") },
            placeholder = { Text("৳ 1,000") },
            modifier = Modifier.fillMaxWidth().testTag("cash_out_amount_input"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Fee Summary Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.BANGLA) "ক্যাশ আউট ফি (১.২৫%):" else "Cash out fee (1.25%):",
                    fontSize = 12.sp,
                    color = Color(0xFFE65100)
                )
                Text(
                    text = "৳ ${String.format(Locale.US, "%.2f", fee)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = pinText,
            onValueChange = { pinText = it },
            label = { Text(if (language == AppLanguage.BANGLA) "প্রোকাহ পিন (৪ ডিজিট)" else "Prokah PIN") },
            placeholder = { Text("1234") },
            modifier = Modifier.fillMaxWidth().testTag("cash_out_pin_input"),
            singleLine = true
        )

        if (errorText != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(errorText!!, color = ProkahError, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        HoldToConfirmButton(
            language = language,
            labelBn = "ক্যাশ আউট নিশ্চিত করতে চাপুন",
            labelEn = "Hold to confirm Cash Out",
            onConfirmed = {
                if (amount <= 0) {
                    errorText = if (language == AppLanguage.BANGLA) "সঠিক পরিমাণ দিন" else "Enter valid amount"
                } else if ((amount + fee) > userProfile.balance) {
                    errorText = if (language == AppLanguage.BANGLA) "অপর্যাপ্ত ব্যালেন্স!" else "Insufficient balance!"
                } else {
                    onCashOut(agentPhone, agentName, amount)
                }
            }
        )
    }
}

// 3. Mobile Recharge Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileRechargeSheet(
    userProfile: UserProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onRecharge: (String, String, Double) -> Unit
) {
    var phone by remember { mutableStateOf(userProfile.phoneNumber.replace("+880 ", "0")) }
    var selectedOperator by remember { mutableStateOf("Grameenphone") }
    var amountText by remember { mutableStateOf("100") }
    var isPrepaid by remember { mutableStateOf(true) }

    val operators = listOf("Grameenphone", "Robi", "Banglalink", "Teletalk", "Airtel")
    val quickAmounts = listOf("20", "50", "100", "200", "500")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState())
            .testTag("mobile_recharge_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "মোবাইল রিচার্জ" else "Mobile Recharge",
            subtitle = if (language == AppLanguage.BANGLA) "যেকোনো অপারেটরে তাত্ক্ষণিক রিচার্জ" else "Instant airtime for any operator",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(if (language == AppLanguage.BANGLA) "মোবাইল নম্বর" else "Mobile Number") },
            modifier = Modifier.fillMaxWidth().testTag("recharge_phone_input"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (language == AppLanguage.BANGLA) "অপারেটর নির্বাচন করুন" else "Select Operator",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ProkahTextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            operators.forEach { op ->
                val isSelected = op == selectedOperator
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { selectedOperator = op },
                    color = if (isSelected) ProkahRed else Color(0xFFF7F5F3),
                    border = BorderStroke(1.dp, if (isSelected) ProkahRed else ProkahCardBorder)
                ) {
                    Text(
                        text = op.take(4),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else ProkahTextPrimary,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Prepaid / Postpaid switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { isPrepaid = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPrepaid) Color(0xFFFFECEB) else Color(0xFFF7F5F3),
                    contentColor = if (isPrepaid) ProkahRed else ProkahTextSecondary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (language == AppLanguage.BANGLA) "প্রিপেইড" else "Prepaid", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { isPrepaid = false },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isPrepaid) Color(0xFFFFECEB) else Color(0xFFF7F5F3),
                    contentColor = if (!isPrepaid) ProkahRed else ProkahTextSecondary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (language == AppLanguage.BANGLA) "পোস্টপেইড" else "Postpaid", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text(if (language == AppLanguage.BANGLA) "রিচার্জের পরিমাণ (৳)" else "Recharge Amount (৳)") },
            modifier = Modifier.fillMaxWidth().testTag("recharge_amount_input"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            quickAmounts.forEach { q ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { amountText = q },
                    color = Color(0xFFF7F5F3),
                    border = BorderStroke(1.dp, ProkahCardBorder)
                ) {
                    Text(
                        text = "৳$q",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProkahRed,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        HoldToConfirmButton(
            language = language,
            labelBn = "রিচার্জ নিশ্চিত করতে চাপুন",
            labelEn = "Hold to confirm Recharge",
            onConfirmed = {
                val amt = amountText.toDoubleOrNull() ?: 50.0
                onRecharge(phone, selectedOperator, amt)
            }
        )
    }
}

// 4. Add Money Sheet
@Composable
fun AddMoneySheet(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onAdd: (String, Double) -> Unit
) {
    var selectedSource by remember { mutableStateOf("BRAC Bank A/C") }
    var amountText by remember { mutableStateOf("5000") }

    val sources = listOf("BRAC Bank A/C", "Dutch-Bangla Bank", "City Bank", "Visa / Mastercard")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .testTag("add_money_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "অ্যাড মানি" else "Add Money",
            subtitle = if (language == AppLanguage.BANGLA) "ব্যাংক বা কার্ড থেকে তাৎক্ষণিক ব্যালেন্স যোগ করুন" else "Deposit from Bank or Card instantly",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (language == AppLanguage.BANGLA) "উৎস নির্বাচন করুন" else "Select Source",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ProkahTextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        sources.forEach { src ->
            val isSelected = src == selectedSource
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { selectedSource = src },
                color = if (isSelected) Color(0xFFFFECEB) else Color.White,
                border = BorderStroke(1.dp, if (isSelected) ProkahRed else ProkahCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) ProkahRed else Color(0xFFCFD8DC))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(src, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ProkahTextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text(if (language == AppLanguage.BANGLA) "টাকার পরিমাণ (৳)" else "Amount (৳)") },
            modifier = Modifier.fillMaxWidth().testTag("add_money_amount_input"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val amt = amountText.toDoubleOrNull() ?: 1000.0
                onAdd(selectedSource, amt)
            },
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("add_money_submit_btn"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ProkahRed)
        ) {
            Text(if (language == AppLanguage.BANGLA) "টাকা যোগ করুন" else "Add Money Now", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 5. Transfer Money (Bank) Sheet
@Composable
fun TransferMoneySheet(
    userProfile: UserProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onTransfer: (String, String, Double, String) -> Unit
) {
    var bankName by remember { mutableStateOf("Eastern Bank PLC") }
    var accNumber by remember { mutableStateOf("108-100-29381") }
    var accHolder by remember { mutableStateOf("Sabbir Ahmed") }
    var amountText by remember { mutableStateOf("2500") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState())
            .testTag("transfer_money_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "ট্রান্সফার মানি (ব্যাংক)" else "Transfer to Bank",
            subtitle = if (language == AppLanguage.BANGLA) "যেকোনো ব্যাংক একাউন্টে তাৎক্ষণিক স্থানান্তর" else "Instant transfer to any bank account",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = bankName,
            onValueChange = { bankName = it },
            label = { Text(if (language == AppLanguage.BANGLA) "ব্যাংকের নাম" else "Bank Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = accNumber,
            onValueChange = { accNumber = it },
            label = { Text(if (language == AppLanguage.BANGLA) "হিসাব নম্বর (Account No)" else "Account Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = accHolder,
            onValueChange = { accHolder = it },
            label = { Text(if (language == AppLanguage.BANGLA) "হিসাবধারীর নাম" else "Account Holder Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text(if (language == AppLanguage.BANGLA) "টাকার পরিমাণ" else "Amount") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        HoldToConfirmButton(
            language = language,
            labelBn = "ব্যাংক ট্রান্সফার নিশ্চিত করুন",
            labelEn = "Hold to confirm Transfer",
            onConfirmed = {
                val amt = amountText.toDoubleOrNull() ?: 1000.0
                onTransfer("$bankName ($accHolder)", accNumber, amt, "Bank Transfer")
            }
        )
    }
}

// 6. Bill Pay Sheet
@Composable
fun BillPaySheet(
    userProfile: UserProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onBillPay: (String, String, Double) -> Unit
) {
    val billers = listOf("DESCO Electricity", "DPDC Electricity", "Dhaka WASA (Water)", "Titas Gas", "Link3 Internet")
    var selectedBiller by remember { mutableStateOf(billers.first()) }
    var meterNumber by remember { mutableStateOf("10492817") }
    var amountText by remember { mutableStateOf("1450") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState())
            .testTag("bill_pay_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "বিল পে (ইউটিলিটি)" else "Bill Pay",
            subtitle = if (language == AppLanguage.BANGLA) "বিদ্যুৎ, গ্যাস, পানি ও ইন্টারনেট বিল" else "Electricity, Gas, Water & Internet",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (language == AppLanguage.BANGLA) "বিল প্রদানকারী প্রতিষ্ঠান" else "Select Biller",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ProkahTextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))

        billers.take(3).forEach { b ->
            val isSelected = b == selectedBiller
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { selectedBiller = b },
                color = if (isSelected) Color(0xFFFFECEB) else Color(0xFFF7F5F3),
                border = BorderStroke(1.dp, if (isSelected) ProkahRed else ProkahCardBorder)
            ) {
                Text(
                    text = b,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) ProkahRed else ProkahTextPrimary,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = meterNumber,
            onValueChange = { meterNumber = it },
            label = { Text(if (language == AppLanguage.BANGLA) "মিটার / গ্রাহক নম্বর" else "Customer / Meter No") },
            modifier = Modifier.fillMaxWidth().testTag("bill_meter_input"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text(if (language == AppLanguage.BANGLA) "বিলের পরিমাণ (৳)" else "Bill Amount (৳)") },
            modifier = Modifier.fillMaxWidth().testTag("bill_amount_input"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        HoldToConfirmButton(
            language = language,
            labelBn = "বিল পরিশোধ করতে চাপুন",
            labelEn = "Hold to pay bill",
            onConfirmed = {
                val amt = amountText.toDoubleOrNull() ?: 500.0
                onBillPay(selectedBiller, meterNumber, amt)
            }
        )
    }
}

// 7. Merchant Pay Sheet
@Composable
fun MerchantPaySheet(
    userProfile: UserProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onMerchantPay: (String, String, Double, String) -> Unit
) {
    var merchantName by remember { mutableStateOf("Aarong Outlet") }
    var merchantPhone by remember { mutableStateOf("01999887766") }
    var counterNo by remember { mutableStateOf("Counter 4") }
    var amountText by remember { mutableStateOf("1250") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState())
            .testTag("merchant_pay_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "মার্চেন্ট পে" else "Merchant Pay",
            subtitle = if (language == AppLanguage.BANGLA) "দোকানে কিউআর কোড বা মার্চেন্ট নম্বরে পে করুন" else "Pay at shops via QR or Merchant number",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = merchantName,
            onValueChange = { merchantName = it },
            label = { Text(if (language == AppLanguage.BANGLA) "মার্চেন্ট / দোকানের নাম" else "Merchant Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = merchantPhone,
            onValueChange = { merchantPhone = it },
            label = { Text(if (language == AppLanguage.BANGLA) "মার্চেন্ট নম্বর" else "Merchant Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text(if (language == AppLanguage.BANGLA) "টাকার পরিমাণ" else "Amount") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        HoldToConfirmButton(
            language = language,
            labelBn = "পেমেন্ট নিশ্চিত করতে চাপুন",
            labelEn = "Hold to pay merchant",
            onConfirmed = {
                val amt = amountText.toDoubleOrNull() ?: 500.0
                onMerchantPay(merchantName, merchantPhone, amt, counterNo)
            }
        )
    }
}

// 8. Interactive Simulated QR Scanner Sheet
@Composable
fun QrScannerSheet(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onMerchantPay: (String, String, Double, String) -> Unit,
    onCashOut: (String, String, Double) -> Unit
) {
    var torchEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .testTag("qr_scanner_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "কিউআর স্ক্যানার" else "QR Scanner",
            subtitle = if (language == AppLanguage.BANGLA) "মার্চেন্ট বা এজেন্টের কিউআর কোড স্ক্যান করুন" else "Scan Merchant or Agent QR Code",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Scanner Viewfinder Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            // Reticle Target Box
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (torchEnabled) Color.White.copy(alpha = 0.15f) else Color.Transparent)
                    .padding(10.dp)
            ) {
                // Corner brackets
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Torch Button (Top Right)
            IconButton(
                onClick = { torchEnabled = !torchEnabled },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    Icons.Default.FlashOn,
                    contentDescription = "Torch",
                    tint = if (torchEnabled) ProkahGold else Color.White
                )
            }

            // Scanning indicator line
            Text(
                text = if (language == AppLanguage.BANGLA) "কিউআর কোডটি ফ্রেমের মাঝে রাখুন" else "Align QR code inside the frame",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 14.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (language == AppLanguage.BANGLA) "দ্রুত টেস্ট স্ক্যানার:" else "Quick Test Scans:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ProkahTextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    onMerchantPay("Aarong Lifestyle", "01999887766", 2150.0, "POS-10")
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFECEB), contentColor = ProkahRed),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (language == AppLanguage.BANGLA) "মার্চেন্ট কিউআর" else "Merchant QR", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    onCashOut("01755443322", "Gulshan Agent Point", 1500.0)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF3E0), contentColor = ProkahOrange),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (language == AppLanguage.BANGLA) "এজেন্ট কিউআর" else "Agent QR", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 9. Digital Receipt Sheet
@Composable
fun TransactionReceiptSheet(
    transaction: Transaction?,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val decimalFormat = remember { DecimalFormat("#,##0.00") }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

    if (transaction == null) {
        onDismiss()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .testTag("receipt_sheet"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Green Success Checkmark
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.CheckCircle, null, tint = ProkahSuccess, modifier = Modifier.size(44.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (language == AppLanguage.BANGLA) "লেনদেন সফল হয়েছে!" else "Transaction Successful!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ProkahTextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = dateFormat.format(Date(transaction.timestamp)),
            fontSize = 12.sp,
            color = ProkahTextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Big Amount Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F7F5)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (language == AppLanguage.BANGLA) transaction.type.titleBn else transaction.type.titleEn,
                    fontSize = 13.sp,
                    color = ProkahTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "৳ ${decimalFormat.format(transaction.amount)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = ProkahRed
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Receipt Details Table
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReceiptRow(
                label = if (language == AppLanguage.BANGLA) "প্রাপক / প্রতিষ্ঠান:" else "Recipient:",
                value = "${transaction.counterPartyName} (${transaction.counterPartyNumber})"
            )
            ReceiptRow(
                label = "TrxID:",
                value = transaction.id
            )
            ReceiptRow(
                label = if (language == AppLanguage.BANGLA) "সার্ভিস চার্জ:" else "Fee:",
                value = "৳ ${decimalFormat.format(transaction.fee)}"
            )
            ReceiptRow(
                label = if (language == AppLanguage.BANGLA) "নতুন ব্যালেন্স:" else "New Balance:",
                value = "৳ ${decimalFormat.format(transaction.balanceAfter)}"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("receipt_close_btn"),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ProkahRed)
        ) {
            Text(if (language == AppLanguage.BANGLA) "সম্পন্ন করুন" else "Done", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 10. Zakat Calculator Sheet
@Composable
fun ZakatCalculatorSheet(
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    var goldGrams by remember { mutableStateOf("0") }
    var silverGrams by remember { mutableStateOf("0") }
    var cashAmount by remember { mutableStateOf("250000") }
    var debtsAmount by remember { mutableStateOf("20000") }

    val goldVal = (goldGrams.toDoubleOrNull() ?: 0.0) * 11500.0 // approx ৳11,500/g in BD
    val silverVal = (silverGrams.toDoubleOrNull() ?: 0.0) * 180.0
    val cashVal = cashAmount.toDoubleOrNull() ?: 0.0
    val debtsVal = debtsAmount.toDoubleOrNull() ?: 0.0

    val netWealth = (goldVal + silverVal + cashVal - debtsVal).coerceAtLeast(0.0)
    val zakatPayable = netWealth * 0.025 // 2.5%

    val decimalFormat = remember { DecimalFormat("#,##0.00") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState())
            .testTag("zakat_calculator_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "যাকাত ক্যালকুলেটর" else "Zakat Calculator",
            subtitle = if (language == AppLanguage.BANGLA) "ইসলামী শরিয়ত মোতাবেক নিসাব ও যাকাত হিসাব করুন" else "Calculate your Nisab & 2.5% Zakat",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = cashAmount,
            onValueChange = { cashAmount = it },
            label = { Text(if (language == AppLanguage.BANGLA) "নগদ টাকা ও ব্যাংক ব্যালেন্স (৳)" else "Cash & Bank Balance (৳)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = goldGrams,
            onValueChange = { goldGrams = it },
            label = { Text(if (language == AppLanguage.BANGLA) "স্বর্ণের পরিমাণ (গ্রাম)" else "Gold (Grams)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = debtsAmount,
            onValueChange = { debtsAmount = it },
            label = { Text(if (language == AppLanguage.BANGLA) "পরিশোধযোগ্য ঋণ (৳)" else "Liabilities & Debts (৳)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = if (language == AppLanguage.BANGLA) "আপনার মোট প্রদেয় যাকাত (২.৫%):" else "Your Payable Zakat (2.5%):",
                    fontSize = 13.sp,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "৳ ${decimalFormat.format(zakatPayable)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B5E20)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ProkahRed)
        ) {
            Text(if (language == AppLanguage.BANGLA) "যাকাত ফান্ডে দান করুন" else "Pay Zakat to Fund", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 11. Limits & Charges Sheet
@Composable
fun LimitsChargesSheet(
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState())
            .testTag("limits_charges_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "লিমিট ও চার্জ সমূহ" else "Limits & Charges",
            subtitle = if (language == AppLanguage.BANGLA) "প্রোকাহ একাউন্টের হালনাগাদ ফি ও লেনদেন সীমা" else "Updated fees & daily transaction limits",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        LimitsItemRow("সেন্ড মানি (Send Money)", "৳০.০০ (ফ্রি)", "দৈনিক: ৫০,০০০৳ | মাসিক: ২,০০,০০০৳")
        LimitsItemRow("ক্যাশ আউট (Cash Out)", "১.২৫% (১২.৫০৳/হাজার)", "দৈনিক: ৫০,০০০৳ | মাসিক: ১,৫০,০০০৳")
        LimitsItemRow("মোবাইল রিচার্জ (Recharge)", "৳০.০০ (ফ্রি)", "দৈনিক: ১০,০০০৳ | মাসিক: ৫০,০০০৳")
        LimitsItemRow("ইউটিলিটি বিল পে (Bill Pay)", "৳৫.০০", "দৈনিক: ১,০০,০০০৳ | মাসিক: ৩,০০,০০০৳")
        LimitsItemRow("মার্চেন্ট পে (Merchant)", "৳০.০০ (ফ্রি)", "দৈনিক: ৫০,০০০৳ | মাসিক: ২,০০,০০০৳")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ProkahRed)
        ) {
            Text(if (language == AppLanguage.BANGLA) "ঠিক আছে" else "Close", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LimitsItemRow(name: String, fee: String, limit: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F7F5)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ProkahTextPrimary)
                Text(fee, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ProkahRed)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(limit, fontSize = 11.sp, color = ProkahTextSecondary)
        }
    }
}

// 12. Notifications Sheet
@Composable
fun NotificationsSheet(
    notifications: List<NotificationItem>,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .testTag("notifications_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "নোটিফিকেশন ও অফার" else "Notifications & Offers",
            subtitle = if (language == AppLanguage.BANGLA) "নতুন অফার ও নিরাপত্তা সতর্কবার্তা" else "Latest promotions and security updates",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notifications, key = { it.id }) { item ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFBF8F5),
                    border = BorderStroke(1.dp, ProkahCardBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (language == AppLanguage.BANGLA) item.titleBn else item.titleEn,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ProkahRed
                            )
                            Text(item.timeAgo, fontSize = 10.sp, color = ProkahTextSecondary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (language == AppLanguage.BANGLA) item.messageBn else item.messageEn,
                            fontSize = 12.sp,
                            color = ProkahTextPrimary
                        )
                    }
                }
            }
        }
    }
}

// 13. Contact Us Sheet
@Composable
fun ContactUsSheet(
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
            .testTag("contact_us_sheet")
    ) {
        ModalHeader(
            title = if (language == AppLanguage.BANGLA) "যোগাযোগ করুন" else "Contact Us",
            subtitle = if (language == AppLanguage.BANGLA) "প্রোকাহ গ্রাহক সেবা আপনার পাশে ২৪/৭" else "Prokah 24/7 Customer Support",
            onClose = onDismiss
        )

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECEB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, null, tint = ProkahRed, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(if (language == AppLanguage.BANGLA) "হেল্পলাইন নম্বর" else "Helpline Number", fontSize = 12.sp, color = ProkahTextSecondary)
                    Text("16167 / 09612316167", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ProkahRed)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "ইমেইল: support@prokah.com.bd\nহেড অফিস: প্রোকাহ টাওয়ার, গুলশান-২, ঢাকা-১২১২\nওয়েবসাইট: www.prokah.com.bd",
            fontSize = 13.sp,
            color = ProkahTextPrimary,
            lineHeight = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ProkahRed)
        ) {
            Text(if (language == AppLanguage.BANGLA) "বন্ধ করুন" else "Close", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Modal Header Helper
@Composable
private fun ModalHeader(
    title: String,
    subtitle: String,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ProkahTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = ProkahTextSecondary
            )
        }
        IconButton(onClick = onClose) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = ProkahTextSecondary)
        }
    }
}

// Receipt Row Helper
@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = ProkahTextSecondary)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ProkahTextPrimary)
    }
}
