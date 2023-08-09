package com.ankur.akart.RoomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

@Entity(tableName = "Product_table")
data class ProductModel(

    @PrimaryKey
    @Nonnull
    val ProductId :String,
    @ColumnInfo(name ="productName")
    val ProductName:String?="",
    @ColumnInfo(name ="productImg")
    val ProductCoverImg:String?="",
    @ColumnInfo(name ="productSP")
    val ProductSP:String?=""

)
