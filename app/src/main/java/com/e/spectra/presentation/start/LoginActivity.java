package com.e.spectra.presentation.start;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.e.spectra.R;
import com.e.spectra.databinding.ActivityLoginBinding;
import com.e.spectra.domain.model.AuthViewModel;
import com.e.spectra.domain.model.services.impl.NotificationJobService;
import com.e.spectra.presentation.AbstractCatalogueActivity;
import com.e.spectra.presentation.navigation.NavigationActivity;
import com.e.spectra.presentation.view.AuthListener;
import com.e.spectra.util.ViewUtil;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


public class LoginActivity extends AbstractCatalogueActivity<AuthViewModel> implements ComponentCallbacks2, AuthListener, HasActivityInjector {
    @BindView(R.id.loginProgressBar)
    ProgressBar mProgressBar;
    private JobScheduler mScheduler;

    AuthViewModel viewModel;
    private static final int JOB_ID = 0;
    @BindView(R.id.forgot_password)
    TextView resetPassword;


    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    @Named("AuthViewModel")
    ViewModelProvider.Factory factory;

    @Override
    public AuthViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel.class);
        return viewModel;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bind();
        ButterKnife.bind(this);

     //   hideSoftKeyboard();

        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
    }

    private void bind() {
        ActivityLoginBinding loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        loginBinding.setViewModel(viewModel);
        viewModel.authListener = this;
        viewModel.setupFirebaseAuth();
    }


    @Override
    public void onStart() {
        super.onStart();
        viewModel.onStart();

        scheduleJob();

    }


    @Override
    public void onStop() {
        super.onStop();
        viewModel.onStop();

    }


    public void scheduleJob() {
        PersistableBundle jobParams = new PersistableBundle();
        jobParams.putString("title", "Hello");
        jobParams.putString("body", "Buffalo");
        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName).setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setExtras(jobParams);
        JobInfo myJobInfo = builder.build();
        mScheduler.schedule(myJobInfo);
        Toast.makeText(this, R.string.job_scheduled, Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void hideSoftKeyboard() {
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    }

    @Override
    public void onStarted() {
        ViewUtil.showProgressBar(mProgressBar);
    }

    @Override
    public void onSuccess(LiveData response) {

        response.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                ViewUtil.hideProgressBar(mProgressBar);
                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                startActivity(intent);
                ViewUtil.toast(LoginActivity.this, o.toString());
            }
        });

    }


    @Override
    public void onFailed(String message) {
        ViewUtil.hideProgressBar(mProgressBar);
        ViewUtil.toast(LoginActivity.this, message);
    }

    @Override
    public void onReset(LiveData response) {

        response.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {

                ViewUtil.toast(LoginActivity.this, o.toString());
            }
        });

    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
