package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ContactItem
import com.example.ui.theme.ProkahCardBorder
import com.example.ui.theme.ProkahGold
import com.example.ui.theme.ProkahOrange
import com.example.ui.theme.ProkahRed
import com.example.ui.theme.ProkahTextPrimary
import com.example.ui.theme.ProkahTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    contacts: List<ContactItem>,
    language: AppLanguage,
    onContactSelectForTransfer: (ContactItem) -> Unit,
    onAddNewContact: (String, String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredContacts = remember(contacts, searchQuery) {
        if (searchQuery.isBlank()) contacts
        else contacts.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.phoneNumber.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F7F5))
            .testTag("contacts_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 44.dp, bottom = 14.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.BANGLA) "কন্টাক্টস ও বেনিফিশিয়ারি" else "Contacts & Beneficiaries",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProkahTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_search_input"),
                        placeholder = {
                            Text(
                                text = if (language == AppLanguage.BANGLA) "নাম অথবা ফোন নম্বর খুঁজুন..." else "Search by name or phone...",
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, null, tint = ProkahTextSecondary, modifier = Modifier.size(20.dp))
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
                }
            }

            // List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = contentPadding.calculateBottomPadding() + 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredContacts, key = { it.id }) { contact ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onContactSelectForTransfer(contact) }
                            .testTag("contact_card_${contact.id}"),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, ProkahCardBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(contact.avatarColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = contact.name.take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = contact.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ProkahTextPrimary
                                    )
                                    if (contact.isFavorite) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = "Favorite",
                                            tint = ProkahGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = contact.phoneNumber,
                                    fontSize = 13.sp,
                                    color = ProkahTextSecondary
                                )
                            }

                            // Quick Send button
                            Button(
                                onClick = { onContactSelectForTransfer(contact) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFECEB), contentColor = ProkahRed),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Send, null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "টাকা পাঠান" else "Send",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Add Contact Floating Action Button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = contentPadding.calculateBottomPadding() + 20.dp)
                .testTag("add_contact_fab"),
            containerColor = ProkahRed,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Contact")
        }

        // Add Contact Dialog
        if (showAddDialog) {
            var newName by remember { mutableStateOf("") }
            var newPhone by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = {
                    Text(
                        text = if (language == AppLanguage.BANGLA) "নতুন কন্টাক্ট যোগ করুন" else "Add New Beneficiary",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text(if (language == AppLanguage.BANGLA) "পূর্ণ নাম" else "Full Name") },
                            modifier = Modifier.fillMaxWidth().testTag("add_contact_name_input"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = newPhone,
                            onValueChange = { newPhone = it },
                            label = { Text(if (language == AppLanguage.BANGLA) "মোবাইল নম্বর" else "Phone Number") },
                            modifier = Modifier.fillMaxWidth().testTag("add_contact_phone_input"),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newName.isNotBlank() && newPhone.isNotBlank()) {
                                onAddNewContact(newName, newPhone)
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ProkahRed)
                    ) {
                        Text(if (language == AppLanguage.BANGLA) "সংরক্ষণ" else "Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text(if (language == AppLanguage.BANGLA) "বাতিল" else "Cancel")
                    }
                }
            )
        }
    }
}
