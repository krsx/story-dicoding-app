package com.example.proyekakhirstoryapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.proyekakhirstoryapp.ui.map.mapstyle.MapStyle
import com.example.proyekakhirstoryapp.ui.map.mapstyle.MapType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreference constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }

    fun getUserToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_TOKEN_KEY] ?: ""
        }
    }

    fun getMapType(): Flow<MapType> = dataStore.data.map {
        when (it[MAP_TYPE_KEY]) {
            MapType.NORMAL.name -> MapType.NORMAL
            MapType.SATELLITE.name -> MapType.SATELLITE
            MapType.TERRAIN.name -> MapType.TERRAIN
            else -> MapType.NORMAL
        }
    }

    suspend fun saveMapType(mapType: MapType) {
        dataStore.edit {
            it[MAP_TYPE_KEY] = when (mapType) {
                MapType.NORMAL -> MapType.NORMAL.name
                MapType.SATELLITE -> MapType.SATELLITE.name
                MapType.TERRAIN -> MapType.TERRAIN.name
            }
        }
    }

    fun getMapStyle(): Flow<MapStyle> = dataStore.data.map {
        when (it[MAP_STYLE_KEY]) {
            MapStyle.NORMAL.name -> MapStyle.NORMAL
            MapStyle.NIGHT.name -> MapStyle.NIGHT
            MapStyle.SILVER.name -> MapStyle.SILVER
            else -> MapStyle.NORMAL
        }
    }

    suspend fun saveMapStyle(mapStyle: MapStyle) {
        dataStore.edit {
            it[MAP_STYLE_KEY] = when (mapStyle) {
                MapStyle.NORMAL -> MapStyle.NORMAL.name
                MapStyle.NIGHT -> MapStyle.NIGHT.name
                MapStyle.SILVER -> MapStyle.SILVER.name
            }
        }
    }

    suspend fun clearCache() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token_key")
        private val MAP_TYPE_KEY = stringPreferencesKey("map_type")
        private val MAP_STYLE_KEY = stringPreferencesKey("map_style")

        @Volatile
        private var INSTANCE: SettingPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}