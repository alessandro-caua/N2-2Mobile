package com.outracoisa.n2do2alessandro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity
import com.outracoisa.n2do2alessandro.repository.GameSessionRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = GameSessionRepository(application.applicationContext)
    val upcomingSessions: LiveData<List<GameSessionEntity>> = repository.getUpcomingSessions()
    
    fun delete(session: GameSessionEntity) {
        viewModelScope.launch {
            repository.delete(session)
        }
    }
}
