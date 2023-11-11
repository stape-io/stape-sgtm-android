package io.stape.sgtm.utils

/**
 * Extension method to filter out null values from a map.
 *
 * @param K The type of keys contained in the map.
 * @param V The type of values contained in the map.
 * @return A map with no null values.
 */
internal fun <K, V> Map<K, V?>.filterNullValues(): Map<K, V> {
    return filterValues { it != null }
        .mapValues { it.value!! }
}
