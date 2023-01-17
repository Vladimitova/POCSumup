import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.function.Executable

class APIScopeStateTest{
    private var accessToken: String? = null
    private var sumupClient: SumupClient = SumupClient()

    @Before
    fun init() {
        val response = sumupClient.sendPOSTTokenRequest("client_credentials")
        val jsonObject = Json.parseToJsonElement(response.body()).jsonObject
        accessToken = jsonObject["access_token"]?.jsonPrimitive?.contentOrNull
    }

    @Test
    fun testTransactionsState() {
        val response = sumupClient.sendGETTransactionsRequest(accessToken)
        Assertions.assertEquals(200, response.statusCode(), response.body())

        val transactionResponse = Json { ignoreUnknownKeys = true }.decodeFromString<TransactionResponse>(response.body())
        Assertions.assertAll(
            Executable { Assertions.assertEquals(0, transactionResponse.items.size) },
            Executable { Assertions.assertEquals(0, transactionResponse.links.size) }
        )
    }

    @Test
    fun testBankAccount() {
        val response = sumupClient.sendGETBankAccountsRequest(accessToken)
        Assertions.assertEquals(200, response.statusCode(), response.body())

        val accountResponse = Json { ignoreUnknownKeys = true }.decodeFromString<List<BankAccount>>(response.body())
        Assertions.assertAll(
            Executable { Assertions.assertTrue(accountResponse.isNotEmpty()) },
            Executable { Assertions.assertEquals(0, accountResponse[0].account_number) },
            Executable { Assertions.assertEquals(0, accountResponse[0].account_holder_name) }
        )
    }
}