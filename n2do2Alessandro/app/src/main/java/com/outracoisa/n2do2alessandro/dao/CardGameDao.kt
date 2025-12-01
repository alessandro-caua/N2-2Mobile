package com.outracoisa.n2do2alessandro.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity

@Dao
interface GameSessionDao {
    
    @Query("SELECT * FROM game_sessions ORDER BY date DESC, time DESC")
    fun getAllSessions(): LiveData<List<GameSessionEntity>>
    
    @Query("SELECT * FROM game_sessions WHERE status = 'Agendado' ORDER BY date ASC, time ASC")
    fun getUpcomingSessions(): LiveData<List<GameSessionEntity>>
    
    @Query("SELECT * FROM game_sessions WHERE status = 'Realizado' ORDER BY date DESC, time DESC")
    fun getCompletedSessions(): LiveData<List<GameSessionEntity>>
    
    @Query("SELECT * FROM game_sessions WHERE id = :id")
    fun getSessionById(id: Int): LiveData<GameSessionEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: GameSessionEntity): Long
    
    @Update
    suspend fun update(session: GameSessionEntity)
    
    @Delete
    suspend fun delete(session: GameSessionEntity)
    
    @Query("DELETE FROM game_sessions WHERE id = :id")
    suspend fun deleteById(id: Int)
    
    @Query("UPDATE game_sessions SET players_confirmed = :players WHERE id = :id")
    suspend fun updatePlayers(id: Int, players: String)
    
    @Query("UPDATE game_sessions SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)
}
