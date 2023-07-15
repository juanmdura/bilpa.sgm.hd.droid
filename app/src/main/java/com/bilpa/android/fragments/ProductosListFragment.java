package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.ProductosActivity;
import com.bilpa.android.R;
import com.bilpa.android.adapters.ProductosAdapter;
import com.bilpa.android.model.Producto;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.products.ProductosResult;
import com.mautibla.utils.ViewUtils;

public class ProductosListFragment extends Fragment implements AdapterView.OnItemClickListener
{

    private ListView vListProducts;
    private ProgressBar vProgress;
    private TextView vEmptyView;
    private Long mSurtidorId;

    public static ProductosListFragment newInstance(Long surtidorId) {
        ProductosListFragment fragment = new ProductosListFragment();
        fragment.mSurtidorId = surtidorId;
        return fragment;
    }

    public ProductosListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_productos_list, container, false);
        vListProducts = (ListView) v.findViewById(R.id.vListProducts);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);
        vListProducts.setEmptyView(vEmptyView);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        load();
    }

    private void load() {

        ViewUtils.visible(vProgress);
        ViewUtils.gone(vListProducts, vEmptyView);


        ProductosResult mProductosResult = ((ProductosActivity) getActivity()).mProductosResult;
        if (mProductosResult == null) {
            ApiService.getProductos(mSurtidorId, new AsyncCallback<ProductosResult>(getActivity()) {
                @Override
                protected void onSuccess(ProductosResult result) {
                    ((ProductosActivity) getActivity()).mProductosResult = result;
                    bind(result);
                }
            });
        } else {
            bind(mProductosResult);
        }
    }

    private void bind(ProductosResult productosResult) {
        ViewUtils.visible(vListProducts);
        ViewUtils.gone(vProgress);
        vListProducts.setAdapter(new ProductosAdapter(getActivity(), productosResult.productos));
        vListProducts.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_PRODUCTOS);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProductosAdapter adapter = (ProductosAdapter) parent.getAdapter();
        Producto producto = adapter.getItem(position);
        vListProducts.setItemChecked(position, true);
        ((ProductosActivity) getActivity()).goToProductoDetail(producto, position);
    }

    public void setActivated(int position) {
        vListProducts.setItemChecked(position, true);
    }
}
