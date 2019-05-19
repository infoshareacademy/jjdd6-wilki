<div id="manage_cash_header">
    Set stop loss and take profit price
</div>
<div id="increase" class="input_area">
    TICKER</br>
    <input type="text" class="text-input" autocomplete="off" name="ticker" required placeholder="e.g. KGH"/></br></div>
<div id="increase">
    Set stop-loss price</br>
    <form class= "buy_form" action="/sl-and-tp" method="POST">
        <div class="input_area">
            STOP-LOSS PRICE</br>
        <input type="number" min="0.01" step="0.01" autocomplete="off" placeholder="Enter price in PLN" required name="price"/></br></div>
        <input type="hidden" name="action" value="sl"/>
        <input id = "buy" type="submit" value="CONFIRM"/>
    </form>
</div>
<div id="reduce">
    Set take-profit price
    <form class="buy_form" action="/sl-and-tp" method="POST">
        <#--<div class="input_area">-->
            <#--TICKER</br>-->
            <#--<input type="text" class="text-input" autocomplete="off" name="ticker" required placeholder="e.g. KGH"/></div>-->
        <div class="input_area">
            TAKE-PROFIT PRICE</br>
            <input type="number" min="0.01" step="0.01" autocomplete="off" placeholder="Enter price in PLN" required name="price"/></br></div>
        <input type="hidden" name="action" value="tp"/>
        <input id="buy" type="submit" value="CONFIRM"/>
    </form>
</div>
<div id="price_status"
     <#if status?ends_with("PLN")>style="color: lightgreen"<#else> style="color: red"</#if>
>
    ${status}
</div>