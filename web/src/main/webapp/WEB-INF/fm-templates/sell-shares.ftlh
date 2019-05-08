<div id="table_header">
    <div class="ticker">Ticker</div>
    <div class="amount">Amount</div>
    <div class="avg-buy-price">Avg buy price</div>
    <div class="value">Value</div>
    <div class="current-price">Current price</div>
    <div class="current-value">Current value</div>
    <div class="return">Return</div>
    <div class="sell"></div>
</div>
<div id="table_content">
    <#list shares>
        <#items as share>
            <div class="ticker">${share.ticker}</div>
            <div class="amount">${share.sharesTotalAmount?string["0"]}</div>
            <div class="avg-buy-price">${share.avgBuyPrice?string["###0.0000"]} PLN</div>
            <div class="value">${share.baseValue?string["###0.00"]} PLN</div>
            <div class="current-price">${share.currentPrice?string["###0.0000"]} PLN</div>
            <div class="current-value">${share.currentValue?string["###0.00"]} PLN</div>
            <div class="return">${share.currentReturn?string["###0.00"]} %</div>
            <div class="sell">
                <form class="sell_form" action="share-sell" method="post">
                    <input type="text" name="amount" placeholder="Amount" class="sell_input" required>&nbsp&nbsp&nbsp
                    <input type="text" name="price" placeholder="Price" class="sell_input" required>
                    <input id="sell_submit" name="ticker" type="submit" data-id="${share.ticker}" value="SELL">
                </form>
            </div>
        </#items>
    </#list>
</div>
<script>
    $(function () {

        $(document).ready(function () {
            $(".sell_form").submit(function (event) {
                event.preventDefault();

                var form = $('.sell_form');
                var ticker= $(this).attr('data-id');
                var data = new FormData(ticker + form);
                $.ajax({
                    processData: false,
                    cache: false,
                    contentType: false,
                    url: '/share-sell',
                    type: 'POST',
                    data: data.serialize(),
                    success: function (result) {
                        location.href = '/share-sell';
                    }
                });
            });
        });
    });
</script>