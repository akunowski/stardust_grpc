package pl.chacz.galaxy

import com.google.protobuf.Empty
import io.grpc.Server
import io.grpc.ServerBuilder
import java.util.*

class GalaxyServer(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(GalaxyService())
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

    private class GalaxyService : GalaxyGrpcKt.GalaxyCoroutineImplBase() {
        override suspend fun findPlanet(request: Empty): Planet {
            //TODO: add logic
            return Planet
                .newBuilder()
                .setId(UUID.randomUUID().toString())
                .build()
        }

        override suspend fun deletePlanet(request: DeletePlanetRequest): Status {
            //TODO: add logic
            return Status
                .newBuilder()
                .setStatus("planet removed form galaxy map")
                .build()
        }
    }
}

fun main() {
    val port = 8802
    val server = GalaxyServer(port)
    server.start()
    server.blockUntilShutdown()
}