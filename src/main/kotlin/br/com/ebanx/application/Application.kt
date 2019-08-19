package br.com.ebanx.application

import br.com.ebanx.application.controller.RoutesController
import br.com.ebanx.domain.BestRoute
import br.com.ebanx.domain.entities.Place
import br.com.ebanx.domain.entities.ShortestPath
import br.com.ebanx.domain.exception.NoPathException
import br.com.ebanx.resources.InputParser
import io.javalin.Javalin
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("please provide the routes file as the first parameter")
        exitProcess(-1)
    }

    val inputFileName = args[0]

    val paths = InputParser.buildPaths(inputFileName)
    val bestRoute = BestRoute(paths)

    startRestServer(bestRoute, inputFileName)

    handleInputs(bestRoute)
}

private fun handleInputs(bestRoute: BestRoute) {
    while (true) {
        print("please enter the route: ")
        val input = readLine()!!.split("-")

        if (input[0].toUpperCase() == "EXIT" && input.size == 1) {
            exitProcess(-1)
        }

        if (input.size != 2) {
            println("invalid input")
            continue
        }

        val shortestPath = try {
            bestRoute.getShortestPath(
                Place(input[0]),
                Place(input[1])
            )
        } catch (e: NoPathException) {
            println(e.message)
            continue
        }

        println(formatResponse(shortestPath))
    }
}

private fun formatResponse(shortestPath: ShortestPath) = ("best route: $shortestPath")

private fun startRestServer(bestRoute: BestRoute, inputFile: String) {
    val app = Javalin.create().start(System.getProperty("port")?.toInt() ?: 7000)

    val controller = RoutesController()

    app.get("/best-route") { ctx ->
        val shortestPath = controller.bestRoute(bestRoute, ctx)

        ctx.result(formatResponse(shortestPath))
    }

    app.post("/register-route") { ctx ->
        controller.registerRoutes(inputFile, bestRoute, ctx)

        ctx.result("route added")
    }
}
