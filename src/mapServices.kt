package ru.geevee.mapswitch

import kotlin.text.Regex

interface MapService {
    val name: String
    fun detect(url: String): Boolean
    fun extractCoordinates(url: String): Coordinates
    fun getUrl(coordinates: Coordinates): String
}

fun getApplicableService(url: String): MapService? {
    return services.firstOrNull { it.detect(url) }
}


object GoogleMaps: MapService {
    override val name: String
        get() = "Google"

    override fun detect(url: String): Boolean {
        return "maps" in url && "google" in url // TODO not very smart detector
    }

    override fun extractCoordinates(url: String): Coordinates {
        val match = Regex("@([\\d.]+),([\\d.]+),([\\d.]+)z").matchAll(url).first()
        val latitude = match.groups[1]!!.value
        val longitude = match.groups[2]!!.value
        val zoomLevel = match.groups[3]!!.value

        return Coordinates(latitude, longitude, zoomLevel)
    }

    override fun getUrl(coordinates: Coordinates): String {
        return with(coordinates) {
            "https://www.google.com/maps/@$latitude,$longitude,${zoomLevel}z"
        }
    }
}

object YandexMaps: MapService {
    override val name: String
        get() = "Yandex"

    override fun detect(url: String): Boolean {
        return "maps" in url && "yandex" in url // TODO not very smart detector
    }

    override fun extractCoordinates(url: String): Coordinates {
        val match = Regex("\\?ll=([\\d.]+)%2C([\\d.]+)&z=([\\d.]+)").matchAll(url).first()
        val latitude = match.groups[2]!!.value
        val longitude = match.groups[1]!!.value
        val zoomLevel = match.groups[3]!!.value

        return Coordinates(latitude, longitude, zoomLevel)
    }

    override fun getUrl(coordinates: Coordinates): String {
        return with(coordinates) {
            "https://maps.yandex.ru/?ll=$longitude%2C$latitude&z=$zoomLevel"
        }
    }
}

object OpenStreetMap: MapService {
    override val name: String
        get() = "OpenStreetMap"

    override fun detect(url: String): Boolean {
        return "openstreetmap.org" in url // TODO not very smart detector
    }

    override fun extractCoordinates(url: String): Coordinates {
        val match = Regex("#map=([\\d.]+)\\/([\\d.]+)\\/([\\d.]+)").matchAll(url).first()
        val latitude = match.groups[2]!!.value
        val longitude = match.groups[3]!!.value
        val zoomLevel = match.groups[1]!!.value

        return Coordinates(latitude, longitude, zoomLevel)
    }

    override fun getUrl(coordinates: Coordinates): String {
        return with(coordinates) {
            "http://www.openstreetmap.org/#map=$zoomLevel/$latitude/$longitude"
        }
    }
}

object Wikimapia: MapService {
    override val name: String
        get() = "Wikimapia"

    override fun detect(url: String): Boolean {
        return "wikimapia.org" in url // TODO not very smart detector
    }

    override fun extractCoordinates(url: String): Coordinates {
        val match = Regex("lat=([\\d.]+)&lon=([\\d.]+)&z=([\\d.]+)").matchAll(url).first()
        val latitude = match.groups[1]!!.value
        val longitude = match.groups[2]!!.value
        val zoomLevel = match.groups[3]!!.value

        return Coordinates(latitude, longitude, zoomLevel)
    }

    override fun getUrl(coordinates: Coordinates): String {
        return with(coordinates) {
            "http://wikimapia.org/#lat=$latitude&lon=$longitude&z=$zoomLevel"
        }
    }
}

// FIXME: property is here because of KT-4144: objects can be used only after they are declared
val services = listOf(GoogleMaps, YandexMaps, OpenStreetMap, Wikimapia)
