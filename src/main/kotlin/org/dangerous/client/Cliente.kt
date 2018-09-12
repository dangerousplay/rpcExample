package org.dangerous.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.dangerous.model.Comando
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.Socket
import java.util.*

class Cliente {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val socket = Socket("127.0.0.1", 6379)

            val output = ObjectOutputStream(socket.getOutputStream())
            val input = ObjectInputStream(socket.getInputStream())
            val mapper = jacksonObjectMapper()

            while(true){
                val comando = Comando(UUID.randomUUID(), Comando.Operacao.SOMA, arrayOf(1,2))
                mapper.writeValue(output as OutputStream,comando)

                try {
                    val resultado = input.readUnshared()

                    when(resultado){
                        resultado is String -> {
                            println(resultado)
                        }
                    }
                }catch (e: Exception){

                }

                Thread.sleep(2000)
            }
        }
    }
}