package com.example.boton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boton.ui.theme.BotonTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    YapeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun YapeScreen(modifier: Modifier = Modifier) {
    // --- Estados de la App ---
    var amount by remember { mutableStateOf("") }
    var recipientName by remember { mutableStateOf("") }
    var currentBalance by remember { mutableStateOf("500.0") }
    var transactionMessage by remember { mutableStateOf("") }

    // Formateador para mostrar la moneda en formato local (S/ 500.00)
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("es", "PE")) }

    // --- Lógica de la Transacción ---
    val onYapearClick: () -> Unit = {
        val amountToSend = amount.toDoubleOrNull()
        val currentBalanceDouble = currentBalance.toDoubleOrNull()

        if (amountToSend == null || amountToSend <= 0) {
            transactionMessage = "Por favor, ingresa un monto válido para enviar."
        } else if (currentBalanceDouble == null) {
            transactionMessage = "Por favor, ingresa un saldo total válido."
        } else if (recipientName.isBlank()) {
            transactionMessage = "Por favor, ingresa el nombre del destinatario."
        } else if (amountToSend > currentBalanceDouble) {
            transactionMessage = "Saldo insuficiente."
        } else {
            // La transacción es exitosa: actualiza el saldo y limpia los campos
            val newBalance = currentBalanceDouble - amountToSend
            currentBalance = newBalance.toString()

            transactionMessage = "¡Yapeaste ${currencyFormatter.format(amountToSend)} a $recipientName!\nTu nuevo saldo es ${currencyFormatter.format(newBalance)}"
            amount = ""
            recipientName = ""
        }
    }

    // --- Interfaz de Usuario ---
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = currentBalance,
            onValueChange = {
                // Validación: solo permite números y un punto decimal
                currentBalance = it.filter { char -> char.isDigit() || char == '.' }
            },
            label = { Text("Monto total") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = {
                // Validación: solo permite números y un punto decimal
                amount = it.filter { char -> char.isDigit() || char == '.' }
            },
            label = { Text("Monto a enviar") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = recipientName,
            onValueChange = { recipientName = it },
            label = { Text("Nombre del destinatario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onYapearClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Yapear")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (transactionMessage.isNotEmpty()) {
            Text(
                text = transactionMessage,
                textAlign = TextAlign.Center
            )
        }
    }
}
