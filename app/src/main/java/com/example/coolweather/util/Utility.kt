package com.example.coolweather.util

import android.text.TextUtils
import com.google.gson.JsonArray
import org.json.JSONArray
import java.lang.Exception
import com.example.coolweather.db.Province
import kotlin.contracts.ReturnsNotNull
import com.example.coolweather.db.City
import com.example.coolweather.db.County
import java.security.KeyStore


class Utility {
    companion object {
        fun handleProvinceResponse(response: String): Boolean {
            if (!TextUtils.isEmpty(response)) {
                try {
                    var jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        val province = Province()
                        province.provinceName = jsonObject.getString("name")
                        province.provinceCode = jsonObject.getInt("id")
                        province.save()
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false
        }

        fun handleCityResponse(response: String, provinceId: Int): Boolean {
            if (!TextUtils.isEmpty(response)) {
                try {
                    var jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        val city = City()
                        city.cityName = jsonObject.getString("name")
                        city.cityCode = jsonObject.getInt("id")
                        city.provinceId = provinceId
                        city.save()
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false
        }

        fun handleCountyResponse(response: String, cityId: Int): Boolean {
            if (!TextUtils.isEmpty(response)) {
                try {
                    var jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        var jsonObject = jsonArray.getJSONObject(i)
                        var county = County()
                        county.countyName = jsonObject.getString("name")
                        county.weatherId = jsonObject.getString("weather_id")
                        county.cityId = cityId
                        county.save();
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false
        }
    }
}