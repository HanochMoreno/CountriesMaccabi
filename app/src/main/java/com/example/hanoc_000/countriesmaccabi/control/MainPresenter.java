package com.example.hanoc_000.countriesmaccabi.control;

import android.support.annotation.NonNull;

import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.rest_countries_api.ApiUtil;
import com.example.hanoc_000.countriesmaccabi.rest_countries_api.RestCountriesApi;
import com.example.hanoc_000.countriesmaccabi.rest_countries_api.RestCountriesRemoteApi;
import com.example.hanoc_000.countriesmaccabi.view.CountryBordersActivity;
import com.example.hanoc_000.countriesmaccabi.view.MainActivity;
import com.example.hanoc_000.countriesmaccabi.view.MyActivity;

import java.util.ArrayList;

import rx.Scheduler;
import rx.Single;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * The unit that connects between the data provider (RestCountries API) and the views (activities).
 */
public class MainPresenter implements MainContract.Presenter {

    /**
     * The Retrofit description of the RestCountries API endpoints.
     */
    @NonNull
    private RestCountriesApi api;

    /**
     * The Scheduler on which our API request will operate.
     */
    @NonNull
    private Scheduler backgroundScheduler;

    /**
     * the Scheduler on which we want our observer to wait for the API request callbacks.
     */
    @NonNull
    private Scheduler mainScheduler;

    @NonNull
    private CompositeSubscription subscriptions;

    /**
     * An instance (can be a mock) of MyActivity type Activity.
     * Could be MainActivity or CountryBorderActivity.
     */
    private MyActivity view;

//-------------------------------------------------------------------------------------------------

    /**
     * CTOR for production environment.
     */
    public MainPresenter(MyActivity view) {

        this(new RestCountriesRemoteApi(), Schedulers.io(), AndroidSchedulers.mainThread(), view);
    }

//-------------------------------------------------------------------------------------------------

    /**
     * CTOR for unit-testing environment.
     */
    public MainPresenter(
            @NonNull RestCountriesApi api,
            @NonNull Scheduler backgroundScheduler,
            @NonNull Scheduler mainScheduler,
            MyActivity view) {

        this.api = api;
        this.backgroundScheduler = backgroundScheduler;
        this.mainScheduler = mainScheduler;
        this.view = view;
        subscriptions = new CompositeSubscription();
    }

//-------------------------------------------------------------------------------------------------

    /**
     * The activity is ready to receive the data.
     */
    @Override
    public void loadData() {
        view.onFetchDataStarted();
        subscriptions.clear();

        Subscriber<ArrayList<Country>> subscriber = new Subscriber<ArrayList<Country>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable t) {
                view.onFetchDataError(t);
            }

            @Override
            public void onNext(ArrayList<Country> countriesList) {
                view.onFetchDataSuccess(countriesList);
            }
        };

        String[] requiredFields;
        String requiredFieldsQuery;
        Single<ArrayList<Country>> task;

        if (view instanceof MainActivity) {
            // MainActivity
            requiredFields = new String[]{"name", "alpha3Code", "nativeName", "flag", "borders"};
            requiredFieldsQuery = ApiUtil.getStringQuery(requiredFields);
            task = api.getAllCountries(requiredFieldsQuery);

        } else {
            // BordersCountryActivity
            requiredFields = new String[]{"name", "alpha3Code", "nativeName", "flag"};
            String[] borderCountriesCodes = ((CountryBordersActivity) view).borders;

            requiredFieldsQuery = ApiUtil.getStringQuery(requiredFields);
            String requiredCountriesQuery = ApiUtil.getStringQuery(borderCountriesCodes);
            task = api.getCountriesDetails(requiredFieldsQuery, requiredCountriesQuery);
        }

        Subscription subscription = task
                .subscribeOn(backgroundScheduler)
                .observeOn(mainScheduler)
                .subscribe(subscriber);

        subscriptions.add(subscription);
    }

//-------------------------------------------------------------------------------------------------

    @Override
    public void subscribe() {
        loadData();
    }

//-------------------------------------------------------------------------------------------------

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

//-------------------------------------------------------------------------------------------------

    /**
     * The activity is being destroyed.
     */
    @Override
    public void onDestroy() {
        this.view = null;
    }

}
