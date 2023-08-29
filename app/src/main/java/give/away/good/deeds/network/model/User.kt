package give.away.good.deeds.network.model

import com.google.firebase.firestore.Exclude

data class User(
    val id: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    val email: String? = "",
    var profilePic: String? = ""
) {
    @Exclude
    fun toMap() {
        "id" to id
        "firstName" to firstName
        "lastName" to lastName
        "email" to email
        "profilePic" to profilePic
    }

    override fun toString(): String {
        return "User(id='$id', firstName='$firstName', lastName='$lastName', email='$email', profilePic='$profilePic')"
    }
}