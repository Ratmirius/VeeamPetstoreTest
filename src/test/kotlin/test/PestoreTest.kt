package test

import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import petstore.utils.OrderData
import petstore.utils.PublicApi
import org.hamcrest.Matchers.*
import petstore.utils.PetData

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PetstoreTest {

    private val publicApi = PublicApi()
    private val requestSpec = publicApi.specification()

    @Test
    fun `Create new order`(){
        val order = OrderData(
            id = 1,
            petId = 2,
            quantity = 2,
            shipDate = "2021-07-27T15:52:02.956Z",
            status = "placed",
            complete = true
        )
        Given {
            spec(requestSpec)
        } When {
            log().all()
            body(mapOf(
                "id" to order.id,
                "petId" to order.petId,
                "quantity" to order.quantity,
                "shipDate" to order.shipDate,
                "status" to order.status,
                "complete" to order.complete
            ))
            post("store/order")
        } Then {
            log().ifValidationFails()
            statusCode(200)
            body("id", `is`(order.id))
            body("petId", `is`(order.petId))
            body("quantity", `is`(order.quantity))
            body("status", `is`(order.status))
            body("complete", `is`(order.complete))
        }
        getOrder(order)
        deleteOrder(order.id)
    }

    @Test
    fun `Get order`(){
        val order = createOrder()

        Given {
            spec(requestSpec)
        } When {
            get("store/order/${order.id}")
        } Then  {
            log().ifValidationFails()
            statusCode(200)
            body("id", `is`(order.id))
            body("petId", `is`(order.petId))
            body("shipDate", `is`(order.shipDate))
            body("quantity", `is`(order.quantity))
            body("status", `is`(order.status))
            body("complete", `is`(order.complete))
        }
        deleteOrder(order.id)
    }

    @Test
    fun `get non-exist order`(){
        val order = createOrder()
        deleteOrder(order.id)

        Given {
            spec(requestSpec)
        } When {
            get("store/order/${order.id}")
        } Then  {
            log().ifValidationFails()
            statusCode(404)
        }
    }

    @Test
    fun `Delete order`(){
        val order = createOrder()
        Given {
            spec(requestSpec)
        } When {
            delete("store/order/${order.id}")
        } Then {
            log().ifValidationFails()
            statusCode(200)
        }
    }

    @Test
    fun `Delete non-exist order`(){
        val order = createOrder()
        deleteOrder(order.id)
        Given {
            spec(requestSpec)
        } When {
            delete("store/order/${order.id}")
        } Then {
            log().ifValidationFails()
            statusCode(404)
        }
    }

    @Test
    fun `get pet inventories by status`(){
        val pet = addNewPet()

        Given {
            spec(requestSpec)
        } When {
            get("store/inventory")
        } Then {
            log().ifValidationFails()
            statusCode(200)
            body(pet.status, `is`(1))
        }
    }


    //Создание заказа
    private fun createOrder(): OrderData {
        val order = OrderData(
            id = 1,
            petId = 2,
            quantity = 2,
            shipDate = "2021-07-27T15:52:02.956Z",
            status = "placed",
            complete = true
        )
        Given {
            spec(requestSpec)
        } When {
            body(mapOf(
                "id" to order.id,
                "petId" to order.petId,
                "quantity" to order.quantity,
                "shipDate" to order.shipDate,
                "status" to order.status,
                "complete" to order.complete
            ))
            post("store/order")
        } Extract {
            order.id = path("id")
            order.petID = path("petId")
            order.shipDate = path("shipDate")
            order.quantity = path("quantity")
            order.complete = path("complete")
            order.status = path("status")
        }
        return order
    }

    //Получение заказа
    private fun getOrder(order: OrderData) {
        Given {
            spec(requestSpec)
        } When {
            get("store/order/${order.id}")
        } Then  {
            log().ifValidationFails()
            statusCode(200)
            body("id", `is`(order.id))
            body("petId", `is`(order.petId))
            body("shipDate", `is`(order.shipDate))
            body("quantity", `is`(order.quantity))
            body("status", `is`(order.status))
            body("complete", `is`(order.complete))
        }
    }

    //Удаление заказа
    private fun deleteOrder(id: Int){
        Given {
            spec(requestSpec)
        } When {
            delete("store/order/${id}")
        } Then {
            log().ifValidationFails()
            statusCode(200)
        }
    }

    //Добавить нового питомца с уникальным status
    private fun addNewPet(): PetData {
        val pet = PetData(
            id = 0,
            category = mapOf("id" to 0, "name" to "string"),
            name = "helldog",
            photoUrls = listOf("string"),
            tags = listOf(mapOf("id" to 0, "name" to "string")),
            status = "status" + (0..100000).random().toString()
        )

        Given {
            spec(requestSpec)
        } When {
            log().all()
            body(mapOf(
                "id" to pet.id,
                "categoty" to pet.category,
                "name" to pet.name,
                "photoUrls" to pet.photoUrls,
                "tags" to pet.tags,
                "status" to pet.status
            ))
            post("/pet")
        } Then {
            log().ifValidationFails()
            statusCode(200)
        }
        return pet
    }
}

