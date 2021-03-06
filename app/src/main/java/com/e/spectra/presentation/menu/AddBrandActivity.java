package com.e.spectra.presentation.menu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.e.spectra.R;

import com.e.spectra.databinding.ActivityAddBrandBinding;
import com.e.spectra.domain.model.BrandViewModel;
import com.e.spectra.presentation.AbstractCatalogueActivity;
import com.e.spectra.presentation.view.BrandListener;
import com.e.spectra.util.ViewUtil;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class AddBrandActivity extends AbstractCatalogueActivity<BrandViewModel> implements BrandListener {

    BrandViewModel viewModel;
    @Inject
    @Named("BrandViewModel")
    ViewModelProvider.Factory factory;

    @Override
    public BrandViewModel getViewModel() {
        viewModel=ViewModelProviders.of(this, factory).get(BrandViewModel.class);
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brand);
        bind();
    }

    private void bind() {
        ActivityAddBrandBinding brandBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_brand);
      //  viewModel = ViewModelProviders.of(this).get(BrandViewModel.class);
        getViewModel();
        brandBinding.setAddBrandViewModel(viewModel);
        viewModel.brandListener = this;
    }

    @Override
    public Context onStarted() {
        Context context = AddBrandActivity.this;
        return context;
    }

    @Override
    public void onInsert(String response) {
        ViewUtil.toast(this, response);

    }

    @Override
    public void onFailed(String message) {
        ViewUtil.toast(this, message);

    }
}
