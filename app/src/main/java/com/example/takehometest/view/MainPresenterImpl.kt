package com.example.takehometest.view

import android.util.Log
import com.example.takehometest.api.responses.PodcastResponseItem
import com.example.takehometest.repository.PodcastsRepository
import com.example.takehometest.repository.PodcastsRepositoryImpl
import com.example.takehometest.view.PodcastsAdapter.PodcastItemModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit.MILLISECONDS

class MainPresenterImpl : MainPresenter {

    private val TAG = MainPresenterImpl::class.simpleName

    private lateinit var view: MainView

    private val textChangeSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    private val compositeDisposable = CompositeDisposable()
    private var podcastsDisposable: Disposable? = null

    private val podcastsRepository: PodcastsRepository = PodcastsRepositoryImpl()

    init {
        subscribeForTestChanges()
    }

    override fun bind(view: MainView) {
        this.view = view
    }

    override fun onTextChange(text: String) {
        textChangeSubject.onNext(text)
    }

    /**
     * Метод реализует задержку в 500 мил/сек
     * и проверяет введенное значение на совпадение с предидущим, что бы не запускать повторный поиск
     * Метод обрабатывает исклчения подписки.
     */
    private fun subscribeForTestChanges() {
        compositeDisposable += textChangeSubject
            .debounce(500, MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { handleTextChanged(it) },
                { Log.e(TAG, it.message.toString()) }
            )
    }

    private fun handleTextChanged(text: String) {
        view.setPodcasts(emptyList())
        disposePodcastsRequest()
        podcastsDisposable = podcastsRepository.getPodcasts(text)
            .flattenAsFlowable { it } //foreach
            .map{ mapToItemModel(it) }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showProgress(true) }
            .doOnEvent { _, _ -> view.showProgress(false) }
            .subscribe(
                ::handlePodcastsReceived
            ) { Log.e(TAG, "Error while receiving podcasts") }
    }

    private fun mapToItemModel(item: PodcastResponseItem) = PodcastItemModel(
        item.id,
        item.title,
        item.publisherName,
        item.categoryName,
        item.images?.thumbnail
    )

    private fun handlePodcastsReceived(podcasts: List<PodcastItemModel>) {
        view.setPodcasts(podcasts)
        view.showEmptyView(podcasts.isEmpty())
    }

    private fun disposePodcastsRequest() {
        podcastsDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    /**
     * Переопределенный метод, отменят подписку на событие
     */
    override fun onDestroy() {
        compositeDisposable.clear()
        disposePodcastsRequest()
    }
}