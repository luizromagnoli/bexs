package br.com.ebanx.resources

import br.com.ebanx.domain.entities.Path
import br.com.ebanx.domain.entities.Place
import java.io.File

object InputParser {
    private val places = hashMapOf<String, Place>()

    fun buildPaths(inputFile: String) =
        File(inputFile).readLines().map {
            parsePath(it)
        }

    fun parsePath(path: String) =
        path.split(",").let {
            Path(
                createPlaceIfNotAlreadyCreated(it[0]),
                createPlaceIfNotAlreadyCreated(it[1]), it[2].toInt())
        }

    private fun createPlaceIfNotAlreadyCreated(place: String) =
        if (places[place] != null) {
            places[place]!!
        } else {
            places[place] = Place(place)
            places[place]!!
        }
}