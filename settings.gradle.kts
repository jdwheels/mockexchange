pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        gradlePluginPortal()
    }
}

rootProject.name = "mockexchange"

include(
    ":mockexchange-populator",
    ":mockexchange-objects",
    ":mockexchange-posts-api",
    ":mockexchange-search",
)
