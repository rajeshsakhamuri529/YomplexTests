package com.yomplex.tests.model

class Quiz {

    var appversion: Int ?= null
    var completed: Boolean ?= null
    var courseId: String ?= null
    var courseName: String ?= null
    var os: String = ""
    var quizSession: Map<String, Bank> ?= null
    var quizSessionId: String ?= null
    var quit: Boolean ?= null

    /*fun toMap(type: String, bank: Bank): Map<String, Bank> {

        val result = HashMap<String, Bank>()
        result.put(type, bank)

        return result
    }*/
    var quizType: String ?= null
    var result: Boolean ?= null
    var timeStamp: Long ?= null
    var timeTaken: Int ?= null
    var topicId: String ?= null
    var topicName: String?= null
    var uId: String?= null
    var osv: Int?= null
}