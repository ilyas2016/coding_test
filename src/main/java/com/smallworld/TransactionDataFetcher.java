package com.smallworld;

import com.smallworld.data.Transaction;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionDataFetcher {

    private final DataService dataService = new DataService();

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {

        return dataService.getDistinctTransactionsByMtn()
                .stream()
                // Filter the list by distinct 'mtn' field
                .collect(Collectors.toMap(
                        Transaction::getMtn,
                        Function.identity(),
                        (existing, replacement) -> existing))  // if the key is seen again, keep the existing transaction
                .values()
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {

        return dataService.getDistinctTransactionsByMtn()
                .stream()
                .filter(transaction -> transaction.getSenderFullName().equalsIgnoreCase(senderFullName))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {

        return dataService.getAllTransactions()
                .stream()
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(0);
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {

        HashSet<String> uniqueClients =
                dataService.getAllTransactions()
                        .stream()
                        .flatMap(transaction ->
                                Stream.of(transaction.getSenderFullName(),
                                        transaction.getBeneficiaryFullName())
                        )
                        .collect(Collectors.toCollection(HashSet::new));

        return uniqueClients.size();

    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {

        return dataService.getAllTransactions().stream()
                .anyMatch(transaction ->
                        (transaction.getSenderFullName().equalsIgnoreCase(clientFullName) ||
                                transaction.getBeneficiaryFullName().equalsIgnoreCase(clientFullName)) &&
                                transaction.getIssueId() != null &&
                                Boolean.FALSE.equals(transaction.getIssueSolved())
                );

    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {

        return dataService.getDistinctTransactionsByMtn().stream()
                .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Long> getUnsolvedIssueIds() {

        return dataService.getAllTransactions().stream()
                .filter(transaction -> transaction.getIssueId() != null && Boolean.FALSE.equals(transaction.getIssueSolved()))
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {

        return dataService.getAllTransactions().stream()
                .filter(transaction -> transaction.getIssueId() != null && Boolean.TRUE.equals(transaction.getIssueSolved()))
                .map(Transaction::getIssueMessage)
                .collect(Collectors.toList());
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {

        return dataService.getDistinctTransactionsByMtn().stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {

        return dataService.getDistinctTransactionsByMtn().stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName,
                        Collectors.summingDouble(Transaction::getAmount)))
                .entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

}
