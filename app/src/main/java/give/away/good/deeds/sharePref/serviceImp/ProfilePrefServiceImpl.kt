package give.away.good.deeds.sharePref.serviceImp

import give.away.good.deeds.network.model.User
import give.away.good.deeds.sharePref.SharedPrefManager
import give.away.good.deeds.sharePref.service.ProfilePrefService


/**
 * @author Hitesh
 * @version 1.0
 * @since 29 July 2023
 */
class ProfilePrefServiceImpl(private val manager: SharedPrefManager) :
    ProfilePrefService {

    companion object {
        private const val IS_LOGGED_IN = "auth.is_logged_in"
        private const val USER_ID = "profile.user_id"
        private const val ACCESS_TOKEN = "auth.access_token"
        private const val REFRESH_TOKEN = "auth.refresh_token"

        private const val FIRST_NAME = "profile.first_name"
        private const val LAST_NAME = "profile.last_name"
        private const val EMAIL = "profile.email"
        private const val STATUS = "profile.status"
        private const val BIRTHDAY = "profile.birthday"
        private const val PROFILE_IMAGE = "profile.profile_image"
        private const val PROFILE_IMAGE_ID = "profile.profile_image_id"
        private const val USER_ARCHETYPE = "profile.user_archetype"
        private const val LANGUAGE = "profile.prefer.language"
        private const val FEELING_TITLE = "profile.feeling.title"
        private const val FEELING_IMAGE = "profile.feeling.image"
        private const val FEELING_CARD_POSITION = "profile.feeling.card.position"
        private const val QUIZ_COMPLETE = "profile.quiz.complete"
        private const val PROFILE_GENDER_ID = "profile.gender.id"
        private const val PROFILE_AGE_GROUP_ID = "profile.age.group.id"
    }

    override fun isLoggedIn() = manager.getBoolean(IS_LOGGED_IN)

    override fun setLoggedIn(status: Boolean) {
        manager.put(IS_LOGGED_IN, status)
    }

    override fun getUserId(): String {
        return manager.getString(USER_ID)!!
    }

    override fun setUserId(userId: String) {
        manager.put(USER_ID, userId)
    }

    override fun getAccessToken() = manager.getString(ACCESS_TOKEN)

    override fun setAccessToken(token: String) {
        manager.put(ACCESS_TOKEN, token)
    }


    override fun saveUser(user: User) {
        manager.put(USER_ID, user.id)
        manager.put(FIRST_NAME, user.firstName)
        manager.put(LAST_NAME, user.lastName)
        manager.put(EMAIL, user.email)
        setLoggedIn(true)
    }

    override fun getFirstName() = manager.getString(FIRST_NAME) ?: ""

    override fun getLastName() = manager.getString(LAST_NAME)

    override fun getEmail() = manager.getString(EMAIL)

    override fun getProfileImage() = manager.getString(PROFILE_IMAGE)

    override fun getProfileImageId(): String? = manager.getString(PROFILE_IMAGE_ID)

    override fun setProfileImage(imageUrl: String, imageId: String) {
        manager.put(PROFILE_IMAGE, imageUrl)
        manager.put(PROFILE_IMAGE_ID, imageId)
    }

    override fun getUser(): User {
        return User(getUserId(), getFirstName(), getLastName(), getEmail() ?: "")
    }

    override fun clear() {
        val lang = manager.get(LANGUAGE, "")
        manager.clear()
        manager.put(LANGUAGE, lang)
    }


}