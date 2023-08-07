package give.away.good.deeds.sharePref.service

/**
 * @author Hitesh
 * @version 1.0
 * @since July 2023
 */
interface AuthPrefService {
    fun getEmail(): String?

    fun getPassword(): String?

    fun setEmail(email: String)

    fun setPassword(password: String)

    fun haveSeenAppIntroScreen(boolean: Boolean)

    fun haveSeenAppIntroScreen(): Boolean
}