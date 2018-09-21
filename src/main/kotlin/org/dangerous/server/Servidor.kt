package org.dangerous.server

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.dangerous.model.Comando
import org.dangerous.model.Resposta
import java.io.EOFException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.Inet4Address
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.SocketException
import java.util.concurrent.Executors
import java.util.logging.Logger

object Servidor {
    private lateinit var socket: ServerSocket
    private val EXECUTOR = Executors.newScheduledThreadPool(50)
    private val mapper = jacksonObjectMapper()
    private val LOG = Logger.getLogger("Server")


    @JvmStatic
    fun main(args: Array<String>) {
        socket = ServerSocket(6379)

        LOG.info("[Server] Starting server on port 6379")

        EXECUTOR.submit {
            while (true) {
                val connection = socket.accept()

                LOG.info("[Server] Received client from IP: ${connection.inetAddress.hostName}")

                val output = ObjectOutputStream(connection.getOutputStream())
                val input = ObjectInputStream(connection.getInputStream())

                EXECUTOR.submit {
                    while (true) {
                        if (!connection.isConnected || connection.isClosed || !connection.isBound)
                            break

                        try {
                            val obj = input.readUnshared()

                            if(obj is String){
                                val comando = mapper.readValue<Comando>(obj)

                                when (comando.op) {
                                    Comando.Operacao.DIVISAO -> {
                                        val divided = comando.argumentos.map { BigDecimal(it.toString()) }.reduce { A, B -> A.divide(B, 10, RoundingMode.UP) }
                                        val resposta = Resposta<BigDecimal>(comando.id, divided)

                                        //mapper.writeValue(output as OutputStream, resposta)
                                        output.writeUnshared(mapper.writeValueAsString(resposta))
                                    }

                                    Comando.Operacao.MULTIPLICACAO -> {
                                        val times = comando.argumentos.map { BigDecimal(it.toString()) }.reduce { A, B -> A.multiply(B) }
                                        val resposta = Resposta<BigDecimal>(comando.id, times)

                                        output.writeUnshared(mapper.writeValueAsString(resposta))
                                    }

                                    Comando.Operacao.SOMA -> {
                                        val sum = comando.argumentos.map { BigDecimal(it.toString()) }.reduce { A, B -> A.add(B) }
                                        val resposta = Resposta<BigDecimal>(comando.id, sum)

                                        output.writeUnshared(mapper.writeValueAsString(resposta))
                                    }

                                    Comando.Operacao.SUBTRACAO -> {
                                        val subtract = comando.argumentos.map { BigDecimal(it.toString()) }.reduce { A, B -> A.subtract(B) }
                                        val resposta = Resposta<BigDecimal>(comando.id, subtract)

                                        output.writeUnshared(mapper.writeValueAsString(resposta))
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            if(e is EOFException || e is SocketException)
                                break

                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}