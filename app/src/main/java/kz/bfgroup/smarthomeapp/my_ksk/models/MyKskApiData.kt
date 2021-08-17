package kz.bfgroup.smarthomeapp.my_ksk.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MyKskApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("ksk_name")
    val kskName: String?,
    @JsonProperty("city")
    val city: String?,
    @JsonProperty("adress")
    val address: String?,
    @JsonProperty("director_full_name")
    val director_full_name: String?,
    @JsonProperty("phones")
    val phones: String?,
    @JsonProperty("reiting")
    val reiting: String?
)