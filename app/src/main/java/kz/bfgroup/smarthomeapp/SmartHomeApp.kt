package kz.bfgroup.smarthomeapp

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class SmartHomeApp: Application() {

    override fun onCreate() {
        MapKitFactory.setApiKey("5082c38f-5a2f-4d28-8ecb-3371165769a0")
        super.onCreate()
    }
}