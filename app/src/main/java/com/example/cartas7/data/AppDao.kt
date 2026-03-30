package com.example.cartas7.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

/**
 * Data access object for players, games and participations.
 */
@Dao
interface AppDao {

    /**
     * Inserts a player if it does not exist.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertJugador(jugador: Jugador): Long

    /**
     * Updates an existing player.
     */
    @Update
    fun updateJugador(jugador: Jugador)

    /**
     * Returns one player by name.
     */
    @Query("SELECT * FROM jugadores WHERE nombre = :nombre LIMIT 1")
    fun findJugadorByNombre(nombre: String): Jugador?

    /**
     * Inserts a played game.
     */
    @Insert
    fun insertPartida(partida: Partida): Long

    /**
     * Inserts one participation row.
     */
    @Insert
    fun insertParticipacion(participacion: Participacion)

    /**
     * Returns all games sorted from newest.
     */
    @Query("SELECT id AS partidaId, fechaMillis, ganador FROM partidas ORDER BY fechaMillis DESC")
    fun getPartidasResumen(): List<PartidaResumen>

    /**
     * Returns games where the given player participated.
     */
    @Query(
        """
        SELECT p.id AS partidaId, p.fechaMillis, p.ganador
        FROM partidas p
        INNER JOIN participaciones pa ON pa.partidaId = p.id
        INNER JOIN jugadores j ON j.id = pa.jugadorId
        WHERE j.nombre = :nombre
        ORDER BY p.fechaMillis DESC
        """
    )
    fun getPartidasByJugador(nombre: String): List<PartidaResumen>

    /**
     * Creates players if missing and returns the latest rows.
     */
    @Transaction
    fun getOrCreateJugadores(nombres: List<String>): List<Jugador> {
        val jugadores = ArrayList<Jugador>()
        for (nombre in nombres) {
            val existente = findJugadorByNombre(nombre)
            if (existente != null) {
                jugadores.add(existente)
            } else {
                insertJugador(Jugador(nombre = nombre.trim()))
                val creado = findJugadorByNombre(nombre)
                if (creado != null) {
                    jugadores.add(creado)
                }
            }
        }
        return jugadores
    }
}
