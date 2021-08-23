package kz.bfgroup.smarthomeapp.my_home.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class HomePassportApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("id_home")
    val id_home: String?,
    @JsonProperty("year_construction")
    val year_construction: String?,
    @JsonProperty("total_area")
    val total_area: String?,
    @JsonProperty("number_floor")
    val number_floor: String?,
    @JsonProperty("living_area")
    val living_area: String?,
    @JsonProperty("area_premises")
    val area_premises: String?,
    @JsonProperty("balcony_area")
    val balcony_area: String?,
    @JsonProperty("number_apartments")
    val number_apartments: String?,
    @JsonProperty("series_project")
    val series_project: String?
)