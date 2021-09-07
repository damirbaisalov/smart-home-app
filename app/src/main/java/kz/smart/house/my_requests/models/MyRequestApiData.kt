package kz.smart.house.my_requests.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MyRequestApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("heading")
    val heading: String?,
    @JsonProperty("text")
    val text: String?,
    @JsonProperty("status")
    val status: String?,
    @JsonProperty("data_time")
    val data_time: String?,
    @JsonProperty("dom_id")
    val dom_id: String?,
    @JsonProperty("ksk_id")
    val ksk_id: String?,
    @JsonProperty("tenants_id")
    val tenants_id: String?,
    @JsonProperty("adress")
    val address: String
)