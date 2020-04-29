package pl.chacz.deathstar

import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import pl.chacz.galaxy.GalaxyGrpcKt
import pl.chacz.log.LogGrpcKt


fun main() {
    val galaxyStub = GalaxyGrpcKt.GalaxyCoroutineStub(
            ManagedChannelBuilder
                    .forAddress("localhost", 8802)
                    .usePlaintext()
                    .executor(Dispatchers.Default.asExecutor())
                    .build())

    val logStub = LogGrpcKt.LogCoroutineStub(ManagedChannelBuilder
            .forAddress("localhost", 8803)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build())

    val deathStarService = DeathStarService(galaxyStub, logStub)
    val port = 8801
    val server = DeathStarServer(port, deathStarService)
    server.start()
    server.blockUntilShutdown()
}