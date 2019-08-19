package br.com.ebanx.domain

import br.com.ebanx.domain.entities.Place

class RoutesService {
    fun bestRoute(bestRoute: BestRoute, from: Place, to: Place) = bestRoute.getShortestPath(from, to)
}