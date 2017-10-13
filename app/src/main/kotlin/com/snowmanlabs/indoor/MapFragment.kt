package com.snowmanlabs.indoor

import android.graphics.BitmapFactory
import com.snowmanlabs.com.R
import com.snowmanlabs.indoor.domain.Floor
import com.snowmanlabs.indoor.domain.POI
import kotlin.collections.ArrayList

/**
 * Created by diefferson on 09/10/17.
 **/
class MapFragment : IndoorFragment() {

    override fun getFloors(): ArrayList<Floor> {
        var floors = ArrayList<Floor>()
        floors.add(
                Floor.Builder()
                        .number(0)
                        .name("Piso 1")
                        .description("Teste")
                        //.latitude(-25.456030217451996)
                        .latitude(-25.456025)
                        //.longitude(-49.2753406921695)
                        .longitude(-49.275349)
                        .height(55.320877f)
                        .width(20.97941f)
                        .bearing(22.5f)
                        .image("https://i.imgur.com/S4s5QIP.png")
                        .build()
        )
        return floors;
    }

    override fun getPois(): List<POI> {

        val poi1 = POI.Builder().id(1).name("varanda").type(POI.POI_TYPE).description("teste").latitude(-25.455839).longitude(-49.275259).build()
        val poi2 = POI.Builder().id(2).name("recepcao").type(POI.POI_TYPE).description("teste").latitude(-25.455858).longitude(-49.275229).build()
        val poi3 = POI.Builder().id(3).name("porta lab").type(POI.CONNECTOR_TYPE).description("teste").latitude(-25.455871).longitude(-49.275234).build()
        val poi4 = POI.Builder().id(4).name("janela lab").type(POI.POI_TYPE).description("teste").latitude(-25.455857).longitude(-49.275276).build()
        val poi5 = POI.Builder().id(5).name("banheiro lab").type(POI.POI_TYPE).description("teste").latitude(-25.455879).longitude(-49.275202).build()
        val poi6 = POI.Builder().id(6).name("lab").type(POI.POI_TYPE).description("teste").latitude(-25.455875).longitude(-49.275255).build()
        val poi7 = POI.Builder().id(7).name("canto lab 1").type(POI.POI_TYPE).description("teste").latitude(-25.455897).longitude(-49.275297).build()
        val poi8 = POI.Builder().id(8).name("canto lab 2").type(POI.POI_TYPE).description("teste").latitude(-25.455921).longitude(-49.275223).build()
        val poi9 = POI.Builder().id(9).name("porta lab 2").type(POI.CONNECTOR_TYPE).description("teste").latitude(-25.455923).longitude(  -49.275269).build()
        val poi10 = POI.Builder().id(10).name("reuniao 1").type(POI.POI_TYPE).description("teste").latitude(-25.455937).longitude(-49.275275).build()
        val poi11 = POI.Builder().id(11).name("reuniao 2").type(POI.CONNECTOR_TYPE).description("teste").latitude(-25.455956).longitude(-49.275283).build()
        val poi12 = POI.Builder().id(12).name("copa").type(POI.POI_TYPE).description("teste").latitude(-25.455965).longitude(-49.275256).build()
        val poi13 = POI.Builder().id(13).name("cozinha").type(POI.POI_TYPE).description("teste").latitude(-25.455940).longitude(-49.275236).build()


        //INITIALIZE CONNECTIONS
        poi1.addDestination(poi2)

        poi2.addDestination(poi1)
        poi2.addDestination(poi3)

        poi3.addDestination(poi2)
        poi3.addDestination(poi4)
        poi3.addDestination(poi5)
        poi3.addDestination(poi6)

        poi4.addDestination(poi3)

        poi5.addDestination(poi3)
        poi5.addDestination(poi8)

        poi6.addDestination(poi3)
        poi6.addDestination(poi7)
        poi6.addDestination(poi9)

        poi7.addDestination(poi6)
        poi7.addDestination(poi9)

        poi8.addDestination(poi5)
        poi8.addDestination(poi9)

        poi9.addDestination(poi6)
        poi9.addDestination(poi7)
        poi9.addDestination(poi8)
        poi9.addDestination(poi10)

        poi10.addDestination(poi9)
        poi10.addDestination(poi11)

        poi11.addDestination(poi10)
        poi11.addDestination(poi12)

        poi12.addDestination(poi11)
        poi12.addDestination(poi13)

        poi13.addDestination(poi12)

        val pois = ArrayList<POI>()
        pois.add(poi1)
        pois.add(poi2)
        pois.add(poi3)
        pois.add(poi4)
        pois.add(poi5)
        pois.add(poi6)
        pois.add(poi7)
        pois.add(poi8)
        pois.add(poi9)
        pois.add(poi10)
        pois.add(poi11)
        pois.add(poi12)
        pois.add(poi13)
        return pois
    }

    companion object {
        fun newInstance(): MapFragment = MapFragment()
    }
}