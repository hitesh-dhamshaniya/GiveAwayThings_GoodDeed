package give.away.good.deeds.repository

import give.away.good.deeds.network.model.User


object UserCache {

    private val list = mutableListOf<User>()

    fun add(user: User?) {
        user?.let { list.add(it) }
    }

    fun get(userId: String): User? {
        return list.firstOrNull { user -> user.id == userId }
    }

}