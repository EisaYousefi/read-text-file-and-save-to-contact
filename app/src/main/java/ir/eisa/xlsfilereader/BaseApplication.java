package ir.eisa.xlsfilereader;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    private static BaseApplication instance;
    private Context context;

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    public static BaseApplication getContext(Context context) {
        return ((BaseApplication) context.getApplicationContext());
    }



    public void setInstance(BaseApplication mInstance) {
        BaseApplication.instance = mInstance;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        setInstance(this);

    }
}
