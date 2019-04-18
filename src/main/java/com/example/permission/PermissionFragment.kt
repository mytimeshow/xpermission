package com.alan.administrator.test.permission

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.util.SparseArray
import java.util.*

import kotlin.collections.ArrayList

/**
 * Created by alan on  2019/4/4 14:50
 */
@SuppressLint("CheckResult", "NewApi")
class PermissionFragment : Fragment() {

    private val callBacks = SparseArray<PermissiomCallBack.CallBack>()
    private val eachCallBacks = SparseArray<PermissiomCallBack.EachCallBack>()
    private var pers: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

    }


    /**
     * 一次性请求多个权限并且全部被授权才会回调ongranded
     */
    fun requestPermissons(permission: Array<String>, callback: PermissiomCallBack.CallBack) {
        pers = permission
        var permissions = filter(permission).toTypedArray()
        if (permissions.isEmpty()) return
        if (isNeverAsk(permissions)) {
            showOpenPermissionSettingDialog(permissions)
            return
        }
        val requestCode: Int = generateCode()
        callBacks.put(requestCode, callback)
        requestPermissions(permissions, requestCode)
    }


    /*
    * 每个权限的请求结果都会被封装到permission类中并且回调到callback的回调方法中
    * */
    fun requestEachPermission(permission: Array<String>, callback: PermissiomCallBack.EachCallBack) {
        pers = permission
        var permissions = filter(permission).toTypedArray()
        if (permissions.isEmpty()) return
        if (isNeverAsk(permissions)) {
            showOpenPermissionSettingDialog(permissions)
            return
        }
        val requestCode: Int = generateCode()
        eachCallBacks.put(requestCode, callback)
        requestPermissions(permissions, requestCode)
    }


    /*
    * 弹出打开被禁掉切不再询问的权限的对话框
    * */
    private fun showOpenPermissionSettingDialog(permission: Array<String>) {
        SettingDialog.getInstance().setContent("需要打开${PermissionToChinese.toChinese(permission[0])}权限才可以使用该功能").setOnClickListener(object : SettingDialog.OnClickListener {
            override fun onPositive() {
                jumbToPermissionSetting()
            }

        }).show(activity!!.supportFragmentManager, "SettingDialog")
    }


    /*
    * 去权限设置页面
    * */
    private fun jumbToPermissionSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 1001)
    }


    /*
    * 权限是否被设置了不再询问
    * */
    private fun isNeverAsk(permissions: Array<String>): Boolean {
        var isNever = false
        for (per in permissions) {
            if (!activity!!.shouldShowRequestPermissionRationale(per)) {
                isNever = true
                break
            }
        }
        return isNever
    }


    /**
     * 生成随机的requestcode
     */
    private fun generateCode(): Int {
        //生成五位随机数
        var code: Int = Random().nextInt(10000)
        var times = 0
        do {
            times++
            if (callBacks.get(code) == null) break
        } while (10 > times)
        return code
    }


    /**
     * 每个权限单独回调到callback中
     */
    private fun handleEachPermission(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var callBack = eachCallBacks.get(requestCode)
        if (callBack == null) return
        eachCallBacks.remove(requestCode)
        val permission = Permission()

        //android版本小于6.0
        if (Build.VERSION.SDK_INT < M) {
            allGranded(callBack, permissions)
            return
        }
        for ((index, e) in grantResults.withIndex()) {
            permission.name = permissions[index].substring(permissions[index].lastIndexOf(".") + 1)
            permission.isGranded = if (e == PackageManager.PERMISSION_DENIED) false else true
            callBack.onResult(permission)

        }
    }


    /*
    * 权限全部被授权
    * */
    private fun allGranded(callBack: PermissiomCallBack.EachCallBack, permissions: Array<out String>) {
        permissions.forEach { s ->
            callBack.onResult(Permission(s.substring(s.lastIndexOf(".") + 1), true))
        }

    }


    /**
     * 全部申请结果通过回调ongranded   否则  回调ondenied
     */
    private fun hadlePermissionCallBack(requestCode: Int, grantResults: IntArray) {
        val callBack = callBacks.get(requestCode)
        if (callBack == null) return
        callBacks.remove(requestCode)
        if (Build.VERSION.SDK_INT < M) {
            callBack.onGranded()
            return
        }


        grantResults.forEach { i ->
            if (i != PackageManager.PERMISSION_GRANTED) {
                callBack.onDenied()
                return
            }
        }

        callBack.onGranded()

    }


    /**
     * 过滤掉已经被授权的权限
     */
    fun filter(permissions: Array<String>): ArrayList<String> {
        var pers = ArrayList<String>()
        permissions.forEach { s ->
            if (activity?.checkSelfPermission(s) != PackageManager.PERMISSION_GRANTED) pers.add(s)
        }

        return pers
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        hadlePermissionCallBack(requestCode, grantResults)
        handleEachPermission(requestCode, permissions, grantResults);
    }


}