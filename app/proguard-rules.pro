# Add project specific ProGuard rules here.
# One Lock does not ship obfuscated release builds by default.
# If you enable minification, uncomment/adjust rules below as needed.

# Keep data model classes intact (used for the module catalog)
-keep class com.arngmods93.onelock.data.model.** { *; }
