package com.outracoisa.n2do2alessandro.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity
import com.outracoisa.n2do2alessandro.helper.GameConstants
import com.outracoisa.n2do2alessandro.ui.theme.*
import com.outracoisa.n2do2alessandro.viewmodel.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    sessionId: Int,
    viewModel: DetailsViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val session by viewModel.getSessionById(sessionId).observeAsState()
    var showStatusDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Excluir",
                            tint = ErrorRed
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
        val currentSession = session
        if (currentSession != null) {
            DetailsContent(
                session = currentSession,
                modifier = Modifier.padding(paddingValues),
                onStatusClick = { showStatusDialog = true },
                onStatusUpdate = { newStatus ->
                    viewModel.updateStatus(sessionId, newStatus)
                    Toast.makeText(context, "Status atualizado!", Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryPurple)
            }
        }
        
        // Status selection dialog
        if (showStatusDialog && session != null) {
            StatusSelectionDialog(
                currentStatus = session!!.status,
                onStatusSelected = { newStatus ->
                    viewModel.updateStatus(sessionId, newStatus)
                    showStatusDialog = false
                    Toast.makeText(context, "Status atualizado!", Toast.LENGTH_SHORT).show()
                },
                onDismiss = { showStatusDialog = false }
            )
        }
        
        // Delete confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirmar Exclusão") },
                text = { Text("Deseja realmente excluir esta jogatina?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteById(sessionId)
                            showDeleteDialog = false
                            onNavigateBack()
                            Toast.makeText(context, "Jogatina excluída!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("Excluir", color = ErrorRed)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                },
                containerColor = DarkCard,
                titleContentColor = TextPrimary,
                textContentColor = TextSecondary
            )
        }
    }
}

@Composable
private fun DetailsContent(
    session: GameSessionEntity,
    modifier: Modifier = Modifier,
    onStatusClick: () -> Unit,
    onStatusUpdate: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    
    val cardColor = try {
        Color(android.graphics.Color.parseColor(session.color))
    } catch (e: Exception) {
        GameColorBlue
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header card with game name and color
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(cardColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = session.gameName.take(2).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.gameName,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Organizado por ${session.hostName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
        
        // Status card with clickable status change
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            onClick = onStatusClick
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                
                StatusBadge(status = session.status)
            }
        }
        
        // Info card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Informações",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                
                InfoRow(
                    icon = Icons.Default.CalendarMonth,
                    label = "Data",
                    value = session.date
                )
                
                InfoRow(
                    icon = Icons.Default.Schedule,
                    label = "Horário",
                    value = session.time
                )
                
                InfoRow(
                    icon = Icons.Default.People,
                    label = "Jogadores",
                    value = "${getConfirmedCount(session)}/${session.maxPlayers}"
                )
                
                if (session.location.isNotBlank()) {
                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Local",
                        value = session.location
                    )
                }
            }
        }
        
        // Players card
        if (session.playersConfirmed.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Jogadores Confirmados",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    
                    session.playersConfirmed.split(",").forEach { player ->
                        if (player.isNotBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = AccentCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = player.trim(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Notes card
        if (session.notes.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Observações",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    
                    Text(
                        text = session.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryPurple,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun StatusSelectionDialog(
    currentStatus: String,
    onStatusSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val statuses = listOf(
        GameConstants.STATUS.SCHEDULED,
        GameConstants.STATUS.COMPLETED,
        GameConstants.STATUS.CANCELLED
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Alterar Status") },
        text = {
            Column {
                statuses.forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentStatus == status,
                            onClick = { onStatusSelected(status) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = PrimaryPurple,
                                unselectedColor = TextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = status,
                            color = TextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        },
        containerColor = DarkCard,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary
    )
}

private fun getConfirmedCount(session: GameSessionEntity): Int {
    return if (session.playersConfirmed.isBlank()) 0 
    else session.playersConfirmed.split(",").filter { it.isNotBlank() }.size
}
