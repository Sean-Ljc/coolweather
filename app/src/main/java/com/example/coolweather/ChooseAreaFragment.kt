package com.example.coolweather

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.coolweather.db.City
import com.example.coolweather.db.County
import com.example.coolweather.db.Province
import kotlinx.android.synthetic.main.choose_area.*
import org.litepal.crud.DataSupport
import com.example.coolweather.util.HttpUtil
import com.example.coolweather.util.Utility
import com.google.gson.annotations.Until
import kotlinx.android.synthetic.main.choose_area.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.nio.channels.InterruptedByTimeoutException

class ChooseAreaFragment : Fragment() {

    companion object {
        val LEVEL_PROVINCE: Int = 0
        val LEVEL_CITY: Int = 1
        val LEVEL_COUNTY: Int = 2
    }

    var progressDialog: ProgressDialog? = null
    var adapter: ArrayAdapter<String>? = null
    var dataList: MutableList<String> = mutableListOf()
    var provinceList: MutableList<Province>? = null
    var cityList: MutableList<City>? = null
    var countyList: MutableList<County>? = null
    var selectedProvince: Province? = null
    var selectedCity: City? = null
    var currentLeve: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = layoutInflater.inflate(R.layout.choose_area, container, false)
        adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, dataList)
        view.lvArea.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lvArea.setOnItemClickListener { adapterView, view, i, l ->
            if (currentLeve == LEVEL_PROVINCE) {
                selectedProvince = provinceList?.get(i)
                queryCities()
            } else if (currentLeve == LEVEL_CITY) {
                selectedCity = cityList?.get(i)
                queryCounties()
            }

        }

        btnBack.setOnClickListener {
            if (currentLeve == LEVEL_COUNTY) {
                queryCities()
            } else if (currentLeve == LEVEL_CITY) {
                queryProvinces()
            }
        }
        queryProvinces()
    }

    private fun queryProvinces() {
        tvTitle.text = "中国"
        btnBack.visibility = View.GONE
        provinceList = DataSupport.findAll(Province::class.java)

        if (provinceList?.size!! > 0) {
            dataList.clear()
            for (province in provinceList!!) {
                dataList.add(province.provinceName!!)
            }
            adapter?.notifyDataSetChanged()
            lvArea.setSelection(0)
            currentLeve = LEVEL_PROVINCE
        } else {
            var address = "http://guolin.tech/api/china"
            queryFromServer(address, "province")
        }

    }

    private fun queryCities() {
        tvTitle.text = selectedProvince?.provinceName
        btnBack.visibility = View.VISIBLE
        cityList = DataSupport.where("provinceid=?", selectedProvince?.id.toString()).find(City::class.java)
        if (cityList?.size!! > 0) {
            dataList.clear()
            for (city in cityList!!) {
                dataList.add(city.cityName!!)
            }
            adapter?.notifyDataSetChanged()
            lvArea.setSelection(0)
            currentLeve = LEVEL_CITY
        } else {
            var provinceCode = selectedProvince?.id
            var address = "http://guolin.tech/api/china/" + provinceCode
            queryFromServer(address, "city")
        }
    }

    private fun queryCounties() {
        tvTitle.text = selectedCity?.cityName
        btnBack.visibility = View.VISIBLE
        countyList = DataSupport.where("cityid=?", selectedCity?.id.toString()).find(County::class.java)
        if (countyList?.size!! > 0) {
            dataList.clear()
            for (county in countyList!!) {
                dataList.add(county.countyName!!)
            }
            adapter?.notifyDataSetChanged()
            lvArea.setSelection(0)
            currentLeve = LEVEL_COUNTY
        } else {
            var provinceCode = selectedProvince?.provinceCode
            var cityCode = selectedCity?.cityCode
            var address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode
            queryFromServer(address, "county")
        }
    }

    private fun queryFromServer(address: String, s: String) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                activity?.runOnUiThread {
                    closeProgressDialog()
                    Toast.makeText(activity, "加载失败", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                var responseTest = response?.body()?.string()
                var result = false
                if ("province".equals(s)) {
                    result = Utility.handleProvinceResponse(responseTest!!)
                } else if ("city".equals(s)) {
                    result = Utility.handleCityResponse(responseTest!!, selectedProvince?.id!!)
                } else if ("county".equals(s)) {
                    result = Utility.handleCountyResponse(responseTest!!, selectedCity?.id!!)
                }

                if (result) {
                    activity?.runOnUiThread {
                        closeProgressDialog()
                        if ("province".equals(s)) {
                            queryProvinces()
                        } else if ("city".equals(s)) {
                            queryCities()
                        } else if ("county".equals(s)) {
                            queryCounties()
                        }
                    }
                }
            }
        })
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(activity, "加载数据", "正在加载...", false, false)
        }
    }

    private fun closeProgressDialog() {
        progressDialog?.dismiss()
    }
}

