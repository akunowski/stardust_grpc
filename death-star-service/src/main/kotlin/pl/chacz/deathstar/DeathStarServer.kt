package pl.chacz.deathstar

import io.grpc.ManagedChannelBuilder
import io.grpc.Server
import io.grpc.ServerBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.async
import pl.chacz.galaxy.DeletePlanetRequest
import pl.chacz.galaxy.GalaxyGrpcKt

class DeathStarServer(
        private val port: Int,
        galaxyClient: GalaxyGrpcKt.GalaxyCoroutineStub) {

    val server: Server = ServerBuilder
            .forPort(port)
            .addService(DeathStarService(galaxyClient))
            .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
                Thread {
                    this@DeathStarServer.stop()
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

    private class DeathStarService(private val galaxyClient: GalaxyGrpcKt.GalaxyCoroutineStub) : DeathStarGrpcKt.DeathStarCoroutineImplBase() {
        override suspend fun destroy(request: DestroyRequest): DestroyReply {

            println("Destory planet request for planet: ${request.name}")

            val deletePlanetRequest =
                    DeletePlanetRequest
                            .newBuilder()
                            .setId(request.name)
                            .build()

            val status = GlobalScope.async { galaxyClient.deletePlanet(deletePlanetRequest) }
            return DestroyReply
                    .newBuilder()
                    .setStatus(status.await().toString())
                    .build()
        }
    }
}

fun main() {
    val client = GalaxyGrpcKt.GalaxyCoroutineStub(
            ManagedChannelBuilder
                    .forAddress("localhost", 8802)
                    .usePlaintext()
                    .executor(Dispatchers.Default.asExecutor())
                    .build())

    val port = 8801
    val server = DeathStarServer(port, client)
    server.start()
    server.blockUntilShutdown()
}