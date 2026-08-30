# One Lock 🔒

**One Lock** es un catálogo y gestor de acceso, de código abierto, para los módulos de **Samsung Good Lock** en dispositivos Samsung Galaxy con One UI.

> One Lock **no aloja ni redistribuye ningún APK**. Cada módulo enlaza a su página de descarga externa (por defecto, una búsqueda en APKMirror); la app solo abre esa URL en tu navegador predeterminado.

---

## 📱 Características

- Catálogo de los módulos más populares de Good Lock (Good Lock, Theme Park, Wonderland, MultiStar, QuickStar, NavStar, Sound Assistant, y más).
- Búsqueda en tiempo real por nombre, package name o categoría.
- Filtrado por categorías (Personalización, Pantalla de bloqueo, Barra de navegación, Notificaciones, Teclado, Cámara, Multitarea, Sistema, Otros).
- Detección automática del dispositivo (fabricante, modelo, versión de Android y, cuando es posible, versión de One UI) sin ADB ni root.
- Indicador de compatibilidad orientativo (🟢 Compatible / 🟡 Desconocida / 🔴 Posiblemente incompatible).
- Modo claro y oscuro, Material 3, diseño responsive.
- Botón "Abrir página de descarga" que lanza un `Intent.ACTION_VIEW` hacia el navegador del usuario — nunca descarga nada automáticamente.
- Pantalla de ajustes con información de la app, versión, enlace al repositorio y aviso legal.

## ⚙️ Compatibilidad

- Android 8.0 (API 26) o superior.
- Pensada para dispositivos Samsung Galaxy con One UI, aunque puede ejecutarse en cualquier dispositivo Android (en ese caso, la compatibilidad de los módulos se mostrará como "desconocida", ya que Good Lock es exclusivo de Samsung).

## 📥 Instalación

Actualmente no hay una APK firmada publicada; debes compilar el proyecto tú mismo siguiendo la sección [Compilación local](#-compilación-local).

## 🛠️ Compilación local

### Requisitos

- [Android Studio](https://developer.android.com/studio) (Koala o superior recomendado).
- JDK 17 (incluido con Android Studio).
- Android SDK con `compileSdk 35` / `targetSdk 35` instalado (Android Studio lo gestiona automáticamente).

### Pasos

```bash
git clone https://github.com/ArngMods93/One-Lock.git
cd One-Lock
```

1. Abre Android Studio.
2. Selecciona **File → Open…** y elige la carpeta `One-Lock` que acabas de clonar.
3. Espera a que Gradle sincronice las dependencias (la primera vez puede tardar unos minutos).
4. Conecta un dispositivo o inicia un emulador (API 26+).
5. Pulsa **Run ▶** o ejecuta desde terminal:

```bash
./gradlew assembleDebug
```

El APK generado quedará en `app/build/outputs/apk/debug/`.

> **Nota sobre `gradlew`:** el proyecto incluye el wrapper de Gradle (`gradlew`, `gradlew.bat` y `gradle/wrapper/gradle-wrapper.jar`) configurado para Gradle 8.9. Si Android Studio te pide regenerar el wrapper, acepta: es un paso normal y no significa que el proyecto esté roto.

## 🏗️ Arquitectura

Arquitectura MVVM con Jetpack Compose:

```text
app/src/main/java/com/arngmods93/onelock/
├── data/
│   ├── model/            # GoodLockModule, ModuleCategory, CompatibilityStatus, DeviceSnapshot
│   ├── ModuleCatalog.kt  # Única fuente de verdad del catálogo (nombres, URLs, metadatos)
│   └── repository/       # ModuleRepository: búsqueda y filtrado
├── ui/
│   ├── home/              # HomeScreen + HomeViewModel (lista, búsqueda, filtros)
│   ├── details/            # ModuleDetailScreen + ModuleDetailViewModel
│   ├── settings/           # SettingsScreen (info, versión, repo, disclaimer)
│   ├── navigation/         # OneLockNavHost (Navigation Compose + bottom bar)
│   ├── components/         # ModuleCard, CompatibilityBadge, SearchBar, CategoryChip, DeviceInfoCard
│   └── theme/               # Color.kt, Type.kt, Theme.kt (Material 3, claro/oscuro)
├── utils/
│   ├── DeviceInfo.kt         # Detección de fabricante/modelo/Android/One UI (best-effort, sin ADB/root)
│   ├── CompatibilityChecker.kt
│   └── BrowserUtils.kt       # Apertura segura de URLs externas vía ACTION_VIEW
└── MainActivity.kt
```

- **Capa de datos** (`data/`): modelos inmutables y el catálogo estático de módulos.
- **Capa de dominio/utilidades** (`utils/`): lógica de compatibilidad y apertura de enlaces, sin dependencias de UI.
- **Capa de presentación** (`ui/`): pantallas Compose + `ViewModel` por pantalla, siguiendo un flujo unidireccional de estado (`StateFlow` → `collectAsState`).

## ➕ Cómo agregar nuevos módulos

Todo el catálogo vive en un único archivo:

`app/src/main/java/com/arngmods93/onelock/data/ModuleCatalog.kt`

Para añadir un módulo nuevo, agrega una entrada más a la lista `modules`:

```kotlin
GoodLockModule(
    id = "mi-modulo",
    name = "Mi Módulo",
    packageName = "com.samsung.android.mimodulo",
    description = "Descripción breve de lo que hace el módulo.",
    category = ModuleCategory.PERSONALIZATION,
    downloadUrl = apkMirrorSearch("Mi Modulo Good Lock"),
    minimumAndroid = "Android 10.0+",
    minimumAndroidSdk = 29,
    supportedOneUI = "One UI 3.0 – 8.x"
)
```

No hace falta tocar ningún archivo de UI: la pantalla principal, la búsqueda y los filtros de categoría leen siempre de este catálogo.

## 🔐 Aviso legal (Disclaimer)

> One Lock es un proyecto independiente y no está afiliado, respaldado ni patrocinado por Samsung Electronics o Good Lock.

Good Lock y sus módulos pertenecen a sus respectivos propietarios. One Lock únicamente proporciona información y enlaces externos hacia las páginas de descarga correspondientes. La app no modifica, descompila, redistribuye ni instala silenciosamente ningún módulo o archivo del sistema.

## 📄 Licencia

Este proyecto se distribuye bajo la licencia [MIT](LICENSE).
