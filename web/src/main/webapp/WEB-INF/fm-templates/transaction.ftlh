<div id="table_header">
    <div class="date">Date</div>
    <div class="transaction_ticker">Ticker</div>
    <div class="transaction_type">Transaction type</div>
    <div class="amount_transaction">Amount</div>
    <div class="purchase_price">Price</div>
    <div class="profit">Profit</div>
    <div class="fee">Transaction fee</div>
</div>

<div id="table_content">
    <#list transactions>
        <#items as transaction>
            <div class="date">${transaction.date}</div>
            <div class="transaction_ticker">${transaction.getShare().getTicker()}</div>
            <div class="transaction_type">${transaction.getType()}</div>
            <div class="amount_transaction">${transaction.amount?string["0"]}</div>
            <div class="purchase_price">${transaction.price?string["###0.00##"]} PLN</div>
            <div class="profit">${transaction.profit?string["###0.00##"]} PLN</div>
            <div class="fee">${transaction.transactionFeeValue?string["###0.00##"]} PLN</div>
        </#items>
    </#list>
</div>
