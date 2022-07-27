package petstore.utils

data class PetData(
    var id: Int,
    var category: Map<Any, Any>,
    var name: String,
    var photoUrls: List<String>,
    var tags: List<Map<Any, Any>>,
    var status: String
)
