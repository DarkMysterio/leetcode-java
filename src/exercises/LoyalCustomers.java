package exercises;

import java.util.*;

public class LoyalCustomers {
    public List<String> findLoyalCustomers(List<List<String>> day1Logs, List<List<String>> day2Logs) {
        Set<String> results = new HashSet<>();
        HashMap<String, Set<String>> customers = new HashMap<>();

        for(List<String> log : day1Logs){
            String customerId = log.get(2);
            String pageId = log.get(1);
            if (!customers.containsKey(customerId)) {
                customers.put(customerId, new HashSet<>());
            }
            customers.get(customerId).add(pageId);
        }
        for(List<String> log : day2Logs){
            String customerId = log.get(2);
            String pageId = log.get(1);
            if (customers.containsKey(customerId)) {
                customers.get(customerId).add(pageId);
                if (customers.get(customerId).size() >= 2) {
                    results.add(customerId);
                }
            }
        }

        return new ArrayList<>(results);
    }
}
