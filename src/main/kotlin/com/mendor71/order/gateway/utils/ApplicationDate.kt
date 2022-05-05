package com.mendor71.order.gateway.utils

import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneId

@Component
class ApplicationDate {
    fun offsetDateTime(): OffsetDateTime = OffsetDateTime.now(ZoneId.systemDefault())
}