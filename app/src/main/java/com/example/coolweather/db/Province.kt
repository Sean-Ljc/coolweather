package com.example.coolweather.db

import org.litepal.crud.DataSupport

class Province : DataSupport() {
    var id: Int? = null
    var provinceName: String? = null
    var provinceCode: Int? = null
}

//data class Province(var provinceName: String, var provinceCode: String = "") : DataSupport() {
//    constructor(id: Int, provinceName: String, provinceCode: String) : this(provinceName, provinceCode)
//}