package pl.chacz.log

import com.google.protobuf.Empty
import io.grpc.Server
import io.grpc.ServerBuilder

class LogServer(private val port: Int) {

    val server: Server = ServerBuilder
        .forPort(port)
        .addService(LogService())
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

    private class LogService: LogGrpcKt.LogCoroutineImplBase() {
        override suspend fun newPlanet(request: Planet): Empty {
            //TODO: add logic
            return Empty.getDefaultInstance()
        }

        override suspend fun planetDestroy(request: Planet): Empty {
            //TODO: add logic
            return Empty.getDefaultInstance()
        }
    }
}

fun main() {
    val port = 8802
    val server = LogServer(port)
    server.start()
    server.blockUntilShutdown()
}