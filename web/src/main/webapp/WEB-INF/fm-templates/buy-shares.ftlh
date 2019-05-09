<?php
<div>
<form class="buy_form" method="POST" action="/share-buy">
    <div class="input_area">
        TICKER</br>
        <input type="text" class="text-input" name="ticker" required placeholder="e.g. KGH"/></div>
    <div class="input_area">
        AMOUNT</br>
        <input type="text" class="text-input" required name="amount"/></div>
    <div class="input_area">
        PRICE</br>
        <input type="text" class="text-input" required name="price"/></div>
    </br>
    <input id="buy" type="submit" value="ADD"/>
</form>
<div id="info">
    Available money: ${freeCash} PLN</br>
    Transaction: </br>
    Money left: </br>
</div>
<div id="status"
     <#if status == "Transaction success">style="color: lightgreen"</#if>
        <#if status == "Amount should be a whole number">style="color: red"</#if>
        <#if status == "Amount should be an integer greater than 0">style="color: red"</#if>
        <#if status == "Price should have a numerical value - format 0.00">style="color: red"</#if>
        <#if status == "Price should be a number greater than 0 - format 0.00">style="color: red"</#if>
        <#if status == "You don't have enough money!">style="color: red"</#if>
        <#if status?ends_with("not valid") >style="color: red"</#if>
>${status}</div>
</div>
