package com.alan.administrator.test.permission

import android.support.v7.app.AppCompatActivity
import com.example.permission.AppManager

/**
 * Created by alan on  2019/4/4 17:02
 */

object PermissionHelper {

    val FRAGMENT_TAG = "PermissionFragment"


    /**
     * 一次性请求权限
     */
    @JvmStatic
    fun requestPermissions(pers: Array<String>, callback: PermissiomCallBack.CallBack) {
        val fragmentManage = (AppManager.instance.getTopActivity() as AppCompatActivity).supportFragmentManager
        var fragment = fragmentManage.findFragmentByTag(FRAGMENT_TAG)
        if (fragment == null) {
            fragment = PermissionFragment()
            fragmentManage.beginTransaction().add(fragment, FRAGMENT_TAG).commit()
            fragmentManage.executePendingTransactions()
        }
        fragment as PermissionFragment
        fragment.requestPermissons(pers, callback)
    }


    /**
     * 别分返回每个权限的请求结果
     */
    @JvmStatic
    fun requestEachPermissions(permissions: Array<String>, callback: PermissiomCallBack.EachCallBack) {
        val fragmentManage = (AppManager.instance.getTopActivity() as AppCompatActivity).supportFragmentManager
        var fragment = fragmentManage.findFragmentByTag(FRAGMENT_TAG)
        if (fragment == null) {
            fragment = PermissionFragment()
            fragmentManage.beginTransaction().add(fragment, FRAGMENT_TAG).commit()
            fragmentManage.executePendingTransactions()
        }
        fragment as PermissionFragment
        fragment.requestEachPermission(permissions, callback)
    }


}
