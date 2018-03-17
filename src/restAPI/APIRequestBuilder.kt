package restAPI

open abstract class APIRequestBuilder {
    protected open val scheme : String = "https"
    protected open abstract val host : String
    protected open abstract val path : String

    companion object {
        val opSkinsApiKey = "be1c7d27835e4b13cb19377ef005ff"
    }
}