/*
 * Copyright 2015 - 2017 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.snowmanlabs.indoor.domain

import android.location.Location

import java.util.Date

class Position {

    constructor() {}

    constructor(deviceId: String, location: Location, battery: Double) {
        time = Date(location.time)
        latitude = location.latitude
        longitude = location.longitude
        altitude = location.altitude
    }

    var id: Long = 0

    var time: Date? = null

    var latitude: Double = 0.toDouble()

    var longitude: Double = 0.toDouble()

    var altitude: Double = 0.toDouble()

}
