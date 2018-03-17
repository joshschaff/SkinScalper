package restAPI

import restAPI.opSkinsAPI.LowestListPrice
import restAPI.opSkinsAPI.OPSkinsRequestBuilder

fun convertArrayToURL(param : String, array : Array<out Any>) : Array<Pair<String,String>> {
    return array.mapIndexed { i, e -> Pair("$param[$i]",e.toString()) }.toTypedArray()
}

abstract class SkinAnalysis {
    abstract val name : String
    abstract val opSkinsPrice : Int
    abstract val steamMarketPrice : Int
    val profitability : Double
     get() = (steamMarketPrice.toDouble() - opSkinsPrice.toDouble())/(opSkinsPrice.toDouble())
}

/**
 * Constructed from OpSkinsRequestBuilder.PricingInterface().getLowestListPrices
 */
class SkinAnalysis1(e : Map.Entry<String, LowestListPrice>) : SkinAnalysis() {
    override val name: String = e.key
    override val opSkinsPrice: Int = e.value.price
    override val steamMarketPrice : Int = OPSkinsRequestBuilder.PricingInterface().getSuggestedPricesV1(e.key).entries.first().value.steamMarketPrice
}

/*
class SkinAnalysis2(a : SkinAnalysis1) : SkinAnalysis() {
    override val name : String = a.name
    override val opSkinsPrice: Int = a.opSkinsPrice
    override val steamMarketPrice: Int =
}*/