package com.alan.administrator.test.permission

/**
 * Created by alan on  2019/4/4 17:40
 */
interface PermissiomCallBack {


    interface CallBack {
        fun onGranded()
        fun onDenied()
    }

    interface EachCallBack {
        fun onResult(permission: Permission)
    }

}