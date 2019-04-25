<div id="table_header">
    <div class="ticker">Ticker</div>
    <div class="amount">Amount</div>
    <div class="avg-buy-price">Avg buy price</div>
    <div class="value">Value</div>
    <div class="current-price">Current price</div>
    <div class="current-value">Current value</div>
    <div class="return">Return</div>
    <div class="sl-price">Stop loss</div>
    <div class="tp-price">Take profit</div>
</div>
<div id="table_content">
    <#list shares>
        <#items as share>
            <div class="ticker">${share.ticker}</div>
            <div class="amount">${share.sharesTotalAmount}</div>
            <div class="avg-buy-price">${share.avgBuyPrice} PLN</div>
            <div class="value">${share.baseValue} PLN</div>
            <div class="current-price">${share.currentPrice} PLN</div>
            <div class="current-value">${share.currentValue} PLN</div>
            <div class="return">Mleko 3.2%</div>
            <div class="sl-price">${share.stopLossPrice} PLN</div>
            <div class="tp-price">${share.takeProfitPrice} PLN</div>


        </#items>
    </#list>
</div>