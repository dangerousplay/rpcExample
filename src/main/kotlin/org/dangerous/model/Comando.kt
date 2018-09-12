package org.dangerous.model

import java.util.*

data class Comando(val id: UUID, val op: Operacao, val argumentos: Array<Number>) {
    enum class Operacao{
        SOMA, SUBTRACAO, DIVISAO, MULTIPLICACAO
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comando

        if (id != other.id) return false
        if (op != other.op) return false
        if (!Arrays.equals(argumentos, other.argumentos)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + op.hashCode()
        result = 31 * result + Arrays.hashCode(argumentos)
        return result
    }


}