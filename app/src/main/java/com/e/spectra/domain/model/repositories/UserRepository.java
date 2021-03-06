package com.e.spectra.domain.model.repositories;

import androidx.lifecycle.LiveData;

public interface UserRepository {

    LiveData<String> userLogin(String emailId, String password);

    LiveData<String> userReset(String emailId);

    LiveData<String> userRegister(String emailId, String password);
}
