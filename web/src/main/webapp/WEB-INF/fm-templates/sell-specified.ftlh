<div id="report_sell">
    REPORTING SELL
</div>
<div id="sell_ticker">
    ${ticker}
</div>
<div id="sell_full_com_name" <#if companyName?length gt 20>style="line-height: 55px; font-size: 40px;"</#if>>
    ${companyName}
</div>
<div id="sell_amount">
    <span>Available amount</span> </br>
    ${"${amount}"?replace(","," ")}
</div>
<div id="sell_buy_price">
    <span>Avg buy price</span> </br>
    ${avgBuyPrice} PLN
</div>
<div id="sell_value">
    <span>Value</span> </br>
    ${"${baseValue}"?replace(","," ")} PLN
</div>
<form action="share-sell" method="POST">
    <div class="sell_form">
        Amount </br>
        <input type="number" min="1" autocomplete="off" max="${"${amount}"?replace(",","")}" step="1" id="amount" required name="amount"/>
    </div>
    <div class="sell_form">
        Price in PLN</br>
        <input type="number" min="0.0001" step="0.0001" autocomplete="off" id="price" required name="price"/>
    </div>
    <div id="sell_calc_current">
        <span>Transaction</span></br>
        + 0 PLN
    </div>
    <div id="confirm">
        <input type="hidden" name="ticker" value="${ticker}"/>
        <input type="submit" value="CONFIRM"/> <input type="button" onClick="location.href='/share-sell'" value="BACK"/>
    </div>
</form>
<div id="sell_status"
     <#if status == "Transaction success">style="color: lightgreen"</#if>
        <#if status == "Amount should be an integer greater than 0">style="color: red"</#if>
        <#if status == "Price should be a number greater than 0 - format 0.0000">style="color: red"</#if>
        <#if status?ends_with("not valid") >style="color: red"</#if>
        <#if status == "You try to sell more shares than you have">style="color: red"</#if>
>${status}</div>
<script>
    $("#amount, #price").on('change keyup', function() {
        var amount = document.getElementById("amount").value;
        var price = document.getElementById("price").value;
        var totalPrice = parseInt(amount) * parseFloat(price);
        if (!isNaN(totalPrice)) {
            document.getElementById("sell_calc_current").innerHTML = "<span>Transaction</span></br>+ " + totalPrice.toFixed(2) + " PLN";
        }
        else{
            document.getElementById("sell_calc_current").innerHTML = "<span>Transaction</span></br>+ 0 PLN";
        }
    });
</script>