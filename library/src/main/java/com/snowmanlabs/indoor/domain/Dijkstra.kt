package com.snowmanlabs.indoor.domain

import java.util.LinkedList


/**
 * Created by diefferson on 10/10/17.
 **/
object Dijkstra {

    fun calculateShortestPathFromSource(graph: Graph, source: POI): Graph {
        source.distance = 0.0

        val settledNodes = HashSet<POI>()
        val unsettledNodes = HashSet<POI>()

        unsettledNodes.add(source)

        while (unsettledNodes.size != 0) {
            val currentNode = getLowestDistanceNode(unsettledNodes)
            unsettledNodes.remove(currentNode)
            for ((adjacentNode, edgeWeight) in currentNode.adjacentNodes) {
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode)
                    unsettledNodes.add(adjacentNode)
                }
            }
            settledNodes.add(currentNode)
        }
        return graph
    }

    private fun getLowestDistanceNode(unsettledNodes: Set<POI>): POI {
        var lowestDistanceNode: POI? = null
        var lowestDistance = Double.MAX_VALUE
        for (node in unsettledNodes) {
            val nodeDistance = node.distance
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance
                lowestDistanceNode = node
            }
        }
        return lowestDistanceNode!!
    }

    private fun calculateMinimumDistance(evaluationNode: POI, edgeWeigh: Double?, sourceNode: POI) {
        val sourceDistance = sourceNode.distance
        if (sourceDistance + edgeWeigh!! < evaluationNode.distance) {
            evaluationNode.distance = sourceDistance + edgeWeigh
            val shortestPath = LinkedList(sourceNode.shortestPath)
            shortestPath.add(sourceNode)
            evaluationNode.shortestPath = shortestPath
        }
    }

}