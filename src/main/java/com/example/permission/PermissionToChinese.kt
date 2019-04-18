package com.alan.administrator.test.permission

object PermissionToChinese {

    @JvmStatic
    fun toChinese(permission:String):String{
        var str=""
        when(permission){
            "android.permission.ACCESS_FINE_LOCATION" ->{str="位置"}
            "android.permission.CAMERA" ->{str="相机"}
            "android.permission.WRITE_EXTERNAL_STORAGE" ->{str="存储"}
            "android.permission.READ_EXTERNAL_STORAGE" ->{str="存储"}
            "android.permission.READ_PHONE_STATE" ->{str="电话"}
            "android.permission.ACCESS_NETWORK_STATE" ->{str="位置"}
            "android.permission.ACCESS_WIFI_STATE" ->{str="wify"}
         else -> str= "部分"
        }
        return str
    }



}