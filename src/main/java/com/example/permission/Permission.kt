package com.alan.administrator.test.permission

/**
 * Created by alan on  2019/4/4 17:43
 */
class Permission {
    var name = ""
    var isGranded = false

    constructor(name: String, isGranded: Boolean) {
        this.name = name
        this.isGranded = isGranded
    }

    constructor()
}