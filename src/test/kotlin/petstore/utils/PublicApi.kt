package petstore.utils

import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.mapper.ObjectMapperType
import io.restassured.specification.RequestSpecification

class PublicApi {

    fun specification(): RequestSpecification = RequestSpecBuilder().apply {
        setContentType(ContentType.JSON)
        setAccept(ContentType.JSON)
        setBaseUri("https://petstore.swagger.io/v2/")
        setConfig(RestAssuredConfig.config().objectMapperConfig(ObjectMapperConfig(ObjectMapperType.JACKSON_2)))
    }.build()
}