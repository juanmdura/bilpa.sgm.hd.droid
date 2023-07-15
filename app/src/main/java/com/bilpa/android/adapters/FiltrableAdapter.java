package com.bilpa.android.adapters;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public abstract class FiltrableAdapter<T extends Object> extends BaseAdapter<T> implements Filterable {

    private List<T> filteredList;
    private List<T> totalList;
    private Filter mFilter;

    private FilterListener filterListener;

    public FiltrableAdapter(Context context, List<T> items) {
        super(context, 0, items);
    }

    public FiltrableAdapter(Context context, int resource, List<T> items) {
        super(context, 0, items);

        init();
    }

    private void init() {

        // Lista de items filtrados
        filteredList = new ArrayList<T>();
        filteredList.addAll(items);

        // Lista con todos items
        totalList = new ArrayList<T>();
        totalList.addAll(items);

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
            clear();
            addAllItems(filteredList);
            FiltrableAdapter.this.notifyDataSetChanged();
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
