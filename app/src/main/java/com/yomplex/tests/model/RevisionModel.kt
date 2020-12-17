package com.yomplex.tests.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RevisionModel : Serializable {
    @SerializedName("rid")
    @Expose
    var rId: String? = null
    @SerializedName("pdf")
    @Expose
    var pdfLink: String? = null
    @SerializedName("thumbnail")
    @Expose
    var imageLink: String? = null
    @SerializedName("shortdescription")
    @Expose
    var shortDescription: String? = null
    @SerializedName("tags")
    @Expose
    var tags: String? = null
    @SerializedName("timetoread")
    @Expose
    var timeToRead: String? = null
    @SerializedName("title")
    @Expose
    var tilte: String? = null
    @SerializedName("filename")
    @Expose
    var filename: String? = null
    @SerializedName("pdfversion")
    @Expose
    var pdfversion: String? = null
    @SerializedName("sortorder")
    @Expose
    var sortorder: String? = null
    var documentid: String? = null

    @SerializedName("sno")
    @Expose
    var sno: String? = null

    override fun toString(): String {
        return "RevisionModel(rId=$rId, pdfLink=$pdfLink, imageLink=$imageLink, shortDescription=$shortDescription, tags=$tags, timeToRead=$timeToRead, tilte=$tilte)"
    }

}
