/**
 * Created by CbaElectronics by Eduardo Sanchez on 18/11/22 10:29.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.addMatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.*
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.DEFAULT_COUNT_REQUEST
import com.cbaelectronics.turinpadel.util.Constants.PLAYER_AVAILABLE_NO
import com.cbaelectronics.turinpadel.util.Constants.PLAYER_AVAILABLE_YES
import kotlinx.coroutines.runBlocking

class AddMatchViewModel: ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val addMatchTitle = R.string.addMatches_title
    val addMatchInfo = R.string.addMatches_info
    val addMatchFechaPlaceholder = R.string.addMatches_editText_fecha
    val addMatchVacantesPlaceholder = R.string.addMatches_editText_vacantes
    val addMatchCategoryPlaceholder = R.string.addMatches_editText_category
    val addMatchGenrePlaceholder = R.string.addMatches_editText_genre
    val addMatchButton = R.string.addMatches_button
    val alertWarning = R.string.addMatches_alert_warning
    val alertOk = R.string.addMatches_alert_ok
    val alertError = R.string.addMatches_alert_error
    val alertIncomplete = R.string.addMatches_alert_incomplete
    val notificationTitle = R.string.notification_topic_newmatch_title
    val notificationBody = R.string.notification_topic_newmatch_body

    // Public

    fun save(match: Match) = runBlocking {
        val documentReference = FirebaseDBService.save(match)
        val matchId = documentReference.id
        var player1: User
        val player2: User
        val player3: User
        val player4: User

        when(match.vacantes){
            0 -> {
                player1 = getPlayerNotAvailable()
                player2 = getPlayerNotAvailable()
                player3 = getPlayerNotAvailable()
                player4 = getPlayerNotAvailable()
            }
            1 -> {
                player1 = getPlayerNotAvailable()
                player2 = getPlayerNotAvailable()
                player3 = getPlayerNotAvailable()
                player4 = getPlayerAvailable()
            }
            2 -> {
                player1 = getPlayerNotAvailable()
                player2 = getPlayerNotAvailable()
                player3 = getPlayerAvailable()
                player4 = getPlayerAvailable()
            }
            3 -> {
                player1 = getPlayerNotAvailable()
                player2 = getPlayerAvailable()
                player3 = getPlayerAvailable()
                player4 = getPlayerAvailable()
            }
            else -> {
                player1 = getPlayerAvailable()
                player2 = getPlayerAvailable()
                player3 = getPlayerAvailable()
                player4 = getPlayerAvailable()
            }
        }


        val matchDetails = MatchDetails(matchId, user, match.date, match.genre, match.category, match.vacantes, DEFAULT_COUNT_REQUEST, player1, player2, player3, player4)
        FirebaseDBService.save(matchDetails)
    }

    // Private

    private fun getPlayerAvailable(): User{
        return User(PLAYER_AVAILABLE_YES, registerDate = null)
    }

    private fun getPlayerNotAvailable(): User{
        return User(PLAYER_AVAILABLE_NO, registerDate = null)
    }


}