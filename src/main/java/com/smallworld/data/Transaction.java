package com.smallworld.data;

import lombok.Data;

@Data
public class Transaction {

    private Long mtn;
    private Double amount;
    private String senderFullName;
    private Integer senderAge;
    private String beneficiaryFullName;
    private Integer beneficiaryAge;
    private Long issueId;
    private Boolean issueSolved;
    private String issueMessage;

}
