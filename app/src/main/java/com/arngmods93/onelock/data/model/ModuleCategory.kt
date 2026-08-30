package com.arngmods93.onelock.data.model

/**
 * High level categories used to group and filter Good Lock modules.
 * Keep this list small and stable; new modules should map to one of
 * these buckets whenever possible instead of introducing new ones.
 */
enum class ModuleCategory(val displayName: String) {
    PERSONALIZATION("Personalización"),
    LOCK_SCREEN("Pantalla de bloqueo"),
    NAVIGATION_BAR("Barra de navegación"),
    NOTIFICATIONS("Notificaciones"),
    KEYBOARD("Teclado"),
    CAMERA("Cámara"),
    MULTITASKING("Multitarea"),
    SYSTEM("Sistema"),
    OTHER("Otros")
}
