package com.outracoisa.n2do2alessandro.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity
import com.outracoisa.n2do2alessandro.helper.LolBlocker
import com.outracoisa.n2do2alessandro.ui.theme.*
import com.outracoisa.n2do2alessandro.viewmodel.AddGameViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGameScreen(
    viewModel: AddGameViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onGameBlocked: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    // Form state
    var gameName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var hostName by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableStateOf("4") }
    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("#2196F3") }
    
    // Validation errors
    var gameNameError by remember { mutableStateOf<String?>(null) }
    var dateError by remember { mutableStateOf<String?>(null) }
    var timeError by remember { mutableStateOf<String?>(null) }
    var hostNameError by remember { mutableStateOf<String?>(null) }
    
    val colorOptions = listOf(
        "#2196F3" to GameColorBlue,
        "#4CAF50" to GameColorGreen,
        "#FF9800" to GameColorOrange,
        "#9C27B0" to GameColorPurple,
        "#F44336" to GameColorRed,
        "#009688" to GameColorTeal
    )
    
    // Date picker
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
            dateError = null
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    
    // Time picker
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            time = String.format("%02d:%02d", hourOfDay, minute)
            timeError = null
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    
    fun validateAndSave() {
        var isValid = true
        
        if (gameName.isBlank()) {
            gameNameError = "Nome do jogo é obrigatório"
            isValid = false
        } else {
            gameNameError = null
        }
        
        if (date.isBlank()) {
            dateError = "Data é obrigatória"
            isValid = false
        } else {
            dateError = null
        }
        
        if (time.isBlank()) {
            timeError = "Horário é obrigatório"
            isValid = false
        } else {
            timeError = null
        }
        
        if (hostName.isBlank()) {
            hostNameError = "Nome do anfitrião é obrigatório"
            isValid = false
        } else {
            hostNameError = null
        }
        
        if (!isValid) return
        
        // Check for LoL Easter egg
        if (LolBlocker.isGameNameSuspicious(gameName)) {
            LolBlocker.blockUser(context)
            Toast.makeText(
                context,
                "🚫 VOCÊ TENTOU ADICIONAR LEAGUE OF LEGENDS!\nBloqueado por 24 horas!",
                Toast.LENGTH_LONG
            ).show()
            onGameBlocked()
            return
        }
        
        val session = GameSessionEntity(
            gameName = gameName,
            date = date,
            time = time,
            hostName = hostName,
            maxPlayers = maxPlayers.toIntOrNull() ?: 4,
            location = location,
            notes = notes,
            color = selectedColor
        )
        
        viewModel.insertGameSession(
            gameSession = session,
            onSuccess = {
                Toast.makeText(context, "Jogatina criada com sucesso!", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            },
            onError = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Jogatina") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Game Name
            OutlinedTextField(
                value = gameName,
                onValueChange = { 
                    gameName = it
                    gameNameError = null
                },
                label = { Text("Nome do Jogo *") },
                placeholder = { Text("Ex: Banco Imobiliário") },
                isError = gameNameError != null,
                supportingText = gameNameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors(),
                singleLine = true
            )
            
            // Date and Time row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Date field
                OutlinedTextField(
                    value = date,
                    onValueChange = { },
                    label = { Text("Data *") },
                    placeholder = { Text("DD/MM/AAAA") },
                    isError = dateError != null,
                    supportingText = dateError?.let { { Text(it) } },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { datePickerDialog.show() },
                    enabled = false,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Selecionar data",
                                tint = PrimaryPurple
                            )
                        }
                    },
                    colors = textFieldColors()
                )
                
                // Time field
                OutlinedTextField(
                    value = time,
                    onValueChange = { },
                    label = { Text("Horário *") },
                    placeholder = { Text("HH:MM") },
                    isError = timeError != null,
                    supportingText = timeError?.let { { Text(it) } },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { timePickerDialog.show() },
                    enabled = false,
                    trailingIcon = {
                        IconButton(onClick = { timePickerDialog.show() }) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Selecionar horário",
                                tint = PrimaryPurple
                            )
                        }
                    },
                    colors = textFieldColors()
                )
            }
            
            // Host Name
            OutlinedTextField(
                value = hostName,
                onValueChange = { 
                    hostName = it
                    hostNameError = null
                },
                label = { Text("Anfitrião *") },
                placeholder = { Text("Quem está organizando?") },
                isError = hostNameError != null,
                supportingText = hostNameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors(),
                singleLine = true
            )
            
            // Max Players
            OutlinedTextField(
                value = maxPlayers,
                onValueChange = { 
                    if (it.isEmpty() || it.toIntOrNull() != null) {
                        maxPlayers = it
                    }
                },
                label = { Text("Máximo de Jogadores") },
                placeholder = { Text("4") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            
            // Location
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Local") },
                placeholder = { Text("Onde será a jogatina?") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors(),
                singleLine = true
            )
            
            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Observações") },
                placeholder = { Text("Alguma informação adicional?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = textFieldColors(),
                maxLines = 4
            )
            
            // Color selector
            Text(
                text = "Cor do Card",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(colorOptions) { (hex, color) ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (selectedColor == hex) {
                                    Modifier.border(3.dp, Color.White, CircleShape)
                                } else {
                                    Modifier
                                }
                            )
                            .clickable { selectedColor = hex }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Save button
            Button(
                onClick = { validateAndSave() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Criar Jogatina",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    disabledTextColor = TextPrimary,
    focusedContainerColor = DarkCard,
    unfocusedContainerColor = DarkCard,
    disabledContainerColor = DarkCard,
    focusedBorderColor = PrimaryPurple,
    unfocusedBorderColor = TextSecondary,
    focusedLabelColor = PrimaryPurple,
    unfocusedLabelColor = TextSecondary,
    disabledLabelColor = TextSecondary,
    focusedPlaceholderColor = TextSecondary,
    unfocusedPlaceholderColor = TextSecondary,
    disabledPlaceholderColor = TextSecondary,
    errorBorderColor = ErrorRed,
    errorLabelColor = ErrorRed,
    errorSupportingTextColor = ErrorRed
)
