package restAPI

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlTable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.logging.Level
import java.util.stream.IntStream


//https://www.reddit.com/r/GlobalOffensiveTrade/comments/40ymjp/psa_how_to_visualize_buy_orders_on_offmarket_items/

fun getBuyRequestPrice(name : String) : Unit {

    java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

    val steamResultUrl : String = "https://steamcommunity.com/market/listings/730/$name"

    println(steamResultUrl)


    val page : HtmlPage = WebClient().getPage(steamResultUrl)
    //page.getHtmlElementById<HtmlElement>("market_buyorder_info_show_details").firstElementChild.click<HtmlPage>()
    println(page.getByXPath<HtmlTable>("//table[@class='market_commodity_orders_table']").first())

    if (page.getByXPath<HtmlTable>("//table[@class='market_commodity_orders_table']").size  != 0) {
        //println((page.getByXPath<HtmlTable>("//table[@class='market_commodity_orders_table']").first() as HtmlTable))
        //println(page.getByXPath<HtmlTable>("//table[@class='market_commodity_orders_table']").first())

        // list will be empty if there are no active buyorders
        val buyRequests: Map<Double, Int> = page.getByXPath<HtmlTable>("//table[@class='market_commodity_orders_table']").first()
                .bodies[0].rows
                .mapIndexedNotNull { i, r -> if (i == 0) null else r }
                .map { r -> r.getCell(0).asText().substring(1).split(" ".toRegex())[0].toDouble() to r.getCell(1).asText().toInt() }.toMap()

        val ml = MarketListing(name, steamResultUrl, buyRequests)

        println(ml)
        //println(ml.buyRequests.keys)
        //println(ml.buyRequests.values)
    } else {
        println("size was wrongg")
    }
}


fun meh(args: Array<String>) {

    java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);


    val ml1 : MarketListing = MarketListing("StatTrak™ Galil AR | Kami",
            "http://steamcommunity.com/market/listings/730/SCAR-20%20%7C%20Sand%20Mesh%20%28Field-Tested%29",
            mapOf(.66 to 15, .65 to 26, .63 to 16, .62 to 13, .61 to 5))

    IntStream.range(300,310).parallel().forEach { p ->
        val steamSearchDoc: Document = Jsoup.connect(getSteamResultsPageUrl(p)).get()
        for (j in 0..9) {
            val name : String = steamSearchDoc.getElementById("result_${j}_name").text()
            val steamResultUrl : String = steamSearchDoc.getElementById("resultlink_$j").attr("href")
            val steamResultDoc : Document = Jsoup.connect(steamResultUrl).get()


            val jscode : String = "\$J('#market_buyorder_info_show_details').hide(); \$J('#market_buyorder_info_details').show();"


            //println(steamResultUrl)
            val page : HtmlPage = WebClient().getPage(steamResultUrl)
            page.getHtmlElementById<HtmlElement>("market_buyorder_info_show_details").firstElementChild.click<HtmlPage>()


            //val newPage = page.executeJavaScript(jscode).newPage

            //println("newishtml:" + newPage.isHtmlPage)

            //println(steamResultUrl)




            "http://steamcommunity.com/market/listings/730/SG%20553%20|%20Waves%20Perforated%20(Field-Tested)&market_commodity_orders_table=1"
            //steamResultDoc.select("div#market_buyorder_info_show_details > span").first()

            /*val buyRequestsOld : Map<Double, Int> = steamResultDoc.getElementsByClass("market_commodity_orders_table")
                    // tbody
                    .child(0)
                    // gets rid of the first tr lmao
                    .children().mapIndexedNotNull { i , e -> if (i ==0) null else e }
                    .map { e -> e.child(0).text().toDouble() to e.child(1).text().toInt() }.toMap()*/

            //println(newPage.("market_commodity_buyreqeusts_table").childElementCount)

            if (page.getByXPath<HtmlTable>("//table[@class='market_commodity_orders_table']").size  != 0) {
                //println((page.getByXPath<HtmlTable>("//table[@class='market_commodity_orders_table']").first() as HtmlTable))
                //println(page.getByXPath<HtmlTable>("//table[@class='market_commodity_orders_table']").first())

                // list will be empty if there are no active buyorders
                val buyRequests: Map<Double, Int> = page.getByXPath<HtmlTable>("//table[@class='market_commodity_orders_table']").first()
                        .bodies[0].rows
                        .mapIndexedNotNull { i, r -> if (i == 0) null else r }
                        .map { r -> r.getCell(0).asText().substring(1).split(" ".toRegex())[0].toDouble() to r.getCell(1).asText().toInt() }.toMap()

                val ml = MarketListing(name, steamResultUrl, buyRequests)

                println(ml)
                //println(ml.buyRequests.keys)
                //println(ml.buyRequests.values)
            }
        }
    }


}


// Maximum is 25
// This is a search for all CSGO weapons sorted by quantity
fun getSteamResultsPageUrl(page : Int) : String {
    return "http://steamcommunity.com/market/search?q=&category_730_ItemSet%5B%5D=any&category_730_ProPlayer%5B%5D=any&category_730_StickerCapsule%5B%5D=any&category_730_TournamentTeam%5B%5D=any&category_730_Weapon%5B%5D=any&category_730_Type%5B%5D=tag_CSGO_Type_Pistol&category_730_Type%5B%5D=tag_CSGO_Type_SMG&category_730_Type%5B%5D=tag_CSGO_Type_Rifle&category_730_Type%5B%5D=tag_CSGO_Type_SniperRifle&category_730_Type%5B%5D=tag_CSGO_Type_Shotgun&category_730_Type%5B%5D=tag_CSGO_Type_Machinegun&category_730_Type%5B%5D=tag_CSGO_Type_Knife&appid=730#p${page}_quantity_desc"
}

data class MarketListing( // http://steamcommunity.com/market/listings/730/SCAR-20%20%7C%20Sand%20Mesh%20%28Field-Tested%29
        val name : String, // for example, "SCAR-20 | Sand Mesh (Field-Tested)"
        val url : String,
        val buyRequests : Map<Double,Int>
)
