<div id="set_sl_and_tp_header">
    Set stop loss and take profit price
</div>
<div id="stop-loss">
    Set stop-loss price</br>
    <form action="/sl-and-tp" method="POST">
        <div class="input_area">
            TICKER</br>
            <input type="text" class="text-input" autocomplete="off" name="ticker" required placeholder="e.g. KGH"/></div>
        <input type="number" min="0.01" step="0.01" autocomplete="off" placeholder="Enter price in PLN" required name="price"/></br>
        <input type="hidden" name="action" value="sl"/>
        <input type="submit" name="submit" id="submit" value="CONFIRM"/>
    </form>
</div>
<div id="take-profit">
    Set take-profit price
    <form action="/sl-and-tp" method="POST">
        <div class="input_area">
            TICKER</br>
            <input type="text" class="text-input" autocomplete="off" name="ticker" required placeholder="e.g. KGH"/></div>
        <input type="number" min="0.01" step="0.01" autocomplete="off" placeholder="Enter price in PLN" required name="price"/></br>
        <input type="hidden" name="action" value="tp"/>
        <input type="submit" name="submit" id="submit" value="CONFIRM"/>
    </form>
</div>
<div id="price_status"
     <#if status?ends_with("PLN")>style="color: lightgreen"<#else> style="color: red"</#if>
>
    ${status}
</div>