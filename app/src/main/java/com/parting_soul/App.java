package com.parting_soul;

import com.parting_soul.base.BaseApplication;
import com.parting_soul.learn.layoutinflater.skinning.SkinManager;
import com.parting_soul.support.utils.IUserManager;

/**
 * @author parting_soul
 * @date 2019-10-12
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }

    @Override
    public IUserManager getUserManager() {
        return null;
    }

}
