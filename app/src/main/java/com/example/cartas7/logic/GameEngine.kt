package com.example.cartas7.logic

import com.example.cartas7.data.AppDao
import com.example.cartas7.data.Jugador
import com.example.cartas7.data.Participacion
import com.example.cartas7.data.Partida
import java.util.Random

/**
 * Handles game simulation and persistence for one 7s game.
 */
class GameEngine(private val dao: AppDao) {

    /**
     * Result object returned after simulating a game.
     */
    data class GameResult(
        val winner: String?,
        val message: String
    )

    /**
     * Simulates 10 betting rounds for the provided players.
     */
    fun playGame(playerNames: List<String>): GameResult {
        val jugadores = dao.getOrCreateJugadores(playerNames)
        val activos = jugadores.filter { it.saldo > 0 }
        if (activos.isEmpty()) {
            return GameResult(null, "NO_SALDO")
        }

        val random = Random()
        val puntosMap = HashMap<Long, Int>()
        val apuestaMap = HashMap<Long, Int>()
        val sePlantaMap = HashMap<Long, Boolean>()

        for (jugador in activos) {
            puntosMap[jugador.id] = 0
            apuestaMap[jugador.id] = 0
            sePlantaMap[jugador.id] = false
        }

        repeat(10) {
            for (jugador in activos) {
                val planted = sePlantaMap[jugador.id] ?: false
                if (planted) continue
                val currentBet = apuestaMap[jugador.id] ?: 0
                if (currentBet >= jugador.saldo) {
                    sePlantaMap[jugador.id] = true
                    continue
                }

                apuestaMap[jugador.id] = currentBet + 1
                val draw = random.nextInt(7) + 1
                val total = (puntosMap[jugador.id] ?: 0) + draw
                puntosMap[jugador.id] = total

                if (total >= 7) {
                    sePlantaMap[jugador.id] = true
                }
            }
        }

        val winnerJugador = resolveWinner(activos, puntosMap)
        val partidaId = dao.insertPartida(
            Partida(
                fechaMillis = System.currentTimeMillis(),
                ganador = winnerJugador?.nombre ?: "Sin ganador"
            )
        )

        for (jugador in activos) {
            val bet = apuestaMap[jugador.id] ?: 0
            val newSaldo = (jugador.saldo - bet).coerceAtLeast(0)
            dao.updateJugador(jugador.copy(saldo = newSaldo))
            dao.insertParticipacion(
                Participacion(
                    partidaId = partidaId,
                    jugadorId = jugador.id,
                    apuestaTotal = bet,
                    puntos = puntosMap[jugador.id] ?: 0,
                    sePlanto = sePlantaMap[jugador.id] ?: false
                )
            )
        }

        return GameResult(winnerJugador?.nombre, "OK")
    }

    /**
     * Chooses a winner with highest score not greater than 7.
     */
    private fun resolveWinner(jugadores: List<Jugador>, puntosMap: Map<Long, Int>): Jugador? {
        var winner: Jugador? = null
        var bestScore = -1

        for (jugador in jugadores) {
            val puntos = puntosMap[jugador.id] ?: 0
            if (puntos <= 7 && puntos > bestScore) {
                bestScore = puntos
                winner = jugador
            }
        }
        return winner
    }
}
