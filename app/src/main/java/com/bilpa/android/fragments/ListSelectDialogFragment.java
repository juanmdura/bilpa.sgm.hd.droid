package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.adapters.PicosAdapter;
import com.bilpa.android.model.Pico;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.ListResult;
import com.bilpa.android.services.actions.PicosResult;
import com.mautibla.utils.ViewUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ListSelectDialogFragment<T> extends ToolbarDialogFragment implements AdapterView.OnItemClickListener{

    private ListView vList;
    private ProgressBar vListLoading;
    private TextView vEmptyView;
    private ArrayAdapter<T> mDataAdapter;

    public OnListItemClick onListItemClick;
    private RelativeLayout vListContainer;

    public interface OnListItemClick<T> {
        public void onListItemClick(T t);
    }

    public void setOnListItemListener(OnListItemClick onListItemClick) {
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) getView().findViewById(R.id.vToolbar);
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_arrow_back_white_24dp;
    }

    @Override
    protected int getToolbarMenu() {
        return 0;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_dialog, container, false);

        vListContainer = (RelativeLayout) v.findViewById(R.id.vListContainer);
        vListContainer.setMinimumHeight(getContainerMinHeight());

        vList = (ListView) v.findViewById(R.id.vList);
        vListLoading = (ProgressBar) v.findViewById(R.id.vListLoading);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);
        return v;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vEmptyView.setText(getEmptyMsg());
        vList.setEmptyView(vEmptyView);
        ViewUtils.gone(vList, vEmptyView);
        ViewUtils.visible(vListLoading);
        loadDataAsync(new AsyncCallback<ListResult<T>>(getActivity()) {
            @Override
            protected void onSuccess(ListResult<T> result) {
                saveDataIfNeeded(result);
                onBindData(result);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onListItemClick != null) {
            T item = mDataAdapter.getItem(position);
            onListItemClick.onListItemClick(item);
        }
        dismiss();
    }

    protected int getDialogHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected int getDialogWidth() {
        return getResources().getDimensionPixelSize(R.dimen.dialogs_medium_width);
    }

    protected abstract void loadDataAsync(AsyncCallback<ListResult<T>> asyncCallback);
    protected abstract String getEmptyMsg();
    protected abstract ArrayAdapter<T> createAdapter();
    protected abstract void saveDataIfNeeded(ListResult<T> result);
    protected abstract int getContainerMinHeight();
    protected abstract int getContainerMaxHeight();

    public void onBindData(ListResult<T> result) {
        ViewUtils.visible(vList, vEmptyView);
        ViewUtils.gone(vListLoading);
        mDataAdapter = createAdapter();
        mDataAdapter.addAll(result.getItems());
        vList.setAdapter(mDataAdapter);
        vList.setOnItemClickListener(this);
        invalidateSize();
    }

}
