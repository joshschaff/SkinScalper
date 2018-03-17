package restAPI

import org.apache.http.client.utils.URIBuilder
import java.net.URI

open abstract class APIRequestBuilder {
    protected open val scheme : String = "https"
    protected open abstract val host : String
    protected open abstract val path : String
    protected val appid = "730"

    companion object {
        val opSkinsApiKey = "be1c7d27835e4b13cb19377ef005ff"
    }

    protected open abstract fun getUri(pathAdd: String, vararg params : Pair<String,String>) : URI
}