package kz.smart.house

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class SmartHomeApp: Application() {

    override fun onCreate() {
        MapKitFactory.setApiKey("c262adf3-1680-4d27-a67d-3890f1a891eb")
        super.onCreate()
    }
}