group = "org.black_ixx"
version = "2.1.0"
description = "BossShopPro"

plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.5"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") //Paper
    maven("https://repo.glaremasters.me/repository/public/") //TODO: find way to remove
    maven("https://repo.worldguard.com.au/repository/maven-public") //TODO: find way to remove
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") //PlaceholderAPI
    maven("https://jitpack.io") //Vault, Jobs
    maven("https://nexus.bencodez.com/repository/maven-public/") //VotingPlugin
    maven("https://raw.githubusercontent.com/TeamVK/maven-repository/master/release/") //TokenEnchant
    maven("https://repo.rosewooddev.io/repository/public/") //PlayerPoints
}

dependencies {
    implementation("org.spongepowered:configurate-yaml:4.2.0-SNAPSHOT")

    paperweight.paperDevBundle("1.20-R0.1-SNAPSHOT") //Paper
    //compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT") //Paper
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT") //Bungeecord

    compileOnly("com.bencodez:votingplugin:6.13") //VotingPlugin
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") //Vault
    compileOnly("me.clip:placeholderapi:2.11.3") //PlaceholderAPI
    compileOnly("org.black_ixx:playerpoints:3.2.5") //PlayerPoints
    compileOnly("com.vk2gpz.tokenenchant:TokenEnchantAPI:18.37.1") //TokenEnchant
    compileOnly("com.github.JustEli:Coins:1.10.3") //TODO: new version
    compileOnly("com.yapzhenyie.GadgetsMenu:GadgetsMenu:4.3.32") //GadgetsMenu //TODO: new version
    compileOnly("de.dustplanet.silkspawners.SilkSpawners:SilkSpawners:5.0.2") //SilkSpawners //TODO: new version
    compileOnly("com.meowj:LangUtils:1.6.1") //LangUtils //TODO: new version
    compileOnly("com.github.Realizedd:TokenManager:3.2.4") {
        isTransitive = false
    } //TODO: new version
    compileOnly("com.github.Zrips:Jobs:v4.17.2") {
        isTransitive = false
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

configurations {
    testImplementation {
        extendsFrom(compileOnly.get())
    }
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-parameters")
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    runServer {
        minecraftVersion("1.20")
    }

    shadowJar {
        archiveClassifier.set("")
        minimize()
        fun reloc(pkg: String) = relocate(pkg, "org.black_ixx.relocate.$pkg")
        reloc("io.leangen")
        reloc("org.spongepowered")
        manifest {
            attributes(Pair("Main-Class", "org.black_ixx.bossshop.BossShop"))
        }
    }
}
