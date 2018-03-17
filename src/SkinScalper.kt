import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.*
import org.apache.http.client.fluent.Request
import restAPI.opSkinsAPI.OPSkinsRequestBuilder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import restAPI.opSkinsAPI.LowestListPrice
import restAPI.opSkinsAPI.SuggestedPricesV1
import java.util.LinkedHashMap
import java.util.logging.Level
import java.util.stream.IntStream
import kotlin.io.println


fun meh(args: Array<String>) {
    //restAPI.getBuyRequestPrice("AK-47 | Redline (Field-Tested)")
    Request.Get("https://steamcommunity.com/market/itemordershistogram/?")
}

fun main(args: Array<String>) {

    val lowestPricesEndpoint = "https://api.opskins.com/IPricing/GetAllLowestListPrices/v1/"
    val keyParam = "?key=be1c7d27835e4b13cb19377ef005ff"

    //println(Request.Get(lowestPricesEndpoint + keyParam).execute().returnContent().asString())
    /*val json = OPSkinsRequestBuilder.PricingInterface().getAllLowestListPrices().execute().returnContent().asString()
    val deserializer = LowestListPrice.getJsonDeserializer()
    val gsonBuilder = GsonBuilder()
    gsonBuilder.registerTypeAdapter(Array<LowestListPrice>::class.java, deserializer)
    val customGson = gsonBuilder.create()
    val jsonElement = Gson().fromJson(json,JsonElement::class.javaObjectType).asJsonObject.get("response").asJsonObject
    val lowestPrices : Array<LowestListPrice> = customGson.fromJson(jsonElement, Array<LowestListPrice>::class.java)*/

    val lowestPrices = OPSkinsRequestBuilder.PricingInterface().getAllLowestListPrices()
    /*lowestPrices.entries.sortedBy { e -> e.value.quantity }
            .reversed()
            .filter{e -> e.value.price > 10}.forEach { println(it) }*/

    val filtered = lowestPrices.entries
            .filter{e -> e.value.price > 10 && e.value.quantity > 10 && e.value.price < 1000}


    var filtered2 : MutableMap<String, Pair<LowestListPrice, Double>> = LinkedHashMap<String, Pair<LowestListPrice, Double>>()

    for (e in filtered ) {
        filtered2.put(e.key, Pair(e.value, calcProfitability(e)))
    }

    //val filtered3 : Map<String, Pair<LowestListPrice, Double>> = filtered2.entries.sortedBy { e -> e.value.second}.filter{true}.map








            //.map{ e -> e.key to MarketValue(e.value.quantity, e.value.price, OPSkinsRequestBuilder.PricingInterface().getSuggestedPricesV1(e.key).values.first().steamMarketPrice)}
            //.forEach { println(it) }




    //val uri = URIBuilder().set
}

fun calcProfitability( e : Map.Entry<String, LowestListPrice>) : Double {
    Thread.sleep(10000)
    var profitability : Double = 0.0
    while (true) {
        try {
            profitability = (OPSkinsRequestBuilder.PricingInterface().getSuggestedPricesV1(e.key).values.first().steamMarketPrice.toDouble() - e.value.price.toDouble())/(e.value.price.toDouble())
            break
        } catch ( ex : IllegalStateException) {
            Thread.sleep(1000)
        }
    }
        println("${e.key}: $profitability")
    return profitability
}

data class MarketAnalysis1(val name : String, val opSkinsMinPrice : Int)







fun getOpSkinsResultsPageUrl(query : String) : String {
    return "https://opskins.com/?loc=shop_search&app=730_2&search_item=${query}&sort=lh"
}



// maxPage should be set to the maximum page of allCsgoWeapons sorted by quantity that there are still 100 weapons per listing
// This provides a big enough sample size for buy requests and market stability
val maxPage = 200



