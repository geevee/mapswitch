package ru.geevee.mapswitch.popup

import ru.geevee.mapswitch.Coordinates
import ru.geevee.mapswitch.MapService
import ru.geevee.mapswitch.getApplicableService
import ru.geevee.mapswitch.services
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.onClick

native val chrome: dynamic

fun main(args: Array<String>) {
    document.addEventListener("DOMContentLoaded", {
        chrome.tabs.query(object { val active = true }) {
            tabs ->
            val tab = tabs[0]
            val currentUrl = tab.url

            val mapService = getApplicableService(currentUrl)
            if (mapService == null) {
                window.close()
            } else {
                val coordinates = mapService.extractCoordinates(currentUrl)
                generateServicesList(coordinates, tab.id, services.filter { it != mapService })
            }
        }
    })
}

private fun generateServicesList(coordinates: Coordinates, tabId: Int, services: List<MapService>) {
    val servicesDomElement = document.getElementById("services")!!
    for (service in services) {
        val newUrl = service.getUrl(coordinates)

        val listItemElement = document.createElement("li")
        listItemElement.innerHTML = "<a href=\"$newUrl\">${service.name}</a>"
        listItemElement.getElementsByTagName("a")[0].onClick {
            mouseEvent ->
            with(mouseEvent) {
                if (button == 0.toShort() && !altKey && !ctrlKey && !metaKey && !shiftKey && detail == 1) {
                    chrome.tabs.update(tabId, object { val url = newUrl })
                    window.close()
                }
            }
        }
        servicesDomElement.appendChild(listItemElement)
    }
}