# zolin
A Kotlin client for the Monzo API - built using Retrofit.

### Example Usage
```kotlin
val zolin = ZolinFactory.create(System.getenv("MONZO_API_URL"), System.getenv("MONZO_ACCESS_TOKEN"))

val accounts = zolin.accounts().execute().body()!!.accounts
val current = accounts[0]
val balance = zolin.balance(current.id).execute().body()!!

println(balance)

> Balance(currency=GBP, balance=6351, spendToday=-923)
```

### TODO
* Handle deserialization a little better, getting rid of the need for ModelResponse classes.
* Remove Guava dependency for tests (it's overkill given the usage).
