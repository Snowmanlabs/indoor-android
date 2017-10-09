package com.snowmanlabs.indoor.model


import android.graphics.Bitmap
import java.io.Serializable

/**
 * Created by diefferson on 06/10/17.
 */

class Floor(builder: Builder) : Serializable{

    var number: Int? = null
    var name: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var width: Float ? = null
    var height: Float ? = null
    var bearing: Float ? = null
    var description: String? = null
    var image: Bitmap? = null

    init {
        this.number = builder.number
        this.name = builder.name
        this.latitude = builder.latitude
        this.longitude = builder.longitude
        this.width = builder.width
        this.height = builder.height
        this.bearing = builder.bearing
        this.description = builder.description
        this.image = builder.image
    }


    class Builder{
        var number: Int? = null
        var name: String? = null
        var latitude: Double? = null
        var longitude: Double? = null
        var width: Float ? = null
        var height: Float ? = null
        var bearing: Float ? = null
        var description: String? = null
        var image: Bitmap? = null


        fun number(number:Int): Builder{
            this.number = number
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

        fun width(width:Float): Builder{
            this.width = width
            return this
        }

        fun height(height:Float): Builder{
            this.height = height
            return this
        }

        fun bearing(bearing:Float): Builder{
            this.bearing = bearing
            return this
        }

        fun description(description:String): Builder{
            this.description = description
            return this
        }

        fun image(image: Bitmap): Builder{
            this.image = image
            return this
        }

        fun build() : Floor{

            if(this.number == null) throw IllegalStateException("number == null")
            if(this.name == null) throw IllegalStateException("name == null")
            if(this.latitude == null) throw IllegalStateException("latitude == null")
            if(this.longitude == null) throw IllegalStateException("longitude == null")
            if(this.width == null) throw IllegalStateException("width == null")
            if(this.height == null) throw IllegalStateException("height == null")
            if(this.image == null) throw IllegalStateException("image == null")

            return Floor(this)
        }
    }
}
