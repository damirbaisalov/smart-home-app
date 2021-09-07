package kz.smart.house.ksk_detailed.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kz.smart.house.registration.models.StreetApiData

@JsonIgnoreProperties(ignoreUnknown = true)
data class KskDetailedApiData(
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
    @JsonProperty("doma_phones")
    val doma_phones: String?,
    @JsonProperty("reiting")
    val reiting: String?,
    @JsonProperty("addrs")
    val addrs: List<StreetApiData>
)