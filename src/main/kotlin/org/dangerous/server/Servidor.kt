package org.dangerous.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jdk.nashorn.internal.parser.JSONParser
import org.dangerous.model.Comando
import org.dangerous.model.Resposta
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.math.BigDecimal
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

object Servidor {
    private lateinit var socket: ServerSocket
    private val EXECUTOR = Executors.newScheduledThreadPool(50)
    private val mapper = jacksonObjectMapper()

    @JvmStatic
    fun main(args: Array<String>) {
        socket = ServerSocket(6379)

        EXECUTOR.submit {
            while (true){
                val connection = socket.accept()

                val output = ObjectOutputStream(connection.getOutputStream())
                val input = ObjectInputStream(connection.getInputStream())

                EXECUTOR.submit {
                    while(true){
                        if(connection.isClosed)
                            break

                        val obj = input.readUnshared()

                        when(obj){
                            obj is String -> {
                                try {
                                    val comando = mapper.readValue<Comando>(obj as String)

                                    when(comando.op){
                                        Comando.Operacao.DIVISAO -> {
                                            val divided = comando.argumentos.map { BigDecimal(it.toString()) }.reduce{ A,B -> A.divide(B)}
                                            val resposta = Resposta<BigDecimal>(comando.id, divided)

                                            mapper.writeValue(output as OutputStream, resposta)
                                        }
                                        Comando.Operacao.MULTIPLICACAO -> {
                                            val divided = comando.argumentos.map { BigDecimal(it.toString()) }.reduce{ A,B -> A.multiply(B)}
                                            val resposta = Resposta<BigDecimal>(comando.id, divided)

                                            mapper.writeValue(output as OutputStream, resposta)
                                        }
                                        Comando.Operacao.SOMA -> {
                                            val divided = comando.argumentos.map { BigDecimal(it.toString()) }.reduce{ A,B -> A.add(B)}
                                            val resposta = Resposta<BigDecimal>(comando.id, divided)

                                            mapper.writeValue(output as OutputStream, resposta)
                                        }
                                        Comando.Operacao.SUBTRACAO -> {
                                            val divided = comando.argumentos.map { BigDecimal(it.toString()) }.reduce{ A,B -> A.subtract(B)}
                                            val resposta = Resposta<BigDecimal>(comando.id, divided)

                                            mapper.writeValue(output as OutputStream, resposta)
                                        }
                                    }

                                }catch (e: Exception){

                                }
                            }
                        }

                    }
                }
            }
        }
    }
}