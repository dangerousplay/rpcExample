package org.dangerous.model

import java.util.*

data class Resposta<T>(val id: UUID, val resposta: T) {
}