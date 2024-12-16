import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//acceder a DataStore
val Context.dataStore by preferencesDataStore("tamagotchi_preferences")

object TamagotchiPreferences {

    private val HUNGER_KEY = intPreferencesKey("hunger")
    private val HAPPINESS_KEY = intPreferencesKey("happiness")
    private val ENERGY_KEY = intPreferencesKey("energy")

    // obtener valores almacenados
    fun getHunger(context: Context): Flow<Int> =
        context.dataStore.data.map { preferences -> preferences[HUNGER_KEY] ?: 100 }

    fun getHappiness(context: Context): Flow<Int> =
        context.dataStore.data.map { preferences -> preferences[HAPPINESS_KEY] ?: 100 }

    fun getEnergy(context: Context): Flow<Int> =
        context.dataStore.data.map { preferences -> preferences[ENERGY_KEY] ?: 100 }

    // guardar valores
    suspend fun saveHunger(context: Context, value: Int) {
        context.dataStore.edit { it[HUNGER_KEY] = value }
    }

    suspend fun saveHappiness(context: Context, value: Int) {
        context.dataStore.edit { it[HAPPINESS_KEY] = value }
    }

    suspend fun saveEnergy(context: Context, value: Int) {
        context.dataStore.edit { it[ENERGY_KEY] = value }
    }
}
