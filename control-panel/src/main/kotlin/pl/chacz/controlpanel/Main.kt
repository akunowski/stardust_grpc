package pl.chacz.controlpanel

import com.google.protobuf.Empty
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.async
import pl.chacz.deathstar.DeathStarGrpcKt
import pl.chacz.deathstar.DestroyRequest
import pl.chacz.galaxy.GalaxyGrpcKt


suspend fun main()  {
    val galaxyClient = GalaxyGrpcKt.GalaxyCoroutineStub(
        ManagedChannelBuilder
            .forAddress("localhost", 8802)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build())

    val deathStarClient = DeathStarGrpcKt.DeathStarCoroutineStub(
        ManagedChannelBuilder
            .forAddress("localhost", 8801)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build())

    repeat(5) {
        val planet = GlobalScope.async { galaxyClient.findPlanet(Empty.getDefaultInstance()) }
        val destroyRequest = DestroyRequest.newBuilder().setId(planet.await().id).build()

        deathStarClient.destroy(destroyRequest)
    }


}