import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class SumupClient {
    private val credentialsFile = "sumup-client-credentials.json"
    private val uri: String = "https://api.sumup.com"
    private val redirectUri: String = "https://poc.sample.com/callback"

    private val client: HttpClient = HttpClient.newHttpClient()

    private fun getClientParameters(): MutableMap<String, String?>{
        val jsonObject = Json.parseToJsonElement( SumupClient::class.java.getResource(credentialsFile).readText()).jsonObject
        val result = mutableMapOf<String, String?>()
        result["client_id"] = jsonObject["client_id"]?.jsonPrimitive?.contentOrNull
        result["client_secret"] = jsonObject["client_secret"]?.jsonPrimitive?.contentOrNull
        return result
    }

    fun sendPOSTTokenRequest(grantType: String?): HttpResponse<String>{
        val request = HttpRequest.newBuilder()
            .uri(URI("$uri/token"))
            .POST(getParamsUrlEncoded(getClientParameters().apply { put("grant_type", grantType)} ))
            .headers("Content-Type", "application/x-www-form-urlencoded")
            .build()

        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun sendGETTransactionsRequest(accessToken: String?): HttpResponse<String>{
        val request = HttpRequest.newBuilder()
            .uri(URI("$uri/v0.1/me/transactions/history"))
            .header("Accept", "application/json")
            .header("Authorization", "Bearer $accessToken")
            .GET()
            .build()

        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun sendGETBankAccountsRequest(accessToken: String?): HttpResponse<String>{
        val request = HttpRequest.newBuilder()
            .uri(URI("$uri/v0.1/me/merchant-profile/bank-accounts"))
            .header("Accept", "application/json")
            .header("Authorization", "Bearer $accessToken")
            .GET()
            .build()

        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun sendGETAuthorizationRequest(responseType: String): HttpResponse<String>{
        val clientId = getClientParameters()["client_id"]
        val request = HttpRequest.newBuilder()
            .uri(URI("$uri/authorize?response_type=$responseType&client_id=$clientId&redirect_uri=$redirectUri&scope=payments%20user.app-settings%20transactions.history%20user.profile_readonl"))
            .header("Accept", "application/json")
            .GET()
            .build()

        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private fun getParamsUrlEncoded(parameters: Map<String, String?>): HttpRequest.BodyPublisher? {
        val urlEncoded: String = parameters.entries
            .stream()
            .map { (key, value): Map.Entry<String, String?> ->
                "$key=" + URLEncoder.encode(
                    value,
                    StandardCharsets.UTF_8
                )
            }
            .collect(Collectors.joining("&"))
        return HttpRequest.BodyPublishers.ofString(urlEncoded)
    }
}