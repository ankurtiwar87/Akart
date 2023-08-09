package com.ankur.akart.RoomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertProduct(Product:ProductModel)

   @Delete
   fun deleteProduct(Product: ProductModel)

  @Query("SELECT * FROM Product_table")
    fun getAllProduct():LiveData<List<ProductModel>>

    @Query("SELECT * FROM PRODUCT_TABLE WHERE ProductId= :id")
    fun isExist(id:String):ProductModel



}