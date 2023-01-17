import kotlinx.serialization.Serializable

@Serializable
data class BankAccount (
    val bank_code: String,
    val branch_code: String,
    val swift: String,
    val account_number: String,
    val iban: String,
    val account_type: String,
    val account_category: String,
    val account_holder_name: String,
    val status: String,
    val primary: Boolean,
    val created_at: String,
    val bank_name: String
)