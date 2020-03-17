package nhan.natc.laundry.base;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel<N> extends NetworkViewModel {
    private N mNavigator;
    private MutableLiveData<String> showErrorObs = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void setNavigator(N navigator) {
        this.mNavigator = navigator;
    }

    void onViewCreated() {}

    public N getNavigator() {
        return mNavigator;
    }

    public MutableLiveData<String> getShowErrorObs() {
        return showErrorObs;
    }

    public void addToDispose(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void clearDispose() {
        compositeDisposable.dispose();
    }

}
