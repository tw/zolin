package io.warburton.zolin

/**
 * @author tw
 */
fun main(args: Array<String>) {
    val zolin = ZolinFactory.create(System.getenv("MONZO_API_URL"), System.getenv("MONZO_ACCESS_TOKEN"))

    val resp = zolin.accounts().execute()

    val accounts = resp.body()
    println(accounts)

    for ((id) in accounts!!) {
        val balance = zolin.balance(id).execute().body()
        println(balance)

        val transactions = zolin.transactions(id).execute().body()
        for (transaction in transactions!!) {
            println(transaction)
        }
    }
}