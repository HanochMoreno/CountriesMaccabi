package com.example.hanoc_000.countriesmaccabi.control;

import com.example.hanoc_000.countriesmaccabi.model.Country;

import java.util.ArrayList;

/**
 * The contract between the presenter (controller) and the view.
 */
public interface MainContract {

    /**
     * An activity.
     */
    interface View {

        /**
         * The view's presenter tells the view he started fetching data from the API.
         */
        void onFetchDataStarted();

        /**
         * The view's presenter tells the view that no more data will be received from the API.
         */
        void onFetchDataCompleted();

        /**
         * The view's presenter tells the view the data from the API is received successfully.
         *
         * @param countries: A list of countries received with the API response.
         */
        void onFetchDataSuccess(ArrayList<Country> countries);

        /**
         * The view's presenter tells the view that he ran into an error while trying
         * to get the data from the API.
         *
         * @param e: The error details object.
         */
        void onFetchDataError(Throwable e);
    }

    interface Presenter {

        /**
         * Tell the presenter to start fetching data from the RestCountries API and notify the
         * view in case of success or error.
         */
        void loadData();

        /**
         * Notify the presenter that its view has become active.
         * This is used to trigger the API request.
         */
        void subscribe();

        /**
         * Notify the presenter that its view has become inactive.
         * This is used to cancel any previous request that hasn't returned yet.
         */
        void unSubscribe();

        /**
         * Notify the presenter that its view is going to be destroyed.
         */
        void onDestroy();
    }
}