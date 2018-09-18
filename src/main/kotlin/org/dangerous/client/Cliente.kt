package org.dangerous.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.dangerous.model.Comando
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class Cliente {
    companion object {
        private val scheduler = Executors.newScheduledThreadPool(2)
        private val LOG = Logger.getLogger("Client")

        @JvmStatic
        fun main(args: Array<String>) {
            var hostname = "localhost"
            var port = 6379

            if (args.isNotEmpty()) {
                hostname = args[0]
                port = args[1].toInt()
            }

            LOG.info("[Client] Connecting on server... IP: $hostname Port: $port")

            val socket = Socket(hostname, port)

            val output = ObjectOutputStream(socket.getOutputStream())
            val input = ObjectInputStream(socket.getInputStream())
            val mapper = jacksonObjectMapper()


            scheduler.scheduleAtFixedRate({
                val comando = Comando(UUID.randomUUID(), Comando.Operacao.SOMA, arrayOf(1, 2))
                output.writeUnshared(mapper.writeValueAsString(comando))
                //mapper.writeValue(output as OutputStream,comando)

                try {
                    val resultado = input.readUnshared()

                    if (resultado is String) {
                        println(resultado)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 1000, 1000, TimeUnit.MILLISECONDS)
        }
    }
}