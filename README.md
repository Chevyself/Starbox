Starbox [![Jitpack](https://jitpack.io/v/xChevy/Starbox.svg)](https://jitpack.io/#xChevy/Starbox)
===

Utility library to speedup the creation of Java projects.

Summary
--------
The purpose of this project is sharing code which may help developers in their own software. It is mostly based around [Bukkit/Spigot](https://www.spigotmc.org/), there's a module called `Bukkit` which contains utility to mod such type of Minecraft server. This project
does have a command framework which is located in the repository [Starbox-Commands](https://github.com/Chevyself/Starbox-Commands).

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
        <groupId>com.github.Chevyself</groupId>
        <artifactId>Starbox</artifactId>
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
    implementation 'com.github.Chevyself:Starbox:Tag'
}
```
Check [Jitpack](https://jitpack.io/#Chevyself/Starbox/) to know more