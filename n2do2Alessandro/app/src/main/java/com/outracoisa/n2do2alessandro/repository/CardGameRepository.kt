package com.outracoisa.n2do2alessandro.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.outracoisa.n2do2alessandro.dao.GameSessionDao
import com.outracoisa.n2do2alessandro.database.GameSessionDatabase
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity

class GameSessionRepository(context: Context) {
    
    private val database: GameSessionDatabase = GameSessionDatabase.getDatabase(context)
    private val gameSessionDao: GameSessionDao = database.gameSessionDao()
    
    // Seleção - Listar todas as sessões
    fun getAllSessions(): LiveData<List<GameSessionEntity>> {
        return gameSessionDao.getAllSessions()
    }
    
    // Seleção - Listar sessões agendadas
    fun getUpcomingSessions(): LiveData<List<GameSessionEntity>> {
        return gameSessionDao.getUpcomingSessions()
    }
    
    // Seleção - Listar sessões realizadas
    fun getCompletedSessions(): LiveData<List<GameSessionEntity>> {
        return gameSessionDao.getCompletedSessions()
    }
    
    // Seleção - Buscar sessão por ID
    fun getSessionById(id: Int): LiveData<GameSessionEntity> {
        return gameSessionDao.getSessionById(id)
    }
    
    // Inserção - Adicionar nova sessão
    suspend fun insert(session: GameSessionEntity): Long {
        return gameSessionDao.insert(session)
    }
    
    // Atualização - Atualizar sessão existente
    suspend fun update(session: GameSessionEntity) {
        gameSessionDao.update(session)
    }
    
    // Remoção - Deletar sessão
    suspend fun delete(session: GameSessionEntity) {
        gameSessionDao.delete(session)
    }
    
    // Remoção - Deletar por ID
    suspend fun deleteById(id: Int) {
        gameSessionDao.deleteById(id)
    }
    
    // Atualização - Atualizar jogadores confirmados
    suspend fun updatePlayers(id: Int, players: String) {
        gameSessionDao.updatePlayers(id, players)
    }
    
    // Atualização - Atualizar status da sessão
    suspend fun updateStatus(id: Int, status: String) {
        gameSessionDao.updateStatus(id, status)
    }
    
    // Função auxiliar - Confirmar presença de um jogador
    suspend fun confirmPlayerPresence(sessionId: Int, playerName: String) {
        val session = gameSessionDao.getSessionById(sessionId).value
        session?.let {
            val currentPlayers = if (it.playersConfirmed.isBlank()) {
                listOf()
            } else {
                it.playersConfirmed.split(",").map { p -> p.trim() }
            }
            
            if (!currentPlayers.contains(playerName)) {
                val updatedPlayers = (currentPlayers + playerName).joinToString(",")
                gameSessionDao.updatePlayers(sessionId, updatedPlayers)
            }
        }
    }
}
