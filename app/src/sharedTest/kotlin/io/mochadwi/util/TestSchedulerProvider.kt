package io.mochadwi.util

import io.mochadwi.util.rx.SchedulerProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestSchedulerProvider : SchedulerProvider {
    override fun ui(): TestCoroutineDispatcher = TestCoroutineDispatcher()
}