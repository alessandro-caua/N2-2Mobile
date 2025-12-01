package com.outracoisa.n2do2alessandro.helper

object GameConstants {
    
    object TYPE {
        const val VIDEO_GAME = "Videogame"
        const val BOARD_GAME = "Tabuleiro"
        const val CARD_GAME = "Cartas"
        const val RPG = "RPG"
        const val PARTY_GAME = "Party"
        const val SPORTS = "Esportes"
        const val OTHER = "Outro"
    }
    
    object STATUS {
        const val SCHEDULED = "Agendado"
        const val COMPLETED = "Realizado"
        const val CANCELLED = "Cancelado"
    }
    
    object BUNDLE {
        const val GAME_ID = "game_id"
        const val SESSION_ID = "session_id"
    }
}
