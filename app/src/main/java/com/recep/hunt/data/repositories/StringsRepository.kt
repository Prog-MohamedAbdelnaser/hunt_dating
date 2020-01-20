package com.recep.hunt.data.repositories

import com.nasmanpower.nas.data.sources.resources.AppResources
import com.recep.hunt.R

class StringsRepository(private val appResources: AppResources) {


    fun getNetworkExceptionMessage(): String = appResources.getString(R.string.check_your_network_connection_and_try_again)

    fun getSocketTimeoutExceptionMessage(): String = appResources.getString(R.string.timeout_error_message)


}