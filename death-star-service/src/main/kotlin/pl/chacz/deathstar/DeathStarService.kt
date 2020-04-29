package pl.chacz.deathstar

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.chacz.galaxy.DeletePlanetRequest
import pl.chacz.galaxy.GalaxyGrpcKt
import pl.chacz.log.LogGrpcKt
import pl.chacz.log.Message

class DeathStarService(
        private val galaxyStub: GalaxyGrpcKt.GalaxyCoroutineStub,
        private val logStub: LogGrpcKt.LogCoroutineStub) : DeathStarGrpcKt.DeathStarCoroutineImplBase() {
    override suspend fun destroy(request: DestroyRequest): DestroyReply {

        logTarget(request.id)

        val deletePlanetRequest =
                DeletePlanetRequest
                        .newBuilder()
                        .setId(request.id)
                        .build()

        val status = GlobalScope.async { galaxyStub.deletePlanet(deletePlanetRequest) }
        return DestroyReply
                .newBuilder()
                .setStatus(status.await().toString())
                .build()
    }


    private fun logTarget(planetId: String) = GlobalScope.launch {
        logStub.planetTarget(
                Message
                        .newBuilder()
                        .setMessage("Targeting $planetId")
                        .build())
    }
}