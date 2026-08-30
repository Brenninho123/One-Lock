package com.arngmods93.onelock.data

import com.arngmods93.onelock.data.model.GoodLockModule
import com.arngmods93.onelock.data.model.ModuleCategory

/**
 * Single source of truth for every module shown in One Lock.
 */
object ModuleCatalog {

    val modules: List<GoodLockModule> = listOf(
        GoodLockModule(
            id = "good-lock",
            name = "Good Lock",
            packageName = "com.samsung.android.goodlock",
            description = "La aplicación núcleo del ecosistema Good Lock. Actúa como panel central desde el que instalar y configurar el resto de módulos.",
            category = ModuleCategory.SYSTEM,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/good-lock-2018/",
            minimumAndroid = "Android 9.0+",
            minimumAndroidSdk = 28,
            supportedOneUI = "One UI 2.0 – 8.x"
        ),
        GoodLockModule(
            id = "theme-park",
            name = "Theme Park",
            packageName = "com.samsung.android.themedesigner",
            description = "Crea temas personalizados combinando colores, iconos y fondos sin necesidad de conocimientos de diseño.",
            category = ModuleCategory.PERSONALIZATION,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/theme-park/",
            minimumAndroid = "Android 10.0+",
            minimumAndroidSdk = 29,
            supportedOneUI = "One UI 3.0 – 8.x"
        ),
        GoodLockModule(
            id = "wonderland",
            name = "Wonderland",
            packageName = "com.samsung.android.wonderland",
            description = "Widgets y fondos de pantalla de bloqueo dinámicos e interactivos, con estilos que cambian según la hora o el clima.",
            category = ModuleCategory.LOCK_SCREEN,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/samsung-wonderland/",
            minimumAndroid = "Android 11.0+",
            minimumAndroidSdk = 30,
            supportedOneUI = "One UI 3.1 – 8.x"
        ),
        GoodLockModule(
            id = "clockface",
            name = "ClockFace",
            packageName = "com.samsung.android.app.clockface",
            description = "Editor avanzado de relojes para la pantalla de bloqueo, con más estilos y personalización que el editor de reloj nativo.",
            category = ModuleCategory.LOCK_SCREEN,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/samsung-clockface/",
            minimumAndroid = "Android 11.0+",
            minimumAndroidSdk = 30,
            supportedOneUI = "One UI 4.0 – 8.x"
        ),
        GoodLockModule(
            id = "lockstar",
            name = "LockStar",
            packageName = "com.samsung.android.goodlock.lockscreen",
            description = "Añade widgets, accesos directos y música controlable directamente desde la pantalla de bloqueo.",
            category = ModuleCategory.LOCK_SCREEN,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/lockstar/",
            minimumAndroid = "Android 10.0+",
            minimumAndroidSdk = 29,
            supportedOneUI = "One UI 3.0 – 8.x"
        ),
        GoodLockModule(
            id = "nice-catch",
            name = "NiceCatch",
            packageName = "com.samsung.android.app.nicecatch",
            description = "Gestos y accesos rápidos desde la barra de navegación para lanzar apps y acciones frecuentes.",
            category = ModuleCategory.NAVIGATION_BAR,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/nice-catch/",
            minimumAndroid = "Android 10.0+",
            minimumAndroidSdk = 29,
            supportedOneUI = "One UI 2.5 – 8.x"
        ),
        GoodLockModule(
            id = "navstar",
            name = "NavStar",
            packageName = "com.samsung.android.navstar",
            description = "Personaliza por completo la barra de navegación: iconos, orden de botones, gestos y animaciones.",
            category = ModuleCategory.NAVIGATION_BAR,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/navstar/",
            minimumAndroid = "Android 11.0+",
            minimumAndroidSdk = 30,
            supportedOneUI = "One UI 3.1 – 8.x"
        ),
        GoodLockModule(
            id = "quickstar",
            name = "QuickStar",
            packageName = "com.samsung.android.qstuner",
            description = "Rediseña el panel de notificaciones y ajustes rápidos con más botones, diseños y opciones de comportamiento.",
            category = ModuleCategory.NOTIFICATIONS,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/quickstar/",
            minimumAndroid = "Android 9.0+",
            minimumAndroidSdk = 28,
            supportedOneUI = "One UI 2.0 – 8.x"
        ),
        GoodLockModule(
            id = "keys-cafe",
            name = "Keys Café",
            packageName = "com.samsung.android.keyscafe",
            description = "Personaliza el teclado de Samsung con temas, sonidos, vibración y diseños creados por la comunidad.",
            category = ModuleCategory.KEYBOARD,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/keys-cafe/",
            minimumAndroid = "Android 10.0+",
            minimumAndroidSdk = 29,
            supportedOneUI = "One UI 3.0 – 8.x"
        ),
        GoodLockModule(
            id = "camera-assistant",
            name = "Camera Assistant",
            packageName = "com.samsung.android.app.cameraassistant",
            description = "Ajustes avanzados de cámara: control manual de procesado de imagen, formatos de captura y opciones ocultas.",
            category = ModuleCategory.CAMERA,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/camera-assistant/",
            minimumAndroid = "Android 12.0+",
            minimumAndroidSdk = 31,
            supportedOneUI = "One UI 4.1 – 8.x"
        ),
        GoodLockModule(
            id = "multistar",
            name = "MultiStar",
            packageName = "com.samsung.android.multistar",
            description = "Mejora el multitarea: fuerza el modo multiventana en más apps, personaliza el editor de pantalla dividida y más.",
            category = ModuleCategory.MULTITASKING,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/multistar/",
            minimumAndroid = "Android 9.0+",
            minimumAndroidSdk = 28,
            supportedOneUI = "One UI 2.0 – 8.x"
        ),
        GoodLockModule(
            id = "task-changer",
            name = "Task Changer",
            packageName = "com.samsung.android.app.taskchanger",
            description = "Sustituye la pantalla de apps recientes por diseños alternativos: lista, cuadrícula o vista clásica.",
            category = ModuleCategory.MULTITASKING,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/task-changer/",
            minimumAndroid = "Android 10.0+",
            minimumAndroidSdk = 29,
            supportedOneUI = "One UI 3.0 – 8.x"
        ),
        GoodLockModule(
            id = "home-up",
            name = "Home Up",
            packageName = "com.samsung.android.app.homestar",
            description = "Opciones adicionales para la pantalla de inicio: cuadrícula personalizada, animaciones y disposición de iconos.",
            category = ModuleCategory.PERSONALIZATION,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/home-up/",
            minimumAndroid = "Android 11.0+",
            minimumAndroidSdk = 30,
            supportedOneUI = "One UI 3.1 – 8.x"
        ),
        GoodLockModule(
            id = "regi-star",
            name = "RegiStar",
            packageName = "com.samsung.android.app.regiStar",
            description = "Cambia el estilo de la barra de estado y el reloj, incluyendo animaciones de sonido y notificaciones por app.",
            category = ModuleCategory.SYSTEM,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/registar/",
            minimumAndroid = "Android 10.0+",
            minimumAndroidSdk = 29,
            supportedOneUI = "One UI 3.0 – 8.x"
        ),
        GoodLockModule(
            id = "sound-assistant",
            name = "Sound Assistant",
            packageName = "com.samsung.android.soundassistant",
            description = "Control de volumen y audio avanzado: control de app individual, atenuación multimedia y más opciones de sonido.",
            category = ModuleCategory.SYSTEM,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/soundassistant/",
            minimumAndroid = "Android 9.0+",
            minimumAndroidSdk = 28,
            supportedOneUI = "One UI 2.0 – 8.x"
        ),
        GoodLockModule(
            id = "edge-touch",
            name = "Edge Touch",
            packageName = "com.samsung.android.edgetouch",
            description = "Ajusta la sensibilidad táctil en los bordes de la pantalla para reducir toques accidentales.",
            category = ModuleCategory.OTHER,
            apkMirrorUrl = "https://www.apkmirror.com/apk/samsung-electronics-co-ltd/edge-touch/",
            minimumAndroid = "Android 10.0+",
            minimumAndroidSdk = 29,
            supportedOneUI = "One UI 2.5 – 8.x"
        )
    )
}
