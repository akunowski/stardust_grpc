package pl.chacz.galaxy

import com.google.protobuf.Empty
import io.grpc.Server
import io.grpc.ServerBuilder

class GalaxyServer(
    private val port: Int,
    planetService: PlanetService) {

    val server: Server = ServerBuilder
        .forPort(port)
        .addService(GalaxyService(planetService))
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                this@GalaxyServer.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    private class GalaxyService(
        private val planetService: PlanetService) : GalaxyGrpcKt.GalaxyCoroutineImplBase() {

        override suspend fun findPlanet(request: Empty): Planet {
            val planet = planetService.createPlanet()
            println("Found planet: ${planet.id}")
            return planet
        }

        override suspend fun deletePlanet(request: DeletePlanetRequest): Status {

            val removedPlaned = planetService.removePlanet(request.id)
            val status = if (removedPlaned == null) "failed" else "success"
            println("Removed planet: ${removedPlaned!!.id}")

            return Status
                .newBuilder()
                .setStatus(status)
                .build()
        }
    }
}

fun main() {
    val port = 8802
    val server = GalaxyServer(port, PlanetService())
    server.start()
    server.blockUntilShutdown()
}