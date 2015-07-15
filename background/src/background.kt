package ru.geevee.mapswitch.background

import ru.geevee.mapswitch.getApplicableService

native val chrome: dynamic

fun main(args: Array<String>) {
    chrome.tabs.onUpdated.addListener {
        tabId, info, tab ->
        val url: String = tab.url
        if (getApplicableService(url) != null) {
            chrome.pageAction.show(tabId)
        }
        else {
            chrome.pageAction.hide(tabId)
        }
    }
}