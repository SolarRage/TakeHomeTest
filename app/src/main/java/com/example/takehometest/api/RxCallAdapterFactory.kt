package com.example.takehometest.api

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

//rxJava to work with retrofit instead of retrofit Call
class RxCallAdapterFactory : CallAdapter.Factory() {
    private val originalCallAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        return RxCallAdapterWrapper(originalCallAdapterFactory.get(returnType, annotations, retrofit)!!)
    }

    private class RxCallAdapterWrapper<R, T>(private val wrapped: CallAdapter<R, T>) : CallAdapter<R, T> {

        override fun responseType(): Type {
            return wrapped.responseType()
        }

        override fun adapt(call: Call<R>): T {
            val adaptedObj: T = wrapped.adapt(call)

            if (adaptedObj is Single<*>) {
                val single = adaptedObj as Single<*>
                return single as T
            }

            if (adaptedObj is Observable<*>) {
                val observable = adaptedObj as Observable<*>
                return observable as T
            }

            if (adaptedObj is Flowable<*>) {
                val flowable = adaptedObj as Flowable<*>
                return flowable as T
            }

            if (adaptedObj is Completable) {
                val completable = adaptedObj as Completable
                return completable as T
            }

            if (adaptedObj is Maybe<*>) {
                val maybe = adaptedObj as Maybe<*>
                return maybe as T
            }
            return adaptedObj
        }
    }
}