/*
 * Copyright 2018 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fjoglar.popularmoviesapp.main;

import android.support.annotation.NonNull;

import com.fjoglar.popularmoviesapp.DefaultObserver;
import com.fjoglar.popularmoviesapp.data.source.DataSource;
import com.fjoglar.popularmoviesapp.main.domain.GetWelcomeMessage;
import com.fjoglar.popularmoviesapp.util.schedulers.BaseSchedulerProvider;

public class MainPresenter implements MainContract.Presenter {

    @NonNull
    private final DataSource mRepository;

    @NonNull
    private final MainContract.View mMainView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private final GetWelcomeMessage mGetWelcomeMessage;

    public MainPresenter(@NonNull DataSource repository,
                         @NonNull MainContract.View mainView,
                         @NonNull BaseSchedulerProvider schedulerProvider) {
        mRepository = repository;
        mMainView = mainView;
        mSchedulerProvider = schedulerProvider;

        mMainView.setPresenter(this);

        mGetWelcomeMessage = new GetWelcomeMessage(mRepository,
                                                   mSchedulerProvider.computation(),
                                                   mSchedulerProvider.ui());
    }

    @Override
    public void subscribe() {
        mMainView.showLoading();
        getWelcomeMessage();
    }

    @Override
    public void unsubscribe() {
        mGetWelcomeMessage.dispose();
    }

    @Override
    public void getWelcomeMessage() {
        mGetWelcomeMessage.execute(new WelcomeMessageObserver());
    }

    private final class WelcomeMessageObserver extends DefaultObserver<String> {

        @Override
        public void onNext(String welcomeMessage) {
            mMainView.showWelcomeMessage(welcomeMessage);
        }

        @Override
        public void onComplete() {
            mMainView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {

        }
    }
}