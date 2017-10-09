package com.snowmanlabs.indoor.domain

object DistanceCalculator {

    private val EQUATORIAL_EARTH_RADIUS = 6378.1370
    private val DEG_TO_RAD = Math.PI / 180

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dlong = (lon2 - lon1) * DEG_TO_RAD
        val dlat = (lat2 - lat1) * DEG_TO_RAD
        val a = Math.pow(Math.sin(dlat / 2), 2.0) + Math.cos(lat1 * DEG_TO_RAD) * Math.cos(lat2 * DEG_TO_RAD) * Math.pow(Math.sin(dlong / 2), 2.0)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val d = EQUATORIAL_EARTH_RADIUS * c
        return d * 1000
    }

}
