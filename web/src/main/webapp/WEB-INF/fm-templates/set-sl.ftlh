<div id="sl_tp_form">
    <form method="POST" action="/sl">
        <div class="sl_tp_input_area">
            STOP-LOSS PRICE</br>
            <input type="number" min="0.01" step="0.01" autocomplete="off" placeholder="Enter price in PLN" required name="price"/></br></div>
        <input type="hidden" name="ticker" value="${ticker}"/>
        <input id="submit" type="submit" value="CONFIRM"/>
    </form>
</div>
<div id="sl_tp_status"
     <#if status?ends_with("PLN")>style="color: lightgreen"<#else> style="color: red"</#if>
>
    ${status}
</div>