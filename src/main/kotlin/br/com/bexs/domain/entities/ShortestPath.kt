package br.com.bexs.domain.entities

data class ShortestPath(val Place: List<Place>, val distance: Int) {
    override fun toString(): String {
        val path = StringBuilder()

        path.append(Place.joinToString(" - ") {
            it.toString()
        }).append(" > $$distance")

        return path.toString()
    }
}
