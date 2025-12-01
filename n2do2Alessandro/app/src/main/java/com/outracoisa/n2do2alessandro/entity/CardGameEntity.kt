package com.outracoisa.n2do2alessandro.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_sessions")
data class GameSessionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    
    @ColumnInfo(name = "game_name")
    val gameName: String,
    
    @ColumnInfo(name = "date")
    val date: String, // formato: dd/MM/yyyy
    
    @ColumnInfo(name = "time")
    val time: String, // formato: HH:mm
    
    @ColumnInfo(name = "host_name")
    val hostName: String, // quem criou a jogatina
    
    @ColumnInfo(name = "players_confirmed")
    val playersConfirmed: String = "", // lista separada por vírgula
    
    @ColumnInfo(name = "max_players")
    val maxPlayers: Int = 4,
    
    @ColumnInfo(name = "location")
    val location: String = "",
    
    @ColumnInfo(name = "notes")
    val notes: String = "",
    
    @ColumnInfo(name = "status")
    val status: String = "Agendado", // Agendado, Realizado, Cancelado
    
    @ColumnInfo(name = "color")
    val color: String = "#2196F3" // cor do card
)
