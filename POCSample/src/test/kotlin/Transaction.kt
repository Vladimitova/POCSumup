import kotlinx.serialization.Serializable

@Serializable
data class Transaction (
    val id: String,
    val transaction_code: String,
    val amount: Int,
    val currency: String,
    val timestamp: String,
    val status: String,
    val payment_type: String,
    val installments_count: Int,
    val product_summary: String,
    val payouts_total: Int,
    val payouts_received: Int,
    val payout_plan: String,
    val transaction_id: String,
    val user: String,
    val type: String,
    val card_type: String
)

@Serializable
data class TransactionLink(
    val rel: String,
    val href: String,
    val type: String
)

@Serializable
data class TransactionResponse(
    val items: List<Transaction>,
    val links: List<TransactionLink>
)