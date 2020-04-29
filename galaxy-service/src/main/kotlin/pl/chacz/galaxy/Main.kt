package pl.chacz.galaxy

import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.runBlocking
import pl.chacz.log.LogGrpcKt


fun main() = runBlocking{
    val port = 8802
    val channel = ManagedChannelBuilder
            .forAddress("localhost", 8803)
            .usePlaintext()
            .build()
    val logStub = LogGrpcKt.LogCoroutineStub(channel)
    val galaxyService = GalaxyService(logStub = logStub, planetService = PlanetService())


    val server = GalaxyServer(port, galaxyService)
    server.start()
    server.blockUntilShutdown()
}