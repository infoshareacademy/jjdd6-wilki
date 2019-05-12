<div id="sell_form" >
    <form method="POST" action="/share-sell">
        ${ticker}
        <input type="text" required/>
        <input type="submit"/>
    </form>

</div>
<div id="info">
    Available money: ${"${freeCash}"?replace(","," ")} PLN</br>
    Transaction: 0 PLN</br>
    <span>Money left: ${"${freeCash}"?replace(","," ")} PLN</span></br>
</div>
<div id="status"
     <#if status == "Transaction success">style="color: lightgreen"</#if>
        <#if status == "Amount should be a whole number">style="color: red"</#if>
        <#if status == "Amount should be an integer greater than 0">style="color: red"</#if>
        <#if status == "Price should have a numerical value - format 0.00">style="color: red"</#if>
        <#if status == "Price should be a number greater than 0 - format 0.00">style="color: red"</#if>
        <#if status?ends_with("not valid") >style="color: red"</#if>
>${status}</div>
</div>