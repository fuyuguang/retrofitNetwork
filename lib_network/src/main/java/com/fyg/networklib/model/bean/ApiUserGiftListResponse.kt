package com.fyg.networklib.model.bean

import android.annotation.SuppressLint
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ApiUserGiftListResponse<T>(
    var giftList: T,
    var userWineCoin: Int
) /*: Parcelable*/ {
    /**
     * 数据是否为空
     */
    fun isEmpty() = (giftList as List<*>).size == 0
}