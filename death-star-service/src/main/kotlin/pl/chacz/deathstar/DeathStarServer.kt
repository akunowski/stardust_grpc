package pl.chacz.deathstar

import io.grpc.Server
import io.grpc.ServerBuilder

class DeathStarServer(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(DeathStarService())
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

    private class DeathStarService: DeathStarGrpcKt.DeathStarCoroutineImplBase() {
        override suspend fun destroy(request: DestroyRequest): DestroyReply {
            //TODO all logic
            return DestroyReply
                .newBuilder()
                .setStatus("status")
                .build()
        }
    }
}

fun main() {
    val port = 8801
    val server = DeathStarServer(port)
    server.start()
    server.blockUntilShutdown()
}