package com.example.hanoc_000.countriesmaccabi;

import com.example.hanoc_000.countriesmaccabi.control.MainPresenter;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.rest_countries_api.ApiUtil;
import com.example.hanoc_000.countriesmaccabi.rest_countries_api.RestCountriesApi;
import com.example.hanoc_000.countriesmaccabi.view.CountryBordersActivity;
import com.example.hanoc_000.countriesmaccabi.view.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import rx.Single;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing the main presenter.
 */
public class MainPresenterTest {

    /**
     * A list of API request testing countries, represented by their ISO 3166-1 codes.
     */
    private final String[] TEST_COUNTRIES = new String[]{"lbn", "syr", "jor", "afg"};

    /**
     * A fake list of API response testing countries.
     */
    private final ArrayList<Country> FAKE_COUNTRIES_RESPONSE = new ArrayList<Country>() {
        {
            add(new Country("LBN", "Lebanon", "لبنان", "https://restcountries.eu/data/lbn.svg"));
            add(new Country("SYR", "Syrian Arab Republic", "سوريا", "https://restcountries.eu/data/syr.svg"));
            add(new Country("JOR", "Jordan", "الأردن", "https://restcountries.eu/data/jor.svg"));
            add(new Country("AFG", "Afghanistan", "افغانستان", "https://restcountries.eu/data/afg.svg"));
        }
    };

    @Mock
    private RestCountriesApi restCountriesApi;

    @Mock
    private MainActivity mainActivity;

    @Mock
    private CountryBordersActivity countryBordersActivity;

//-------------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

//-------------------------------------------------------------------------------------------------

    @Test
    public void fetchValidDataShouldLoadInto_CountryBordersActivity_View() {

        String fieldsFiltersQuery = ApiUtil.getStringQuery(ApiUtil.BORDER_COUNTRIES_ACTIVITY_REQUIRED_FIELDS);
        String countriesCodesQuery = ApiUtil.getStringQuery(TEST_COUNTRIES);

        when(restCountriesApi.getCountriesDetails(fieldsFiltersQuery, countriesCodesQuery))
                .thenReturn(Single.just(FAKE_COUNTRIES_RESPONSE));

        countryBordersActivity.borders = TEST_COUNTRIES;

        MainPresenter mainPresenter = new MainPresenter(
                restCountriesApi,
                Schedulers.immediate(),
                Schedulers.immediate(),
                countryBordersActivity
        );

        mainPresenter.loadData();

        InOrder inOrder = Mockito.inOrder(countryBordersActivity);
        inOrder.verify(countryBordersActivity, times(1)).onFetchDataStarted();
        inOrder.verify(countryBordersActivity, times(1)).onFetchDataSuccess(FAKE_COUNTRIES_RESPONSE);
        inOrder.verify(countryBordersActivity, times(1)).onFetchDataCompleted();
    }

//-------------------------------------------------------------------------------------------------

    @Test
    public void fetchErrorShouldReturnErrorTo_CountryBordersActivity_View() {

        String fieldsFiltersQuery = ApiUtil.getStringQuery(ApiUtil.BORDER_COUNTRIES_ACTIVITY_REQUIRED_FIELDS);
        String countriesCodesQuery = ApiUtil.getStringQuery(TEST_COUNTRIES);

        Exception exception = new Exception();

        when(restCountriesApi.getCountriesDetails(fieldsFiltersQuery, countriesCodesQuery))
                .thenReturn(Single.<ArrayList<Country>>error(exception));

        countryBordersActivity.borders = TEST_COUNTRIES;

        MainPresenter mainPresenter = new MainPresenter(
                restCountriesApi,
                Schedulers.immediate(),
                Schedulers.immediate(),
                countryBordersActivity
        );

        mainPresenter.loadData();

        InOrder inOrder = Mockito.inOrder(countryBordersActivity);
        inOrder.verify(countryBordersActivity, times(1)).onFetchDataStarted();
        inOrder.verify(countryBordersActivity, times(1)).onFetchDataError(exception);
        verify(countryBordersActivity, never()).onFetchDataCompleted();
    }

//-------------------------------------------------------------------------------------------------

    @Test
    public void fetchValidDataShouldLoadInto_MainActivity_View() {

        String fieldsFiltersQuery = ApiUtil.getStringQuery(ApiUtil.MAIN_ACTIVITY_REQUIRED_FIELDS);

        when(restCountriesApi.getAllCountries(fieldsFiltersQuery))
                .thenReturn(Single.just(FAKE_COUNTRIES_RESPONSE));

        MainPresenter mainPresenter = new MainPresenter(
                restCountriesApi,
                Schedulers.immediate(),
                Schedulers.immediate(),
                mainActivity
        );

        mainPresenter.loadData();

        InOrder inOrder = Mockito.inOrder(mainActivity);
        inOrder.verify(mainActivity, times(1)).onFetchDataStarted();
        inOrder.verify(mainActivity, times(1)).onFetchDataSuccess(FAKE_COUNTRIES_RESPONSE);
        inOrder.verify(mainActivity, times(1)).onFetchDataCompleted();
    }

//-------------------------------------------------------------------------------------------------

    @Test
    public void fetchErrorShouldReturnErrorTo_MainActivity_View() {

        String fieldsFiltersQuery = ApiUtil.getStringQuery(ApiUtil.MAIN_ACTIVITY_REQUIRED_FIELDS);

        Exception exception = new Exception();

        when(restCountriesApi.getAllCountries(fieldsFiltersQuery))
                .thenReturn(Single.<ArrayList<Country>>error(exception));

        MainPresenter mainPresenter = new MainPresenter(
                restCountriesApi,
                Schedulers.immediate(),
                Schedulers.immediate(),
                this.mainActivity
        );

        mainPresenter.loadData();

        InOrder inOrder = Mockito.inOrder(mainActivity);
        inOrder.verify(mainActivity, times(1)).onFetchDataStarted();
        inOrder.verify(mainActivity, times(1)).onFetchDataError(exception);
        verify(mainActivity, never()).onFetchDataCompleted();
    }

}
