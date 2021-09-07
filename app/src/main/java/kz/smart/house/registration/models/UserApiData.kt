package kz.smart.house.registration.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserApiData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("street")
    val surname: String?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("patronymic")
    val patronymic: String?,
    @JsonProperty("entrance")
    val entrance: String?,
    @JsonProperty("apartment")
    val apartment: String?,
    @JsonProperty("home_id")
    val home_id: String?,
    @JsonProperty("ksk_id")
    val ksk_id: String?,
    @JsonProperty("serial_number")
    val serial_number: String?,
    @JsonProperty("password")
    val password: String?,
    @JsonProperty("e_mail")
    val e_mail: String?,
    @JsonProperty("date")
    val date: String?,
    @JsonProperty("tel")
    val tel: String?,
    @JsonProperty("mess")
    val mess: String?,
    @JsonProperty("mess_status")
    val mess_status: String?
)