package com.yomplex.tests.model

import java.io.Serializable

class OptionsWithAnswer : Serializable{

    var option: Int ?= null
    var istrue: Boolean ?= null

    override fun toString(): String {
        return "OptionsWithAnswer(option=$option, istrue=$istrue)"
    }

}