package kz.smart.house.my_home.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class HomeApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("street")
    val street: String?,
    @JsonProperty("nomer")
    val nomer: String?,
    @JsonProperty("col_padik")
    val col_padik: String?,
    @JsonProperty("ksk_id")
    val ksk_id: String?,
    @JsonProperty("status")
    val status: String?
)