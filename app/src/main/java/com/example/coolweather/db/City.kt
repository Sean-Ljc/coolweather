package com.example.coolweather.db

import org.litepal.crud.DataSupport


class City : DataSupport() {
    var id: Int? = null
    var cityName: String? = null
    var cityCode: Int? = null
    var provinceId: Int? = null
}
//data class City(var cityName: String, var cityCode: String = "", var provinceId: Int) : DataSupport() {
//    constructor(id: Int, cityName: String, cityCode: String, provinceId: Int) : this(cityName, cityCode, provinceId)
//}