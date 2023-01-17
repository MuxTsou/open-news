package ai.nextstop.opennews.models.local

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

import java.util.*

@RealmClass
open class Article : RealmObject() {
    @PrimaryKey
    var url: String = ""
    var author: String? = null
    var title: String = ""
    var description: String? = null
    @SerializedName("urlToImage")
    var coverUrl: String? = null
    var publishedAt: Date? = null
    var content: String? = null
    var page: Int = 0
}