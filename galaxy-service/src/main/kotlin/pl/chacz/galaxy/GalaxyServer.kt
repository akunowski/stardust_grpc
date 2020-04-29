package pl.chacz.galaxy

import io.grpc.Server
import io.grpc.ServerBuilder

class GalaxyServer(
        private val port: Int,
        galaxyService: GalaxyService) {

    val server: Server = ServerBuilder
            .forPort(port)
            .addService(galaxyService)
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
}