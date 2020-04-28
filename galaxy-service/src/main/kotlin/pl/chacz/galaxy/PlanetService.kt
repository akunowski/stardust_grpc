package pl.chacz.galaxy

import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PlanetService(private val planets: ConcurrentHashMap<String, Planet> = ConcurrentHashMap()) {
    
    fun createPlanet(): Planet {
        val planet = Planet.newBuilder().setId(UUID.randomUUID().toString()).build()
        planets.putIfAbsent(planet.id, planet)
        return planet
    }

    fun removePlanet(planetId: String) = planets.remove(planetId)
}