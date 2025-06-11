package com.jn.iyuba.concept.simple.view.home;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

public interface MyDubbingDetailsContract {

    interface MyDubbingDetailsView extends LoadingView {


    }

    interface MyDubbingDetailsPresenter extends IBasePresenter<MyDubbingDetailsView> {


    }

    interface MyDubbingDetailsModel extends BaseModel {


    }
}
