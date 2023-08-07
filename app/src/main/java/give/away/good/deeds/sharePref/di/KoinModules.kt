package give.away.good.deeds.sharePref.di

import android.content.Context
import give.away.good.deeds.sharePref.SharedPrefManager
import give.away.good.deeds.sharePref.service.AuthPrefService
import give.away.good.deeds.sharePref.service.ProfilePrefService
import give.away.good.deeds.sharePref.serviceImp.AuthPrefServiceImpl
import give.away.good.deeds.sharePref.serviceImp.ProfilePrefServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * @author Hitesh
 * @version 1.0
 * @since July 2023
 */
val sharedPrefModules = module {

    factory<SharedPrefManager>(named("app")) {
        val pref = get<Context>().getSharedPreferences(get<Context>().packageName, Context.MODE_PRIVATE)
        SharedPrefManager(pref)
    }

    factory<SharedPrefManager>(named("session")) {
        val pref = get<Context>().getSharedPreferences(get<Context>().packageName + ".session", Context.MODE_PRIVATE)
        SharedPrefManager(pref)
    }

    /* factory<SharedPrefManager>(named("session")) {
         val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

         val pref = EncryptedSharedPreferences.create(
             "secret_shared_prefs",
             masterKeyAlias,
             get(),
             EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
             EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
         )

         SharedPrefManager(pref)
     }*/

    single<AuthPrefService> {
        AuthPrefServiceImpl(
            get(named("app"))
        )
    }

    single<ProfilePrefService> {
        ProfilePrefServiceImpl(
            get(named("session"))
        )
    }

    factory<String>(named("access_token")) {
        (get() as ProfilePrefService).getAccessToken()!!
    }

}