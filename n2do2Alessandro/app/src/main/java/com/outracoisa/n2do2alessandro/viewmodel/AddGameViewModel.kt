package com.outracoisa.n2do2alessandro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity
import com.outracoisa.n2do2alessandro.repository.GameSessionRepository
import kotlinx.coroutines.launch

class AddGameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GameSessionRepository = GameSessionRepository(application)
    
    fun insertGameSession(gameSession: GameSessionEntity, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.insert(gameSession)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao salvar jogatina")
            }
        }
    }
}
