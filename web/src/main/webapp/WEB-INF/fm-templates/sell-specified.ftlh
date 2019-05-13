<div id="sell_ticker">
    ${ticker}
</div>
<div id="sell_full_com_name">
    ${companyName}
</div>
<div id="sell_amount">
    ${amount}
</div>
<div id="sell_buy_price">
    ${avgBuyPrice}
</div>
<div id="sell_value">
    ${baseValue}
</div>
<form action="share-sell" method="POST">
<div id="sell_form">

        <input type="text" required name="amount"/>
        <input type="text" required name="price"/>

</div>
<div id="sell_calc_current">
    Transaction</br>
    + 2000 PLN
</div>
<div id="confirm">
    <input type="submit" value="CONFIRM"/>
</div>
</form>
<div id="status"
     <#if status == "Transaction success">style="color: lightgreen"</#if>
        <#if status == "Amount should be a whole number">style="color: red"</#if>
        <#if status == "Amount should be an integer greater than 0">style="color: red"</#if>
        <#if status == "Price should have a numerical value - format 0.00">style="color: red"</#if>
        <#if status == "Price should be a number greater than 0 - format 0.00">style="color: red"</#if>
>${status}</div>