package ch.hearc.minigolf.utilities.network

import com.github.kittinunf.fuel.core.FuelManager


class HttpManager() {

    companion object {
        val fuelManager = FuelManager
        val routes = Routes()
        val timeout = 3000
        val root = "https://swiped.srvz-webapp.he-arc.ch/api"
        init {
            fuelManager.instance.basePath = root
            fuelManager.instance.timeoutInMillisecond = timeout
            fuelManager.instance.timeoutReadInMillisecond = timeout
        }
    }

}
