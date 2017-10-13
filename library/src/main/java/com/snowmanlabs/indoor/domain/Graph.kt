package com.snowmanlabs.indoor.domain

import java.util.HashSet

/**
 * Created by diefferson on 13/10/17.
 */
class Graph {

    var nodes = HashSet<POI>()

    fun addNode(nodeA: POI) {
        nodes.add(nodeA)
    }
}