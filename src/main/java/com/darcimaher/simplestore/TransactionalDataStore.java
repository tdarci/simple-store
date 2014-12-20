package com.darcimaher.simplestore;


import java.util.*;

/**
 *
 */
public class TransactionalDataStore {

    protected final SearchableStringMap data;
    protected final List<SearchableStringMap> openTransactionsData;

    public TransactionalDataStore() {

        data = new SearchableStringMap();
        openTransactionsData = new ArrayList<SearchableStringMap>();
    }

    public void beginTransaction(){
        openTransactionsData.add(new SearchableStringMap());
    }

    private void commitOneTransaction(){
        if (inTransaction()) {
            SearchableStringMap m = openTransactionsData.get(0);
            for (Map.Entry<String, String> e : m.entrySet()) {
                String val = e.getValue();
                if (val == null) {
                    data.remove(e.getKey());
                }  else {
                    data.put(e.getKey(), val);
                }
            }
            openTransactionsData.remove(0);
        }
    }

    public void commitAllTransactions(){
        while (inTransaction()) {
            commitOneTransaction();
        }
    }

    public boolean inTransaction() {
        return (openTransactionsData.size() > 0);
    }

    public void rollbackOneTransaction(){
        if (inTransaction()){
            openTransactionsData.remove(openTransactionsData.size() - 1);
        }
    }
    public void rollbackAllTransactions(){
        while (inTransaction()) {
            rollbackOneTransaction();
        }
    }

    public void set(String key, String value) {
        SearchableStringMap m = currentDataMap();
        m.put(key, value);
    }

    private SearchableStringMap currentDataMap() {
        return inTransaction() ? openTransactionsData.get(openTransactionsData.size() - 1) : data;
    }

    public void unset(String key) {
        SearchableStringMap m = currentDataMap();
        if (inTransaction()) {
            m.put(key, null);
        } else {
            m.remove(key);
        }
    }

    public String get(String key){
        if (inTransaction()) {
            for (int i = openTransactionsData.size() - 1; i >=0 ; i--) {
                SearchableStringMap m = openTransactionsData.get(i);
                if (m.containsKey(key)) {
                    return m.get(key);
                }
            }
        }
        return data.get(key);
    }

    public long countWithValue(String value) {

        List<SearchableStringMap> allDataSets = new ArrayList<>(openTransactionsData.size() + 1);
        allDataSets.add(data);
        allDataSets.addAll(openTransactionsData);
        Set<String> allFoundKeys = new HashSet<>();
        for (int i = allDataSets.size() - 1; i >= 0; i--) {
            Set<String> foundKeys = allDataSets.get(i).keysWithValue(value);
            for (String curKey : foundKeys) {
                if (allFoundKeys.contains(curKey)){
                    // ok, we already got this one
                    continue;
                }
                // the key was not there already,
                // so we have to make sure it wasn't set to another value in a more
                // recent transaction before we let ourselves count it.
                boolean keyFoundInFutureTransaction = false;
                for (int futureDataSetsIndex = i + 1; futureDataSetsIndex < allDataSets.size(); futureDataSetsIndex++) {
                    SearchableStringMap futureSet = allDataSets.get(futureDataSetsIndex);
                    if (futureSet.containsKey(curKey)) {
                        // uh oh, we don't like this...
                        // it's in a future map, but not counted.
                        // We will pass on this one.
                        keyFoundInFutureTransaction = true;
                    }
                }
                if (keyFoundInFutureTransaction) {
                    continue;
                }
                // we got all the way here, so let's count this as a good one.
                allFoundKeys.add(curKey);
            }

        }
        return allFoundKeys.size();
    }
}
