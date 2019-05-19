<div class="ticker-input_area">
    <form action="/share-transactions" method="get" style="text-align: center">

        <input type="submit" class="ticker-submit" value="Show all shares" onclick="location.href='/transactions'">

    <input type="text" class="ticker-input" autocomplete="off" required name="ticker" placeholder="Search by ticker..." list="tickersList"/>
    <datalist id="tickersList">
        <#list shares as share>
            <option value="${share.ticker}">${share.ticker}</option>
        </#list>
    </datalist>
        <input type="submit" class="ticker-submit" value="Select">
    </form>
</div>

<div><p>" "</br></p></div>

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
