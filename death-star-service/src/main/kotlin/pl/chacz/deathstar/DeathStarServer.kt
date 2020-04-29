package pl.chacz.deathstar

import io.grpc.Server
import io.grpc.ServerBuilder

class DeathStarServer(
        private val port: Int,
        deathStarService: DeathStarService) {

    val server: Server = ServerBuilder
            .forPort(port)
            .addService(deathStarService)
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
}
