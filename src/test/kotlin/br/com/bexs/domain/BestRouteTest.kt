package br.com.bexs.domain

import br.com.bexs.domain.entities.Path
import br.com.bexs.domain.entities.Place
import br.com.bexs.domain.exception.NoPathException
import br.com.bexs.domain.exception.RouteAlreadyExistsException
import org.junit.Test

class BestRouteTest {
    private val placeA = Place("A")
    private val placeB = Place("B")
    private val placeC = Place("C")

    private val paths = listOf(
        Path(placeA, placeB, 100),
        Path(placeA, placeC, 10),
        Path(placeC, placeB, 20)
    )

    @Test
    fun `should answer with the shortest path between two points`() {
        val bestRoute = BestRoute(paths)

        var shortestPath = bestRoute.getShortestPath(source = placeA, target = placeB)
        assert(shortestPath.toString() == "A - C - B > $30")

        shortestPath = bestRoute.getShortestPath(source = placeA, target = placeC)
        assert(shortestPath.toString() == "A - C > $10")

        shortestPath = bestRoute.getShortestPath(source = placeC, target = placeB)
        assert(shortestPath.toString() == "C - B > $20")
    }

    @Test
    fun `should answer with the shortest path between two points - provided examples`() {
        val gru = Place("GRU")
        val brc = Place("BRC")
        val scl = Place("SCL")
        val cdg = Place("CDG")
        val orl = Place("ORL")

        val paths = listOf(
            Path(gru, brc, 10),
            Path(brc, scl, 5),
            Path(gru, cdg, 75),
            Path(gru, scl, 20),
            Path(gru, orl, 56),
            Path(orl, cdg, 5),
            Path(scl, orl, 20)
        )

        val bestRoute = BestRoute(paths)

        var shortestPath = bestRoute.getShortestPath(source = gru, target = cdg)
        assert(shortestPath.toString() == "GRU - BRC - SCL - ORL - CDG > $40")

        shortestPath = bestRoute.getShortestPath(source = brc, target = cdg)
        assert(shortestPath.toString() == "BRC - SCL - ORL - CDG > $30")
    }

    @Test
    fun `should throw exception when there is no path between the two points`() {
        val bestRoute = BestRoute(paths)

        try {
            bestRoute.getShortestPath(source = placeB, target = placeC)
            assert(false)
        } catch (e: NoPathException) {
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }
    }

    @Test
    fun `should add new edge and recalculate the shortest path`() {
        val bestRoute = BestRoute(paths)

        try {
            bestRoute.getShortestPath(source = placeB, target = placeC)
            assert(false)
        } catch (e: NoPathException) {
            assert(true)
        } catch (e: Exception) {
            assert(false)
        }

        bestRoute.addNewPath(Path(placeB, placeC, 1))

        val shortestPath = bestRoute.getShortestPath(source = placeB, target = placeC)
        assert(shortestPath.toString() == "B - C > $1")
    }

    @Test
    fun `should answer with better path when we add a shortest route`() {
        val bestRoute = BestRoute(paths)

        var shortestPath = bestRoute.getShortestPath(source = placeA, target = placeB)
        assert(shortestPath.toString() == "A - C - B > $30")

        // Adding the same  A - B route, but with lower cost
        bestRoute.addNewPath(Path(placeA, placeB, 5))

        shortestPath = bestRoute.getShortestPath(source = placeA, target = placeB)
        assert(shortestPath.toString() == "A - B > $5")
    }

    @Test
    fun `should answer with the same path when we add a longest route which already exists`() {
        val bestRoute = BestRoute(paths)

        var shortestPath = bestRoute.getShortestPath(source = placeA, target = placeB)
        assert(shortestPath.toString() == "A - C - B > $30")

        // Adding the same  A - B route, but with higher cost
        bestRoute.addNewPath(Path(placeA, placeB, 200))

        shortestPath = bestRoute.getShortestPath(source = placeA, target = placeB)
        assert(shortestPath.toString() == "A - C - B > $30")
    }
}