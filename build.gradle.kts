plugins {
    java
    maven
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("net.minecrell.plugin-yml.nukkit") version "0.3.0"
}

group = "net.ree_jp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(
        url = uri("https://repo.nukkitx.com/main/")
    )
}

dependencies {
    compileOnly("cn.nukkit:nukkit:1.0-SNAPSHOT")
    testCompileOnly("cn.nukkit:nukkit:1.0-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.xerial:sqlite-jdbc:3.30.1")
    implementation("com.google.code.gson:gson:2.8.6")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

nukkit {
    name = "StackStorage"
    main = "net.ree_jp.storage.StackStoragePlugin"
    api = listOf("1.0.0")
    author = "Ree-jp"
    description = "VerticalStoragePlugin"
    website = "https://github.com/Ree-jp-minecraft/StackStorage_nukkit"
    version = "1.0.0"

    permissions {
        create("stackstorage.*") {
            description = "StackStorage permission"
            default = net.minecrell.pluginyml.nukkit.NukkitPluginDescription.Permission.Default.FALSE
            children {
                create("stackstorage.command.*") {
                    description = "StackStorage command permission"
                    default = net.minecrell.pluginyml.nukkit.NukkitPluginDescription.Permission.Default.TRUE
                }
                create("stackstorage.action.*") {
                    description = "StackStorage action permission"
                    default = net.minecrell.pluginyml.nukkit.NukkitPluginDescription.Permission.Default.TRUE
                    children {
                        create("stackstorage.action.open") {
                            description = "StackStorage action open permission"
                            default = net.minecrell.pluginyml.nukkit.NukkitPluginDescription.Permission.Default.TRUE
                        }
                        create("stackstorage.action.in") {
                            description = "StackStorage action in permission"
                            default = net.minecrell.pluginyml.nukkit.NukkitPluginDescription.Permission.Default.TRUE
                        }
                        create("stackstorage.action.out") {
                            description = "StackStorage action out permission"
                            default = net.minecrell.pluginyml.nukkit.NukkitPluginDescription.Permission.Default.TRUE
                        }
                    }
                }
                create("stackstorage.admin.*") {
                    description = "StackStorage admin permission"
                    default = net.minecrell.pluginyml.nukkit.NukkitPluginDescription.Permission.Default.OP
                }
            }
        }
    }
}