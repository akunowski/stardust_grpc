package pl.chacz.galaxy

import com.google.protobuf.Empty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.chacz.log.LogGrpcKt
import pl.chacz.log.Message
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class GalaxyService(
        private val planets: ConcurrentHashMap<String, Planet> = ConcurrentHashMap(),
        private val logStub: LogGrpcKt.LogCoroutineStub,
        private val planetService: PlanetService) : GalaxyGrpcKt.GalaxyCoroutineImplBase() {

    override suspend fun findPlanet(request: Empty): Planet {
        val planet = planetService.createPlanet()
        planets.putIfAbsent(planet.id, planet)
        logCreation(planet.id)
        return planet
    }

    override suspend fun deletePlanet(request: DeletePlanetRequest): Status {
        val removedPlaned = planets.remove(request.id)
        val status = if (removedPlaned == null) "failed" else "success"
        logPlanetRemoval(request.id)

        return Status
                .newBuilder()
                .setStatus(status)
                .build()
    }

    private fun logCreation(planetId: String) = GlobalScope.launch {
        logStub.planetCreate(Message
                .newBuilder()
                .setMessage("Created planet with id: $planetId")
                .build())
    }

    private fun logPlanetRemoval(planetId: String) = GlobalScope.launch {
        logStub.planetDestroy(Message
                .newBuilder()
                .setMessage("Removed planet with id: $planetId")
                .build())
    }
}

class PlanetService() {

    fun createPlanet(): Planet = Planet.newBuilder().setId(UUID.randomUUID().toString()).build()
}