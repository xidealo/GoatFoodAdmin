package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.feature.time.TimeService
import com.bunbeauty.domain.model.order.Order

class CountTodayOrdersUseCase(
    private val timeService: TimeService,
) {
    operator fun invoke(orderList: List<Order>): TodayOrderCounts {
        val countedOrders =
            orderList.filter { order ->
                order.isCountedToday()
            }
        return TodayOrderCounts(
            deliveryCount = countedOrders.count { order -> order.isDelivery },
            pickupCount = countedOrders.count { order -> !order.isDelivery },
        )
    }

    private fun Order.isCountedToday(): Boolean {
        val offsetHours = timeZone.toUtcOffsetHours() ?: return false
        if (orderStatus == OrderStatus.CANCELED) {
            return false
        }
        val dayStartMillis = timeService.getCurrentDayStartMillis(offsetHours)
        return time >= dayStartMillis
    }

    private fun String.toUtcOffsetHours(): Int? {
        val match = UTC_OFFSET_REGEX.matchEntire(this) ?: return null
        val hours = match.groupValues[2].toInt()
        return if (match.groupValues[1] == "+") {
            hours
        } else {
            -hours
        }
    }

    private companion object {
        val UTC_OFFSET_REGEX = Regex("""UTC([+-])(\d{1,2})""")
    }
}

data class TodayOrderCounts(
    val deliveryCount: Int,
    val pickupCount: Int,
)
