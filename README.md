Starbox [![](https://jitpack.io/v/me.googas/starbox.svg)](https://jitpack.io/#me.googas/starbox)
===

Utility library to speedup the creation of Java projects.

<img align="right" src="https://github.com/Chevyself/starbox/blob/master/assets/starbox.svg?raw=true" height="200" width="200">

Summary
--------
The purpose of this project is sharing code which may help developers in their own software. It is mostly based around [Bukkit/Spigot](https://www.spigotmc.org/), there's a module called `Bukkit` which contains utility to mod such type of Minecraft server. This project
does have a command framework which is located in the repository [Starbox-Commands](https://github.com/Chevyself/Starbox-Commands).

Documentation
--------
Latest [JavaDoc](https://jitpack.io/com/github/Chevyself/starbox/master-SNAPSHOT/javadoc/)

Getting Starbox
--------
**Maven**
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependencies>
    <dependency>
        <groupId>me.googas.starbox</groupId>
        <artifactId>module</artifactId>
        <version>Tag</version>
    </dependency>
</dependencies>
```
**Gradle**
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
```gradle
dependencies {
    implementation 'me.googas.starbox:module:Tag'
}
```
Check [Jitpack](https://jitpack.io/#me.googas/starbox) to know more