package petstore.utils

data class OrderData(
    var id: Int,
    val petId: Int,
    var quantity: Int,
    var shipDate: String,
    var status: String,
    var complete: Boolean
) {
    lateinit var petID: Any
}
