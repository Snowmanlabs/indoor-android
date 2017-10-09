package com.snowmanlabs.indoor.model

/**
 * Created by diefferson on 06/10/17.
 */

class POI(builder : Builder) {

     var id: Int? = null
     var name: String?= null
     var latitude: Double? = null
     var longitude: Double? = null
     var description: String? = null

    init {
        this.id = builder.id
        this.name = builder.name
        this.latitude = builder.latitude
        this.longitude = builder.longitude
        this.description = builder.description
    }

    class Builder{

        var id: Int?= null
        var name: String?= null
        var latitude: Double? = null
        var longitude: Double? = null
        var description: String? = null


        fun id(id:Int): Builder{
            this.id = id
            return this
        }

        fun name(name:String): Builder{
            this.name = name
            return this
        }

        fun latitude(latitude:Double): Builder{
            this.latitude = latitude
            return this
        }

        fun longitude(longitude:Double): Builder{
            this.longitude = longitude
            return this
        }

        fun description(description:String): Builder{
            this.description = description
            return this
        }


        fun build(): POI{
            if(this.id == null) throw IllegalStateException("id == null")
            if(this.name == null) throw IllegalStateException("name == null")
            if(this.latitude == null) throw IllegalStateException("latitude == null")
            if(this.longitude == null) throw IllegalStateException("longitude == null")

            return POI(this)
        }

    }




}
