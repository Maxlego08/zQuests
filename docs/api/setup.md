# 🔑 Setup

Add the zQuests API as a *compileOnly* dependency to access the interfaces at compile time.
The artifacts are available from the `groupezReleases` Maven repository.

```kotlin
repositories {
    maven {
        name = "groupezReleases"
        url = uri("https://repo.groupez.dev/releases")
    }
}

dependencies {
    compileOnly("fr.maxlego08.quests:zquests-api:<version>")
}
```

Replace `<version>` with the version of zQuests that is running on the server.
