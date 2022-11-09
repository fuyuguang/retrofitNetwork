package com.fyg.networklib.model.bean

import android.annotation.SuppressLint
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class UserGiftList(
    var id: Int,
    var image: String,
    var wineCoin: Int,
    var name: String
) /*: Parcelable*/