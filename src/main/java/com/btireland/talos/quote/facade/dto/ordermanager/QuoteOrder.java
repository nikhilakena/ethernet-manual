package com.btireland.talos.quote.facade.dto.ordermanager;

import java.util.List;


public class QuoteOrder {

    private Order orders;

    private Qbtdcs qbtdcs;

    private List<Quote> quoteList;

    public QuoteOrder() {
    }

    public QuoteOrder(Order orders, Qbtdcs qbtdcs, List<Quote> quoteList) {
        this.orders = orders;
        this.qbtdcs = qbtdcs;
        this.quoteList = quoteList;
    }

    public Order getOrders() {
        return orders;
    }

    public Qbtdcs getQbtdcs() {
        return qbtdcs;
    }

    public List<Quote> getQuoteList() {
        return quoteList;
    }

    @Override
    public String toString() {
        return "QuoteOrder{" +
                "orders=" + orders +
                ", qbtdcs=" + qbtdcs +
                ", quoteList=" + quoteList +
                '}';
    }
}
