package com.outracoisa.n2do2alessandro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity
import com.outracoisa.n2do2alessandro.repository.GameSessionRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = GameSessionRepository(application.applicationContext)
    val allSessions: LiveData<List<GameSessionEntity>> = repository.getAllSessions()
    
    fun insert(session: GameSessionEntity) {
        viewModelScope.launch {
            repository.insert(session)
        }
    }
    
    fun delete(session: GameSessionEntity) {
        viewModelScope.launch {
            repository.delete(session)
        }
    }
    
    fun updateStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateStatus(id, status)
        }
    }
}
