package give.away.good.deeds.sharePref.service

import give.away.good.deeds.network.model.User


/**
 * @author Hitesh
 * @version 1.0
 * @since 4th July 2023
 */
interface ProfilePrefService {

    fun isLoggedIn(): Boolean

    fun setLoggedIn(status:Boolean)

    fun getUserId(): String

    fun setUserId(userId:String)

    fun getAccessToken(): String?

    fun setAccessToken(token:String)

    fun saveUser(user: User)

    fun setProfileImage(imageUrl: String, imageId: String)

    fun getProfileImage(): String?

    fun getProfileImageId(): String?

    fun getEmail(): String?

    fun getLastName(): String?

    fun getFirstName(): String

    fun getUser(): User?

    fun clear()

}