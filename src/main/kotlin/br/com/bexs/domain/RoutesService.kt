package br.com.bexs.domain

import br.com.bexs.domain.entities.Place

class RoutesService {
    fun bestRoute(bestRoute: BestRoute, from: Place, to: Place) = bestRoute.getShortestPath(from, to)
}