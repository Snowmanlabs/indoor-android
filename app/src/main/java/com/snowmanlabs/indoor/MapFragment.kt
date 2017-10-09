package com.snowmanlabs.indoor

import android.graphics.BitmapFactory
import com.snowmanlabs.com.R
import com.snowmanlabs.indoor.model.Connector
import com.snowmanlabs.indoor.model.Floor
import com.snowmanlabs.indoor.model.POI

/**
 * Created by diefferson on 09/10/17.
 **/
class MapFragment : IndoorFragment(){

    override fun getFloors(): ArrayList<Floor> {
        var floors = ArrayList<Floor>()
        floors.add(
                Floor.Builder()
                        .number(0)
                        .name("Piso 1")
                        .description("Teste")
                        .latitude(-25.456030217451996)
                        .longitude(-49.2753406921695)
                        .height(55.320877f)
                        .width(20.97941f)
                        .bearing(22.5f)
                        .image(BitmapFactory.decodeResource(context.resources, R.drawable.planta))
                        .build()
        )
        return  floors;
    }

    override fun getPois(): List<POI> {
        var pois = ArrayList<POI>()
//        pois.add(
//                POI.Builder()
//                    .id(2)
//                    .name("name")
//                    .description("teste")
//                    .latitude(0.0)
//                    .longitude(0.0)
//                    .build()
        // )
        return  pois;
    }

    override fun getConnectors(): List<Connector> {
        var connectors = ArrayList<Connector>()
//        connectors.add(Connector(0, 0.0, 0.0))
        return  connectors
    }

    companion object {
        fun newInstance(): MapFragment = MapFragment()

    }
}