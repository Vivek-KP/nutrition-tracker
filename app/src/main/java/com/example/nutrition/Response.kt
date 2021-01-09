package com.example.nutrition

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("hits")
	val hits: List<HitsItem?>? = null,

	@field:SerializedName("total_hits")
	val totalHits: Int? = null,

	@field:SerializedName("max_score")
	val maxScore: Double? = null
)

data class HitsItem(

	@field:SerializedName("_index")
	val index: String? = null,

	@field:SerializedName("_type")
	val type: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("_score")
	val score: Double? = null,

	@field:SerializedName("fields")
	val fields: Fields? = null
)

data class Fields(

	@field:SerializedName("nf_saturated_fat")
	val nfSaturatedFat: Float? = null,

	@field:SerializedName("nf_cholesterol")
	val nfCholesterol: Float? = null,

	@field:SerializedName("nf_total_fat")
	val nfTotalFat: Float? = null,

	@field:SerializedName("nf_serving_size_qty")
	val nfServingSizeQty: Float? = null,

	@field:SerializedName("nf_calories")
	val nfCalories: Float? = null,

	@field:SerializedName("nf_serving_size_unit")
	val nfServingSizeUnit: String? = null,

	@field:SerializedName("nf_sodium")
	val nfSodium: Float? = null,

	@field:SerializedName("nf_sugars")
	val nfSugars: Float? = null,

	@field:SerializedName("item_name")
	val itemName: String? = null,

	@field:SerializedName("nf_protein")
	val nfProtein: Float? = null,

	@field:SerializedName("nf_total_carbohydrate")
	val nfTotalCarbohydrate: Float? = null
)
