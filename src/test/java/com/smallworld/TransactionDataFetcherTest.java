package com.smallworld;

import com.smallworld.data.Transaction;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionDataFetcherTest {

    private final TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();

    @Test
    public void testGetTotalTransactionAmount() {
        //when
        double totalTransactionAmount = transactionDataFetcher.getTotalTransactionAmount();

        //then
        assertThat(totalTransactionAmount).isEqualTo(2889.17);
    }

    @Test
    public void testGetTotalTransactionAmountSentBy() {

        //when
        double totalTransactionAmountSentByTom =
                transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");

        double totalTransactionAmountSentByAunt =
                transactionDataFetcher.getTotalTransactionAmountSentBy("Aunt Polly");

        double totalTransactionAmountSentByGrace =
                transactionDataFetcher.getTotalTransactionAmountSentBy("Grace Burgess");

        double totalTransactionAmountSentByBilly =
                transactionDataFetcher.getTotalTransactionAmountSentBy("Billy Kimber");

        //then
        assertThat(totalTransactionAmountSentByTom).isEqualTo(678.06);
        assertThat(totalTransactionAmountSentByAunt).isEqualTo(101.02);
        assertThat(totalTransactionAmountSentByGrace).isEqualTo(666.0);
        assertThat(totalTransactionAmountSentByBilly).isEqualTo(459.09);
    }

    @Test
    public void testGetMaxTransactionAmount() {

        //when
        double maxTransactionAmount = transactionDataFetcher.getMaxTransactionAmount();

        //then
        assertThat(maxTransactionAmount).isEqualTo(985.0);
    }

    @Test
    public void testUniqueClients() {

        //when
        long uniqueClients = transactionDataFetcher.countUniqueClients();

        //then
        assertThat(uniqueClients).isEqualTo(14);
    }

    @Test
    public void testHasOpenComplianceIssues() {

        //when
        boolean hasOpenComplianceIssueTom = transactionDataFetcher.hasOpenComplianceIssues("Tom Shelby");
        boolean hasOpenComplianceIssueAunt = transactionDataFetcher.hasOpenComplianceIssues("Aunt Polly");
        boolean hasOpenComplianceIssueGrace = transactionDataFetcher.hasOpenComplianceIssues("Grace Burgess");
        boolean hasOpenComplianceIssueMichael = transactionDataFetcher.hasOpenComplianceIssues("Michael Gray");
        boolean hasOpenComplianceIssueMajor = transactionDataFetcher.hasOpenComplianceIssues("Major Campbell");

        //then
        assertThat(hasOpenComplianceIssueTom).isTrue();
        assertThat(hasOpenComplianceIssueAunt).isFalse();
        assertThat(hasOpenComplianceIssueGrace).isTrue();
        assertThat(hasOpenComplianceIssueMichael).isTrue();
        assertThat(hasOpenComplianceIssueMajor).isFalse();
    }

    @Test
    public void testGetTransactionsByBeneficiaryName() {
        //when
        Map<String, List<Transaction>> transactionsByBeneficiaryName = transactionDataFetcher.getTransactionsByBeneficiaryName();

        //then
        assertThat(transactionsByBeneficiaryName).hasSize(10);
        assertThat(transactionsByBeneficiaryName.get("Alfie Solomons").size()).isEqualTo(1);
        assertThat(transactionsByBeneficiaryName.get("Arthur Shelby").size()).isEqualTo(1);
        assertThat(transactionsByBeneficiaryName.get("Aberama Gold").size()).isEqualTo(1);
        assertThat(transactionsByBeneficiaryName.get("Ben Younger").size()).isEqualTo(1);
        assertThat(transactionsByBeneficiaryName.get("Oswald Mosley").size()).isEqualTo(1);
        assertThat(transactionsByBeneficiaryName.get("MacTavern").size()).isEqualTo(1);
        assertThat(transactionsByBeneficiaryName.get("Michael Gray").size()).isEqualTo(1);
        assertThat(transactionsByBeneficiaryName.get("Winston Churchill").size()).isEqualTo(1);
        assertThat(transactionsByBeneficiaryName.get("Major Campbell").size()).isEqualTo(1);
        assertThat(transactionsByBeneficiaryName.get("Luca Changretta").size()).isEqualTo(1);

    }

    @Test
    public void testGetUnsolvedIssueIds() {

        //when
        Set<Long> unsolvedIssueIds = transactionDataFetcher.getUnsolvedIssueIds();

        //then
        assertThat(unsolvedIssueIds).hasSize(5);
        assertThat(unsolvedIssueIds).contains(1L, 3L, 15L, 54L, 99L);
    }

    @Test
    public void testGetAllSolvedIssueMessages() {

        //when
        List<String> messages = transactionDataFetcher.getAllSolvedIssueMessages();

        //then
        assertThat(messages).hasSize(3);
        assertThat(messages).contains(
                "Never gonna give you up",
                "Never gonna let you down",
                "Never gonna run around and desert you"
        );

    }

    @Test
    public void testGetTop3TransactionsByAmount() {

        //when
        List<Transaction> top3TransactionsByAmount = transactionDataFetcher.getTop3TransactionsByAmount();

        //then
        assertThat(top3TransactionsByAmount).hasSize(3);
        assertThat(top3TransactionsByAmount.get(0).getAmount()).isEqualTo(985.0);
        assertThat(top3TransactionsByAmount.get(1).getAmount()).isEqualTo(666.0);
        assertThat(top3TransactionsByAmount.get(2).getAmount()).isEqualTo(430.2);

    }

    @Test
    public void testGetTopSender(){
        //when
        Optional<String> topSender = transactionDataFetcher.getTopSender();

        //then
        assertThat(topSender).isPresent();
        assertThat(topSender.get()).isEqualTo("Arthur Shelby");
    }

}