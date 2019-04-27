package com.example.coolweather.db

import org.litepal.crud.DataSupport

class County : DataSupport() {
    var id: Int? = null
    var countyName: String? = null
    var weatherId: String? = null
    var cityId: Int? = null
}

//data class County(var countyName: String, var weatherId: Int, var cityId: Int) : DataSupport() {
//    constructor(id: Int, countyName: String, weatherId: Int, cityId: Int) : this(countyName, weatherId, cityId)
//}