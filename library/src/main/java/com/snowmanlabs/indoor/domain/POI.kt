package com.snowmanlabs.indoor.domain

import java.util.LinkedList

/**
 * Created by diefferson on 06/10/17.
 */

class POI(builder : Builder) {

    companion object {
        val POI_TYPE = "POI"
        val CONNECTOR_TYPE = "CONNECTOR"
    }

    var id: Int? = null
    var type:String?= null
    var floorNumber:Int? = null
    var name: String?= null
    var latitude: Double? = null
    var longitude: Double? = null
    var description: String? = null

    //NODE ATRIBUTES
    var shortestPath = LinkedList<POI>()
    var distance = Double.MAX_VALUE
    var adjacentNodes: MutableMap<POI, Double> = HashMap()

    fun addDestination(destination: POI) {
        adjacentNodes.put(destination, DistanceCalculator.distance(this.latitude!!, this.longitude!!, destination.latitude!!, destination.longitude!!))
    }

    override fun toString(): String {
        return this.name!!
    }

    init {
        this.id = builder.id
        this.type = builder.type
        this.floorNumber = builder.floorNumber
        this.name = builder.name
        this.latitude = builder.latitude
        this.longitude = builder.longitude
        this.description = builder.description
    }

    class Builder{

        var id: Int?= null
        var type:String?= null
        var floorNumber:Int? = null
        var name: String?= null
        var latitude: Double? = null
        var longitude: Double? = null
        var description: String? = null

        fun type(type:String): Builder {
            if(type != POI_TYPE && type != CONNECTOR_TYPE){
                throw IllegalStateException("type invalid, only accept POI or CONNECTOR")
            }
            this.type = type
            return this
        }

        fun id(id:Int): Builder {
            this.id = id
            return this
        }

        fun floorNumber(floorNumber:Int): Builder {
            this.floorNumber = floorNumber
            return this
        }

        fun name(name:String): Builder {
            this.name = name
            return this
        }

        fun latitude(latitude:Double): Builder {
            this.latitude = latitude
            return this
        }

        fun longitude(longitude:Double): Builder {
            this.longitude = longitude
            return this
        }

        fun description(description:String): Builder {
            this.description = description
            return this
        }

        fun build(): POI {
            if(this.id == null) throw IllegalStateException("id == null")
            if(this.type == null) throw IllegalStateException("type == null")
            //TODO: if(this.floorNumber == null) throw IllegalStateException("floorNumber == null")
            if(this.name == null) throw IllegalStateException("name == null")
            if(this.latitude == null) throw IllegalStateException("latitude == null")
            if(this.longitude == null) throw IllegalStateException("longitude == null")

            return POI(this)
        }
    }
}
