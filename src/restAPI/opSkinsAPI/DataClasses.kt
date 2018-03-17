package restAPI.opSkinsAPI

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

/**
 * Each of these are used as the value of a map, with the key being the name of the market object
 */

data class LowestListPrice(val quantity : Int, // Total sold on OpSkins
                           // Stored as cents
                           val price : Int) {
    companion object {
        fun getJsonDeserializer() =  object : JsonDeserializer<Map<String,LowestListPrice>> {
            @Throws(JsonParseException::class)
            override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Map<String, LowestListPrice> {
                val jsonObject = json.getAsJsonObject()
                return jsonObject.entrySet().map { m -> Pair(m.key,  LowestListPrice(m.value.asJsonObject.get("quantity").asInt,
                        m.value.asJsonObject.get("price").asInt))}.toMap()
            }
        }
    }
}

data class SuggestedPricesV1(val opSkinsPrice : Int,
                             val steamMarketPrice : Int,
                             val opSkinsLowestPrice : Int) {
    companion object {
        fun getJsonDeserializer() =  object : JsonDeserializer<Map<String,SuggestedPricesV1>> {
            @Throws(JsonParseException::class)
            override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Map<String, SuggestedPricesV1> {
                val jsonObject = json.getAsJsonObject()
                println("suggestedPriceJson: " + jsonObject)
                return jsonObject.entrySet().map { m -> Pair(m.key,  SuggestedPricesV1(m.value.asJsonObject.get("opskins_price").asInt,
                        m.value.asJsonObject.get("market_price").asInt,
                        m.value.asJsonObject.get("opskins_lowest_price").asInt))}.toMap()
            }
        }
    }
}

//this is a comment

/**
 * this is also a multiline comment
 * this is line 2
 */


/*data class PriceListV2(mean : Int,
        min : Int,
        max : Int,
        normalized_mean : Int,
        normalized_min: Int,
*/