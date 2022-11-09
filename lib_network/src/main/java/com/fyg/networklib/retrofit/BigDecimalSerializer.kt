package com.fyg.networklib.retrofit
import com.google.gson.*
import java.lang.reflect.Type
import java.math.BigDecimal

class BigDecimalSerializer : JsonSerializer<BigDecimal> {
    override fun serialize(
        src: BigDecimal,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        /*四舍五入*/
        val newVal = src.setScale(2, BigDecimal.ROUND_HALF_UP)
        /*返回Josnelement*/
        return JsonPrimitive(newVal)
    }



    companion object {

        var gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(BigDecimal::class.java, BigDecimalSerializer())
            .create()

    }

}

