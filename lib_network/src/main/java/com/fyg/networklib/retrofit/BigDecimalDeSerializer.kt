package com.fyg.networklib.retrofit
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.RoundingMode

class BigDecimalDeSerializer : JsonDeserializer<BigDecimal> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BigDecimal {
        /*四舍五入*/
        val newValue = json.asBigDecimal.setScale(2, RoundingMode.HALF_UP)
        /*返回处理后的值*/
        return newValue
    }

    companion object {

        var gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls()
            .setLenient()
            .registerTypeAdapter(BigDecimal::class.java, BigDecimalDeSerializer())
            .create()

        fun <T> fromJson(json: String, classOfT: Class<T>): T {
            return gson.fromJson(json, classOfT)
        }

        inline fun <reified T> fromJson(json: String): T {
            return gson.fromJson(json, object : TypeToken<T>() {}.type)
        }

        fun toJson(src: Any): String {
            return gson.toJson(src)
        }

        fun toJson(src: Any, typeOfSrc: Type): String {
            return gson.toJson(src, typeOfSrc)
        }

    }




}