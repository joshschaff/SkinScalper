package restAPI.opSkinsAPI

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import org.apache.http.client.fluent.Request
import org.apache.http.client.utils.URIBuilder
import restAPI.APIRequestBuilder
import restAPI.convertArrayToURL
import java.net.URI



sealed class OPSkinsRequestBuilder : APIRequestBuilder(){


    override val scheme = "https"
    override val host = "api.opskins.com"


    // This is a base path used by all endpoints in a certain interface
    override abstract val path : String

    // protected keyword means subclasses can access it, but unrelated classes cannot

    override fun getUri(pathAdd: String, vararg params : Pair<String,String>) : URI {
        val builder = URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPath(path + pathAdd)
                .setParameter("appid", "730")
                // https://stackoverflow.com/questions/42286346/api-http-opskins-com-send-paramether
                .setParameter("key", opSkinsApiKey)
        for (p in params) {
            //builder.setParameter(p.first, p.second)
            builder.setParameter(p.first, p.second)
        }
        return  builder.build()
    }


    class CashoutInterface : OPSkinsRequestBuilder() {
        override val path = "/ICashout"
    }

    class PricingInterface : OPSkinsRequestBuilder() {
        override val path = "/IPricing"

        fun getAllLowestListPrices() : Map<String, LowestListPrice> {
            val json = Request.Get(getUri("/GetAllLowestListPrices/v1")).execute().returnContent().asString()
            val deserializer = LowestListPrice.getJsonDeserializer()
            val customGsonBuilder = GsonBuilder().registerTypeAdapter(object : TypeToken<Map<String, LowestListPrice>>(){}.type, deserializer).create()
            val jsonElement = Gson().fromJson(json, JsonElement::class.javaObjectType).asJsonObject.get("response").asJsonObject
            return customGsonBuilder.fromJson(jsonElement, object : TypeToken<Map<String, LowestListPrice>>(){}.type)
        }

        fun getSuggestedPricesV1(vararg item : String) : Map<String, SuggestedPricesV1> {
            val json = Request.Get(getUri("/GetSuggestedPrices/v1", *convertArrayToURL("items",item))).execute().returnContent().asString()
            val deserializer = SuggestedPricesV1.getJsonDeserializer()
            val customGsonBuilder = GsonBuilder().registerTypeAdapter(object : TypeToken<Map<String, SuggestedPricesV1>>(){}.type, deserializer).create()
            println(json)
            val jsonElement = Gson().fromJson(json, JsonElement::class.javaObjectType).asJsonObject.get("response").asJsonObject.get("prices").asJsonObject
            return customGsonBuilder.fromJson(jsonElement, object : TypeToken<Map<String, SuggestedPricesV1>>(){}.type)
        }


        //fun getPriceListV2() : Map<String, >
    }

    class PaymentsInterface : OPSkinsRequestBuilder(){
        override val path = "/IPayments"



    }

    class SalesInterface {
        val path = "/ISales"

    }
}