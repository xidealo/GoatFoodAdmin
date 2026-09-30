package com.bunbeauty.shared.feature.orderlist.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import com.bunbeauty.shared.feature.orderlist.state.OrderListViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.description_order_list_delivery
import fooddeliveryadmin.shared.generated.resources.description_order_list_pickup
import fooddeliveryadmin.shared.generated.resources.ic_cafe
import fooddeliveryadmin.shared.generated.resources.ic_delivery
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OrderItem(
    modifier: Modifier = Modifier,
    orderItem: OrderListViewState.OrderItem,
    onClick: () -> Unit,
) {
    val backgroundColor =
        if (orderItem.isProblematic) {
            AdminTheme.colors.main.surfaceVariant
        } else {
            AdminTheme.colors.main.surface
        }
    val receiptIcon =
        if (orderItem.isDelivery) {
            Res.drawable.ic_delivery
        } else {
            Res.drawable.ic_cafe
        }
    val receiptDescription =
        if (orderItem.isDelivery) {
            Res.string.description_order_list_delivery
        } else {
            Res.string.description_order_list_pickup
        }

    AdminCard(
        modifier =
            modifier
                .fillMaxWidth(),
        onClick = onClick,
        shape = RectangleShape,
        elevated = false,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(receiptIcon),
                contentDescription = stringResource(receiptDescription),
                modifier = Modifier.size(16.dp),
                tint = AdminTheme.colors.main.onSurfaceVariant,
            )
            Text(
                text = orderItem.code,
                style = AdminTheme.typography.titleMedium.medium,
                color = AdminTheme.colors.main.onSurface,
            )
            OrderStatusChip(orderStatus = orderItem.status, statusName = orderItem.statusString)
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = orderItem.dateTime,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurfaceVariant,
                    textAlign = TextAlign.End,
                )
                Text(
                    text = orderItem.deferredTime,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurface,
                    textAlign = TextAlign.End,
                )
            }
        }
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    }
}

@Preview
@Composable
private fun OrderItemPreview() {
    AdminTheme {
        OrderItem(
            orderItem =
                OrderListViewState.OrderItem(
                    uuid = "",
                    status = OrderStatus.NOT_ACCEPTED,
                    statusString = "Обрабатывается",
                    code = "А-00",
                    deferredTime = "",
                    dateTime = "16 июня 14:00",
                    isProblematic = false,
                    isDelivery = false,
                ),
            onClick = {},
        )
    }
}

@Preview()
@Composable
private fun OrderItemLageFontPreview() {
    AdminTheme {
        OrderItem(
            orderItem =
                OrderListViewState.OrderItem(
                    uuid = "",
                    status = OrderStatus.NOT_ACCEPTED,
                    statusString = "Обрабатывается",
                    code = "А-01",
                    deferredTime = "Ко времени: 15:00",
                    dateTime = "16 июня 14:00",
                    isProblematic = true,
                    isDelivery = true,
                ),
            onClick = {},
        )
    }
}
