package test.feature.orderlist

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.feature.orderlist.CountTodayOrdersUseCase
import com.bunbeauty.domain.feature.time.TimeService
import com.bunbeauty.domain.model.order.Order
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CountTodayOrdersUseCaseTest {
    private val timeService: TimeService = mockk()
    private val countTodayOrders =
        CountTodayOrdersUseCase(
            timeService = timeService,
        )

    @Test
    fun `counts today delivery and pickup and skips canceled yesterday and unknown timezone`() {
        val dayStartMillis = 1_000L
        every { timeService.getCurrentDayStartMillis(3) } returns dayStartMillis

        val counts =
            countTodayOrders(
                listOf(
                    createOrder(time = dayStartMillis, isDelivery = true, orderStatus = OrderStatus.ACCEPTED),
                    createOrder(time = dayStartMillis, isDelivery = false, orderStatus = OrderStatus.PREPARING),
                    createOrder(time = dayStartMillis, isDelivery = true, orderStatus = OrderStatus.CANCELED),
                    createOrder(time = dayStartMillis - 1, isDelivery = true, orderStatus = OrderStatus.ACCEPTED),
                    createOrder(
                        time = dayStartMillis,
                        isDelivery = false,
                        orderStatus = OrderStatus.ACCEPTED,
                        timeZone = "Europe/Moscow",
                    ),
                ),
            )

        assertEquals(1, counts.deliveryCount)
        assertEquals(1, counts.pickupCount)
    }

    private fun createOrder(
        time: Long,
        isDelivery: Boolean,
        orderStatus: OrderStatus,
        timeZone: String = "UTC+3",
    ): Order =
        Order(
            uuid = "uuid-$time-$isDelivery",
            code = "code",
            time = time,
            deferredTime = null,
            timeZone = timeZone,
            orderStatus = orderStatus,
            isDelivery = isDelivery,
        )
}
