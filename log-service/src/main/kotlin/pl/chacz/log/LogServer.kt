package pl.chacz.log

import io.grpc.Server
import io.grpc.ServerBuilder

class LogServer(
        private val port: Int,
        logService: LogService) {

    val server: Server = ServerBuilder
            .forPort(port)
            .addService(logService)
            .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
                Thread {
                    this@LogServer.stop()
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