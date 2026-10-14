package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Transaction
import com.example.ui.theme.ProkahCardBorder
import com.example.ui.theme.ProkahRed
import com.example.ui.theme.ProkahSuccess
import com.example.ui.theme.ProkahTextMuted
import com.example.ui.theme.ProkahTextPrimary
import com.example.ui.theme.ProkahTextSecondary
import com.example.viewmodel.TransactionFilter
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    transactions: List<Transaction>,
    searchQuery: String,
    selectedFilter: TransactionFilter,
    language: AppLanguage,
    onSearchQueryChange: (String) -> Unit,
    onFilterSelect: (TransactionFilter) -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val decimalFormat = remember { DecimalFormat("#,##0.00") }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

    val totalIn = remember(transactions) {
        transactions.filter { !it.type.isDebit }.sumOf { it.amount }
    }
    val totalOut = remember(transactions) {
        transactions.filter { it.type.isDebit }.sumOf { it.amount + it.fee }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F7F5))
            .testTag("transaction_history_screen")
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 44.dp, bottom = 12.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.BANGLA) "লেনদেন বিবরণী" else "Transaction Statement",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProkahTextPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Summary Row (Inflow vs Outflow)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Total Received Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ProkahSuccess),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ArrowDownward, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "মোট প্রাপ্তি" else "Total In",
                                    fontSize = 11.sp,
                                    color = Color(0xFF2E7D32)
                                )
                                Text(
                                    text = "+৳ ${decimalFormat.format(totalIn)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }
                    }

                    // Total Spent Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ProkahRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ArrowUpward, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "মোট খরচ" else "Total Out",
                                    fontSize = 11.sp,
                                    color = ProkahRed
                                )
                                Text(
                                    text = "-৳ ${decimalFormat.format(totalOut)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB71C1C)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("transaction_search_input"),
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.BANGLA) "TrxID, প্রাপক বা নম্বর খুঁজুন..." else "Search by TrxID, name or number...",
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, null, tint = ProkahTextSecondary, modifier = Modifier.size(20.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Clear, "Clear search", modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF7F5F3),
                        unfocusedContainerColor = Color(0xFFF7F5F3),
                        focusedBorderColor = ProkahRed,
                        unfocusedBorderColor = ProkahCardBorder
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Tabs (All / In / Out)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterTabButton(
                        title = if (language == AppLanguage.BANGLA) "সকল লেনদেন" else "All",
                        isSelected = selectedFilter == TransactionFilter.ALL,
                        onClick = { onFilterSelect(TransactionFilter.ALL) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterTabButton(
                        title = if (language == AppLanguage.BANGLA) "প্রাপ্তি (ইন)" else "Received (In)",
                        isSelected = selectedFilter == TransactionFilter.RECEIVED,
                        onClick = { onFilterSelect(TransactionFilter.RECEIVED) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterTabButton(
                        title = if (language == AppLanguage.BANGLA) "প্রদান (আউট)" else "Paid (Out)",
                        isSelected = selectedFilter == TransactionFilter.SENT,
                        onClick = { onFilterSelect(TransactionFilter.SENT) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Transactions List
        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == AppLanguage.BANGLA) "কোনো লেনদেন পাওয়া যায়নি" else "No transactions found",
                        fontSize = 15.sp,
                        color = ProkahTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = contentPadding.calculateBottomPadding() + 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(transactions, key = { it.id }) { tx ->
                    TransactionItemCard(
                        transaction = tx,
                        language = language,
                        decimalFormat = decimalFormat,
                        dateFormat = dateFormat,
                        onClick = { onTransactionClick(tx) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterTabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) ProkahRed else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, ProkahCardBorder)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else ProkahTextSecondary
            )
        }
    }
}

@Composable
private fun TransactionItemCard(
    transaction: Transaction,
    language: AppLanguage,
    decimalFormat: DecimalFormat,
    dateFormat: SimpleDateFormat,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("transaction_item_${transaction.id}"),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, ProkahCardBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Type Icon Badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (transaction.type.isDebit) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (transaction.type.isDebit) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = if (transaction.type.isDebit) ProkahRed else ProkahSuccess,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.BANGLA) transaction.type.titleBn else transaction.type.titleEn,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProkahTextPrimary
                )
                Text(
                    text = transaction.counterPartyName,
                    fontSize = 13.sp,
                    color = ProkahTextSecondary,
                    maxLines = 1
                )
                Text(
                    text = dateFormat.format(Date(transaction.timestamp)),
                    fontSize = 11.sp,
                    color = ProkahTextMuted
                )
                Text(
                    text = "TrxID: ${transaction.id}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF8D6E63)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Amount & Status
            Column(horizontalAlignment = Alignment.End) {
                val prefix = if (transaction.type.isDebit) "-" else "+"
                val amountColor = if (transaction.type.isDebit) ProkahRed else ProkahSuccess

                Text(
                    text = "$prefix৳ ${decimalFormat.format(transaction.amount)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = amountColor
                )

                if (transaction.fee > 0.0) {
                    Text(
                        text = if (language == AppLanguage.BANGLA) "চার্জ: ৳${transaction.fee}" else "Fee: ৳${transaction.fee}",
                        fontSize = 10.sp,
                        color = ProkahTextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.BANGLA) "সফল" else "Success",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProkahSuccess
                    )
                }
            }
        }
    }
}
