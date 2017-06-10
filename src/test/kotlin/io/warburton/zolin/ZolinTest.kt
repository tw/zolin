package io.warburton.zolin

import com.google.common.io.Resources
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import java.nio.charset.Charset

/**
 * @author tw
 */
class ZolinTest {
    val server: MockWebServer = MockWebServer()
    val dateFormat: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val zolin: Zolin = ZolinFactory.create("http://${server.hostName}:${server.port}", "abc-123-def")

    fun mock(response: Int, url: String) {
        val content = Resources.toString(Resources.getResource("mocks/$url.json"), Charset.forName("UTF-8"))
        server.enqueue(MockResponse()
                .setResponseCode(response)
                .setHeader("Content-Type", "application/json")
                .setBody(content))
    }

    @After
    fun down() {
        server.shutdown()
    }

    @Test
    fun accounts() {
        mock(200, "accounts")

        var resp = zolin.accounts().execute()
        assertEquals(200, resp.code())
        assertNotNull(resp.body())

        var accounts = resp.body()!!.accounts
        assertEquals(1, accounts.size)

        val account = accounts[0]
        assertEquals("acc_000010A3za4so3kVo7PzSE", account.id)
        assertEquals("Mr Joe Blogs", account.description)
        assertEquals("uk_prepaid", account.type)
        assertEquals(dateFormat.withOffsetParsed().parseDateTime("2016-07-10T10:37:20.123Z"), account.created)

        mock(200, "accounts.empty")

        resp = zolin.accounts().execute()
        assertEquals(200, resp.code())
        assertNotNull(resp.body())

        accounts = resp.body()!!.accounts
        assertEquals(0, accounts.size)
    }

    @Test
    fun balance() {
        mock(200, "balance")

        val resp = zolin.balance("acc_000010A3za4so3kVo7PzSE").execute()
        assertEquals(200, resp.code())
        assertNotNull(resp.body())

        val balance = resp.body()!!
        println(balance)
        assertEquals(balance.balance, 6351)
        assertEquals(balance.currency, "GBP")
        assertEquals(balance.spendToday, -923)
    }

    @Test
    fun transactions() {
        mock(200, "transactions")

        val resp = zolin.transactions("acc_000010A3za4so3kVo7PzSE").execute()
        assertEquals(200, resp.code())
        assertNotNull(resp.body())

        val transactions = resp.body()!!.transactions
        assertEquals(transactions.size, 3)

        transactions.forEach { t ->
            if (t.isLoad) {
                assertNull(t.merchant)
            } else {
                assertNotNull(t.merchant)
            }
        }
    }

}