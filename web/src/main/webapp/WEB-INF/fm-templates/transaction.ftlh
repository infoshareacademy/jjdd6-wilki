<div id="table_header">
    <div class="ticker">Ticker</div>
    <div class="full_company_name">Full company name</div>
    <div class="amount_transaction">Amount</div>
    <div class="date">Date</div>
    <div class="purchase_price">Purchase price</div>
    <div class="profit">Profit</div>
    <div class="fee">Transaction fee</div>
</div>

<div id="table_content">
    <#list transactions>
        <#items as transaction>
            <div class="ticker">${transaction.getShare().getTicker()}</div>
            <div class="full_company_name">${transaction.getShare().getFullCompanyName()}</div>
            <div class="amount_transaction">${transaction.amount?string["0"]}</div>
            <div class="date">${transaction.date}</div>
            <div class="purchase_price">${transaction.price?string["###0.00##"]} PLN</div>-->
            <div class="profit">${transaction.profit?string["###0.00##"]} PLN</div>-->
            <div class="fee">${transaction.transactionFeeValue?string["###0.00##"]} PLN</div>-->

        </#items>
    </#list>
</div>
