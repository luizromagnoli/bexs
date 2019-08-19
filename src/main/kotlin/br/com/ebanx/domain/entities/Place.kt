package br.com.ebanx.domain.entities

data class Place(val id: String): Comparable<Place> {
    override fun compareTo(other: Place): Int {
        return id.compareTo(other.id)
    }

    override fun toString(): String {
        return id
    }
}