package give.away.good.deeds.di


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import give.away.good.deeds.repository.AuthRepository
import give.away.good.deeds.repository.AuthRepositoryImpl
import give.away.good.deeds.repository.MediaRepository
import give.away.good.deeds.repository.MediaRepositoryImpl
import give.away.good.deeds.repository.PostRepository
import give.away.good.deeds.repository.PostRepositoryImpl
import give.away.good.deeds.repository.UserConfigRepository
import give.away.good.deeds.repository.UserConfigRepositoryImpl
import give.away.good.deeds.repository.UserRepository
import give.away.good.deeds.repository.UserRepositoryImpl
import give.away.good.deeds.ui.screens.splash.SplashViewModel
import give.away.good.deeds.utils.NetworkReader
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * @author Hitesh
 * @version 1.0
 * @since July 2023
 */
val appViewModules = module {
    viewModel {
        SplashViewModel(get())
    }

    factory<NetworkReader> {
        NetworkReader(get())
    }
    factory<FirebaseAuth> {
        Firebase.auth
    }
    factory<FirebaseFirestore> {
        Firebase.firestore
    }
    factory<FirebaseStorage> {
        Firebase.storage
    }
    factory<AuthRepository> {
        AuthRepositoryImpl(get())
    }
    factory<UserRepository> {
        UserRepositoryImpl(get(), get())
    }
    factory<MediaRepository> {
        MediaRepositoryImpl(get(), get())
    }
    factory<UserConfigRepository> {
        UserConfigRepositoryImpl(get(), get())
    }
    factory<PostRepository> {
        PostRepositoryImpl(get(), get(), get())
    }

}
