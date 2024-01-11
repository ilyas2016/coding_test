package com.smallworld;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataService {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * load transaction from transactions.json
     *
     * @return list of transactions
     */
    public List<Transaction> getAllTransactions() {

        try {
            return mapper.readerForListOf(Transaction.class)
                    .readValue(Path.of("transactions.json").toFile());
        } catch (IOException e) {
            return List.of();
        }
    }

    /**
     * Return distinct transactions by 'mtn' field
     */
    public List<Transaction> getDistinctTransactionsByMtn() {

        List<Transaction> transactions = getAllTransactions();

        return new ArrayList<>(transactions.stream()
                // Filter the list by distinct 'mtn' field
                .collect(Collectors.toMap(
                        Transaction::getMtn,
                        Function.identity(),
                        (existing, replacement) -> existing))  // if the key is seen again, keep the existing transaction
                .values());
    }
}
