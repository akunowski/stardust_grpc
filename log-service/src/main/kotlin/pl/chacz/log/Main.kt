package pl.chacz.log

fun main() {
    val port = 8803
    val logService = LogService()
    val server = LogServer(port, logService)
    server.start()
    server.blockUntilShutdown()
}