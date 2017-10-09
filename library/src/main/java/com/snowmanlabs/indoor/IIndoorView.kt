package com.snowmanlabs.indoor

import com.snowmanlabs.indoor.domain.Position

/**
 * Created by diefferson on 06/10/17.
 */
interface IIndoorView {

    fun setMyPosition(position: Position)
    fun selectFloor(floor:Int)

}
