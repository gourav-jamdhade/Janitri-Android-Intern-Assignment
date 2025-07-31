package com.example.janitiriandroidinternassignment.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.janitiriandroidinternassignment.R
import com.example.janitiriandroidinternassignment.data.VitalsEntry
import com.example.janitiriandroidinternassignment.ui.theme.AddVitalsDialogHeaderColor
import com.example.janitiriandroidinternassignment.ui.theme.FABBackgroundColor
import com.example.janitiriandroidinternassignment.viewmodel.VitalsViewModel


@Composable
fun MainScreen(viewModel: VitalsViewModel, forceShowDialog: Boolean = false) {

    val vitalsList by viewModel.vitals.collectAsState()
    var showDialog by remember { mutableStateOf(forceShowDialog) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                shape = CircleShape,
                containerColor = FABBackgroundColor,
            ) {
                Text(
                    text = "+",
                    fontSize = 55.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light
                )
            }
        },
        topBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFC8ADFC))

            ) {
                Text(
                    "Track My Pregnancy",
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF5F1C9C),
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }

        }
    ) { paddingValues ->

        if (vitalsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No vitals logged yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(vitalsList) { entry ->
                    VitalsCard(entry)
                }
            }
        }

        if (showDialog) {
            AddVitalsDialog(
                onDismiss = { showDialog = false },
                onSubmit = { entry ->

                    viewModel.addVitals(entry)
                    showDialog = false
                }
            )
        }

    }
}


@Composable
fun VitalsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isNumber: Boolean = true,
    error: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (!isNumber || newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        isError = error != null,
        modifier = modifier,
        keyboardOptions = if (isNumber) KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
    )
    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
        )
    }
}

