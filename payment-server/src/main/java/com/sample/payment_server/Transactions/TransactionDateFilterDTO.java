package com.sample.payment_server.Transactions;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
Request to Filter All transaction by date from client
{
    fromDate; auto 00:00:00
    toDate; auto 23:59:59
}
 */

@NoArgsConstructor
public class TransactionDateFilterDTO {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    TransactionDateFilterDTO(LocalDateTime fromDate, LocalDateTime toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }
}
