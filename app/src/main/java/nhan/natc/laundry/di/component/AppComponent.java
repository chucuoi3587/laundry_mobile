package nhan.natc.laundry.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import nhan.natc.laundry.LaundryApplication;
import nhan.natc.laundry.di.module.ActivityModule;
import nhan.natc.laundry.di.module.ApiModule;
import nhan.natc.laundry.di.module.AppModule;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class, ActivityModule.class, AndroidInjectionModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        @BindsInstance
        Builder appModule(AppModule appModule);
        @BindsInstance
        Builder apiModule(ApiModule apiModule);

        AppComponent build();
    }

    void inject(LaundryApplication application);
    void inject(AppModule appModule);
}
