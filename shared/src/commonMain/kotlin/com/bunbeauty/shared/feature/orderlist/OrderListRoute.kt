package com.bunbeauty.shared.feature.orderlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import com.bunbeauty.shared.feature.orderlist.compose.OrderItem
import com.bunbeauty.shared.feature.orderlist.state.OrderList
import com.bunbeauty.shared.feature.orderlist.state.OrderListViewState
import com.bunbeauty.shared.feature.orderlist.state.OrderMapper
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.error_order_list_connection
import fooddeliveryadmin.shared.generated.resources.ic_profile
import fooddeliveryadmin.shared.generated.resources.msg_order_list_delivery
import fooddeliveryadmin.shared.generated.resources.msg_order_list_empty
import fooddeliveryadmin.shared.generated.resources.msg_order_list_pickup
import fooddeliveryadmin.shared.generated.resources.title_order_list_active
import fooddeliveryadmin.shared.generated.resources.title_order_list_canceled
import fooddeliveryadmin.shared.generated.resources.title_order_list_today
import fooddeliveryadmin.shared.generated.resources.title_orders
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val ORDER_LIST_TODAY_KEY = "order_list_today"
private const val ORDER_LIST_EMPTY_KEY = "order_list_empty"
private const val ORDER_LIST_ACTIVE_TITLE_KEY = "order_list_active_title"
private const val ORDER_LIST_CANCELED_TITLE_KEY = "order_list_canceled_title"

@Composable
fun OrderList.DataState.mapStateOrderList(orderMapper: OrderMapper = koinInject()): OrderListViewState =
    OrderListViewState(
        state =
            when (orderListState) {
                OrderList.DataState.State.LOADING -> OrderListViewState.State.Loading
                OrderList.DataState.State.SUCCESS ->
                    OrderListViewState.State.Success(
                        orderList =
                            orderList
                                .map { order ->
                                    orderMapper.map(order)
                                }.toPersistentList(),
                        connectionError = hasConnectionError,
                        refreshing = refreshing,
                        loadingOrderList = loadingOrderList,
                        loadingOrderUpdates = loadingOrderUpdates,
                        deliveryCount = deliveryCount,
                        pickupCount = pickupCount,
                    )
            },
    )

@Composable
fun OrderList.DataState.mapState(): OrderListViewState = mapStateOrderList()

@Composable
fun OrderListRouteScreen(
    viewModel: OrderListViewModel = koinViewModel(),
    cancelNotification: (Int) -> Unit,
    openOrderDetails: (String, String) -> Unit,
    goToProfileScreen: () -> Unit,
) {
    LifecycleStartEffect(Unit) {
        viewModel.onAction(OrderList.Action.StartObserveOrders)
        onStopOrDispose {
            viewModel.onAction(OrderList.Action.StopObserveOrders)
        }
    }

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: OrderList.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }
    val lazyListState = rememberLazyListState()

    OrderListEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        cancelNotification = cancelNotification,
        openOrderDetails = openOrderDetails,
        lazyListState = lazyListState,
    )
    OrderListScreen(
        state = viewState.mapState(),
        lazyListState = lazyListState,
        onAction = onAction,
        goToProfileScreen = goToProfileScreen,
    )
}

@Composable
private fun OrderListEffect(
    effects: List<OrderList.Event>,
    lazyListState: LazyListState,
    cancelNotification: (Int) -> Unit,
    openOrderDetails: (String, String) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                is OrderList.Event.CancelNotification -> {
                    cancelNotification(effect.notificationId)
                }

                is OrderList.Event.OpenOrderDetailsEvent ->
                    openOrderDetails(
                        effect.orderUuid,
                        effect.orderCode,
                    )

                OrderList.Event.ScrollToTop -> {
                    lazyListState.animateScrollToItem(index = 0)
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun OrderListScreen(
    state: OrderListViewState,
    lazyListState: LazyListState,
    onAction: (OrderList.Action) -> Unit,
    goToProfileScreen: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_orders),
        topActions =
            listOf(
                AdminTopBarAction(
                    iconId = Res.drawable.ic_profile,
                    color = AdminTheme.colors.main.primary,
                    onClick = goToProfileScreen,
                ),
            ),
        backgroundColor = AdminTheme.colors.main.surface,
        pullRefreshEnabled = state.state is OrderListViewState.State.Success,
        refreshing = (state.state as? OrderListViewState.State.Success)?.refreshing == true,
        onRefresh = {
            onAction(OrderList.Action.RefreshSwipe)
        },
    ) {
        when (state.state) {
            OrderListViewState.State.Loading -> {
                LoadingScreen()
            }

            is OrderListViewState.State.Success -> {
                OrderListSuccessScreen(
                    state = state.state,
                    lazyListState = lazyListState,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun ConnectionError() {
    Box(
        modifier =
            Modifier
                .background(AdminTheme.colors.status.negative)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.error_order_list_connection),
            style = AdminTheme.typography.bodySmall,
            color = AdminTheme.colors.status.onStatus,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun OrderListSuccessScreen(
    state: OrderListViewState.State.Success,
    lazyListState: LazyListState,
    onAction: (OrderList.Action) -> Unit,
) {
    Column {
        if (state.connectionError) {
            ConnectionError()
        }

        if (state.loadingOrderUpdates) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = AdminTheme.colors.main.primary,
            )
        }

        if (state.loadingOrderList) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = AdminTheme.colors.main.primary,
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding =
                PaddingValues(
                    bottom = 86.dp,
                ),
        ) {
            item(key = ORDER_LIST_TODAY_KEY) {
                TodayOrdersSummary(
                    deliveryCount = state.deliveryCount,
                    pickupCount = state.pickupCount,
                )
            }

            if (state.orderList.isNotEmpty()) {
                val (canceledOrders, activeOrders) =
                    state.orderList.partition { orderItem ->
                        orderItem.status == OrderStatus.CANCELED
                    }

                if (activeOrders.isNotEmpty()) {
                    item(key = ORDER_LIST_ACTIVE_TITLE_KEY) {
                        Text(
                            modifier =
                                Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 16.dp),
                            text = stringResource(Res.string.title_order_list_active),
                            style = AdminTheme.typography.titleMedium.medium,
                        )
                    }
                    items(
                        items = activeOrders,
                        key = { orderItem -> orderItem.uuid },
                    ) { orderItem ->
                        OrderItem(
                            orderItem = orderItem,
                            onClick = {
                                onAction(
                                    OrderList.Action.OrderClick(
                                        orderCode = orderItem.code,
                                        orderUuid = orderItem.uuid,
                                    ),
                                )
                            },
                        )
                    }
                }

                if (canceledOrders.isNotEmpty()) {
                    item(key = ORDER_LIST_CANCELED_TITLE_KEY) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                            text = stringResource(Res.string.title_order_list_canceled),
                            style = AdminTheme.typography.titleMedium.medium,
                        )
                    }
                    items(
                        items = canceledOrders,
                        key = { orderItem -> orderItem.uuid },
                    ) { orderItem ->
                        OrderItem(
                            orderItem = orderItem,
                            onClick = {
                                onAction(
                                    OrderList.Action.OrderClick(
                                        orderCode = orderItem.code,
                                        orderUuid = orderItem.uuid,
                                    ),
                                )
                            },
                        )
                    }
                }
            } else {
                item(key = ORDER_LIST_EMPTY_KEY) {
                    OrderListEmptyLabel()
                }
            }
        }
    }
}

@Composable
private fun OrderListEmptyLabel() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AdminTheme.dimensions.mediumSpace)
                    .align(Alignment.Center),
            text = stringResource(Res.string.msg_order_list_empty),
            style = AdminTheme.typography.titleMedium,
            color = AdminTheme.colors.main.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun TodayOrdersSummary(
    deliveryCount: Int,
    pickupCount: Int,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AdminHorizontalDivider(
            modifier =
                Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                ),
        )
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            text = stringResource(Res.string.title_order_list_today),
            style = AdminTheme.typography.titleMedium.medium,
            color = AdminTheme.colors.main.onSurface,
            textAlign = TextAlign.Center,
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp),
        ) {
            TodayOrderCount(
                modifier = Modifier.weight(1f),
                hint = stringResource(Res.string.msg_order_list_delivery),
                count = deliveryCount,
            )
            TodayOrderCount(
                modifier = Modifier.weight(1f),
                hint = stringResource(Res.string.msg_order_list_pickup),
                count = pickupCount,
            )
        }
        AdminHorizontalDivider(
            modifier =
                Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                ),
        )
    }
}

@Composable
private fun TodayOrderCount(
    hint: String,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = hint,
            style = AdminTheme.typography.labelSmall.medium,
            color = AdminTheme.colors.main.onSurfaceVariant,
        )
        Text(
            modifier =
                Modifier
                    .padding(top = 4.dp),
            text = count.toString(),
            style = AdminTheme.typography.bodyMedium,
            color = AdminTheme.colors.main.onSurface,
        )
    }
}

@Preview()
@Composable
private fun OrderListSuccessScreenPreview() {
    AdminTheme(isDarkTheme = false) {
        OrderListSuccessScreen(
            state =
                OrderListViewState.State.Success(
                    orderList =
                        persistentListOf(
                            OrderListViewState.OrderItem(
                                uuid = "1",
                                status = OrderStatus.ACCEPTED,
                                statusString = "Принят",
                                code = "22",
                                deferredTime = "",
                                dateTime = "12/9/2024",
                                isProblematic = false,
                                isDelivery = false,
                            ),
                            OrderListViewState.OrderItem(
                                uuid = "2",
                                status = OrderStatus.CANCELED,
                                statusString = "Отменен",
                                code = "23",
                                deferredTime = "",
                                dateTime = "12/9/2024",
                                isProblematic = true,
                                isDelivery = true,
                            ),
                        ),
                    connectionError = false,
                    refreshing = false,
                    loadingOrderList = false,
                    loadingOrderUpdates = false,
                    deliveryCount = 25,
                    pickupCount = 13,
                ),
            lazyListState = LazyListState(),
            onAction = {},
        )
    }
}

@Preview()
@Composable
private fun OrderListSuccessScreenDarkPreview() {
    AdminTheme(isDarkTheme = true) {
        OrderListSuccessScreen(
            state =
                OrderListViewState.State.Success(
                    orderList =
                        persistentListOf(
                            OrderListViewState.OrderItem(
                                uuid = "1",
                                status = OrderStatus.ACCEPTED,
                                statusString = "Принят",
                                code = "22",
                                deferredTime = "",
                                dateTime = "12/9/2024",
                                isProblematic = false,
                                isDelivery = false,
                            ),
                            OrderListViewState.OrderItem(
                                uuid = "2",
                                status = OrderStatus.CANCELED,
                                statusString = "Отменен",
                                code = "23",
                                deferredTime = "",
                                dateTime = "12/9/2024",
                                isProblematic = true,
                                isDelivery = true,
                            ),
                        ),
                    connectionError = false,
                    refreshing = false,
                    loadingOrderList = false,
                    loadingOrderUpdates = false,
                    deliveryCount = 25,
                    pickupCount = 13,
                ),
            lazyListState = LazyListState(),
            onAction = {},
        )
    }
}
