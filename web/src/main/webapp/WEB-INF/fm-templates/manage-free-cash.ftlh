<div id="manage_cash_header">
    Managing free cash
</div>
<div id="increase">
    Increase free cash</br>
    <form action="/increase-reduce-free-cash" method="POST">
        <input type="number" min="0.01" step="0.01" autocomplete="off" placeholder="Enter amount in PLN" required name="cash"/></br>
        <input type="hidden" name="action" value="increase"/>
        <input type="submit" value="CONFIRM"/>
    </form>
</div>
<div id="reduce">
    Reduce free cash
    <form action="/increase-reduce-free-cash" method="POST">
        <input type="number" min="0.01" step="0.01" autocomplete="off" placeholder="Enter amount in PLN" required name="cash"/></br>
        <input type="hidden" name="action" value="reduce"/>
        <input type="submit" value="CONFIRM"/>
    </form>
</div>
<div id="cash_status"
     <#if status == "Success">style="color: lightgreen"</#if>
>
${status}aaaaaaaaaaaaaaaaaaaaaaaaaaaa
</div>