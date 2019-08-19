package br.com.ebanx.application.controller

import br.com.ebanx.domain.BestRoute
import br.com.ebanx.resources.InputParser
import br.com.ebanx.domain.RoutesService
import br.com.ebanx.domain.entities.ShortestPath
import br.com.ebanx.domain.entities.Place
import br.com.ebanx.domain.exception.NoPathException
import br.com.ebanx.domain.exception.RouteAlreadyExistsException
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import java.io.File

class RoutesController {
    private val service = RoutesService()

    fun bestRoute(bestRoute: BestRoute, ctx: Context): ShortestPath {
        val from = ctx.queryParam("de") ?: throw BadRequestResponse("Invalid parameters")
        val to = ctx.queryParam("para") ?: throw BadRequestResponse("Invalid parameters")

        return try {
            service.bestRoute(bestRoute, Place(from), Place(to))
        } catch (e: NoPathException) {
            throw BadRequestResponse(e.message!!)
        }
    }

    fun registerRoutes(inputFile: String, bestRoute: BestRoute, ctx: Context) {
        val newRoute = ctx.body()

        try {
            bestRoute.addNewPath(InputParser.parsePath(newRoute))
            File(inputFile).appendText(newRoute + "\n")
        } catch (e: RouteAlreadyExistsException) {
            throw BadRequestResponse(e.message!!)
        }
    }
}