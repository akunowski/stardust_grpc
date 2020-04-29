package pl.chacz.log

import com.google.protobuf.Empty
import io.grpc.netty.shaded.io.netty.util.internal.logging.InternalLogger
import io.grpc.netty.shaded.io.netty.util.internal.logging.Log4J2LoggerFactory

class LogService : LogGrpcKt.LogCoroutineImplBase() {

    override suspend fun planetCreate(request: Message): Empty {
        logger.info(request.message)
        return Empty.getDefaultInstance()
    }

    override suspend fun planetTarget(request: Message): Empty {
        logger.info(request.message)
        return Empty.getDefaultInstance()
    }

    override suspend fun planetDestroy(request: Message): Empty {
        logger.info(request.message)
        return Empty.getDefaultInstance()
    }

    companion object {
        val logger: InternalLogger = Log4J2LoggerFactory.getInstance(LogService::class.java)
    }
}