package com.example.cartas7.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a player with a reusable balance in euros.
 */
@Entity(tableName = "jugadores")
data class Jugador(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val saldo: Int = 100
)

/**
 * Represents one played game with ten betting rounds.
 */
@Entity(tableName = "partidas")
data class Partida(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fechaMillis: Long,
    val ganador: String
)

/**
 * Represents a player's participation in one game.
 */
@Entity(
    tableName = "participaciones",
    foreignKeys = [
        ForeignKey(
            entity = Partida::class,
            parentColumns = ["id"],
            childColumns = ["partidaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Jugador::class,
            parentColumns = ["id"],
            childColumns = ["jugadorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("partidaId"), Index("jugadorId")]
)
data class Participacion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val partidaId: Long,
    val jugadorId: Long,
    val apuestaTotal: Int,
    val puntos: Int,
    val sePlanto: Boolean
)

/**
 * Projection used to render game rows in lists.
 */
data class PartidaResumen(
    val partidaId: Long,
    val fechaMillis: Long,
    val ganador: String
)
