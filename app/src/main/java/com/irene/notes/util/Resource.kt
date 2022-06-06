package com.irene.notes.util

class Resource<T>(
    var status: Status = Status.LOADING,
    var data: T? = null,
    var message: String? = null
)
{
    companion object {

        fun <T> error(message: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(status: Status, data: T?): Resource<T> {
            return Resource(status, data, null )
        }

        fun <T> success(data: T? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }
    }

    override fun toString(): String {
        return "$status"
    }

    enum class Status {
        SUCCESS, ERROR, LOADING, FIRST_LAUNCH_LOADING
    }
}