package give.away.good.deeds.sharePref.serviceImp

import give.away.good.deeds.sharePref.SharedPrefManager
import give.away.good.deeds.sharePref.service.AuthPrefService

/**
 * @author Hitesh
 * @version 1.0
 * @since July 2023
 */
class AuthPrefServiceImpl(private val manager: SharedPrefManager) :
    AuthPrefService {

    companion object {
        const val EMAIL_ADDRESS = "user.email.address"
        const val PASSWORD = "user.password"
        const val APPINTRO_SCREEN = "screen.app.intro"
    }

    override fun setEmail(email: String) {
        manager.put(EMAIL_ADDRESS, email)
    }

    override fun getEmail() = manager.get(EMAIL_ADDRESS, "")

    override fun setPassword(password: String) {
        manager.put(PASSWORD, password)
    }

    override fun getPassword() = manager.get(PASSWORD, "")

    override fun haveSeenAppIntroScreen(boolean: Boolean) = manager.put(APPINTRO_SCREEN, boolean)

    override fun haveSeenAppIntroScreen(): Boolean {
        return manager.get(APPINTRO_SCREEN, false)
    }
}