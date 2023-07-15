package com.bilpa.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public abstract class FiltrableRecycleAdapter<T extends Object, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Filterable {

    protected List<T> mData;

    private List<T> filteredList;
    private List<T> totalList;
    private Filter mFilter;

    private FilterListener filterListener;

    public FiltrableRecycleAdapter(Context context, List<T> items) {
        mData = items;
        init();
    }

    private void init() {


        // Lista de items filtrados
        filteredList = new ArrayList<T>();
        filteredList.addAll(mData);

        // Lista con todos items
        totalList = new ArrayList<T>();
        totalList.addAll(mData);

        // inicilizacion del filtro
        getFilter();
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null){
            mFilter  = new CustomFilter();
        }
        return mFilter;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            try {
                List<T> filteredResult = getFilteredResults(charSequence);
                FilterResults results = new FilterResults();
                results.values = filteredResult;
                results.count = filteredResult.size();
                return results;

            } catch (Throwable t) {
                t.printStackTrace();
                throw new RuntimeException(t);
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredList = (List<T>) filterResults.values;
            mData.clear();
            mData.addAll(filteredList);
            FiltrableRecycleAdapter.this.notifyDataSetChanged();
        }

        private List<T> getFilteredResults(CharSequence constraint) {
            if (constraint.length() == 0) {
                constraint = "";
            }

            String filter = String.valueOf(constraint).toLowerCase();


            ArrayList<T> listResult = new ArrayList<T>();
            for (T item : totalList) {
                if (filterItem(item, filter)) {
                    listResult.add(item);
                }
            }

            if (listResult.size() == 0) {
                filterListener.onNonResults();
            } else {
                filterListener.onFilter();
            }

            return listResult;
        }
    }

    protected abstract boolean filterItem(T item, String filter);

    public interface FilterListener {
        public void onFilter();
        public void onNonResults();
    }

    public FilterListener getFilterListener() {
        return filterListener;
    }

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }
}
