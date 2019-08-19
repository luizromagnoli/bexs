package br.com.bexs.domain

import br.com.bexs.domain.entities.Path
import br.com.bexs.domain.entities.Place
import br.com.bexs.domain.entities.ShortestPath
import br.com.bexs.domain.exception.NoPathException
import java.util.*

class BestRoute(paths: List<Path>) {
    private val paths: MutableList<Path> = paths.toMutableList()
    private val visitedPlaces = hashSetOf<Place>()
    private val unvisitedPlaces = hashSetOf<Place>()
    private val predecessors = hashMapOf<Place, Place>()
    private val distancesFromSource = hashMapOf<Place, Int>()

    fun getShortestPath(source: Place, target: Place): ShortestPath {
        reset()
        execute(source)

        val path = LinkedList<Place>()
        var step = target

        if (this.predecessors[step] == null) {
            throw NoPathException()
        }

        path.addFirst(step)

        while (this.predecessors[step] != null) {
            step = this.predecessors[step]!!
            path.addFirst(step)
        }

        return ShortestPath(path, distancesFromSource[target]!!)
    }

    fun addNewPath(newPath: Path) {
        paths.add(newPath)

        reset()
    }

    private fun execute(source: Place): HashMap<Place, Place> {
        distancesFromSource[source] = 0
        unvisitedPlaces.add(source)

        while (unvisitedPlaces.size > 0) {
            val node = getMinimum(unvisitedPlaces)

            node?.let {
                visitedPlaces.add(it)
                unvisitedPlaces.remove(it)
                findMinimalDistances(it)
            }
        }

        return predecessors
    }

    private fun findMinimalDistances(node: Place) {
        val adjacentNodes = getNeighbors(node)

        adjacentNodes.forEach { vertex ->
            if (getShortestDistance(vertex) > getShortestDistance(node) + getDistance(node, vertex)) {
                distancesFromSource[vertex] = getShortestDistance(node) + getDistance(node, vertex)
                predecessors[vertex] = node
                unvisitedPlaces.add(vertex)
            }
        }
    }

    private fun getDistance(source: Place, target: Place): Int {
        return paths.filter {edge ->
            edge.source == source && edge.destination == target
        }.minBy {
            it.cost
        }?.cost ?: throw RuntimeException("Should not happen")
    }

    private fun getNeighbors(node: Place): List<Place> {
        val neighbors = arrayListOf<Place>()

        paths.forEach { edge ->
            if (edge.source == node && !isSettled(edge.destination)) {
                neighbors.add(edge.destination)
            }
        }

        return neighbors
    }

    private fun getMinimum(Place: Set<Place>): Place? {
        var minimum: Place? = null

        Place.forEach { vertex ->
            if (minimum == null) {
                minimum = vertex
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex
                }
            }
        }

        return minimum
    }

    private fun isSettled(Place: Place) = visitedPlaces.contains(Place)

    private fun getShortestDistance(destination: Place?) = distancesFromSource[destination] ?: Integer.MAX_VALUE

    private fun reset() {
        visitedPlaces.clear()
        unvisitedPlaces.clear()
        predecessors.clear()
        distancesFromSource.clear()
    }
}