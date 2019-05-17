<html>
<head>
    <meta charset="utf-8">
    <title>Wallet App</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body style="font-family: 'Franklin Gothic Medium', Arial, sans-serif; color: white; font-size: 25px">
<div>


    <form action="/admin" method="get">
        <p>Select user:</p>
        <select name="selectedUser" ">
            <#list users as user>
            <option value="${user.id}">${user.name} ${user.surname}</option>
            </#list>
        </select>
        <p><input type="submit" value="Select"></p>
    </form>
    <a href="/wallet">GO BACK TO WALLET</a>



</div>
<#--<div id="status"-->
     <#--<#if status == "Transaction success">style="color: lightgreen"</#if>-->
        <#--<#if status == "Amount should be a whole number">style="color: red"</#if>-->
        <#--<#if status == "Amount should be an integer greater than 0">style="color: red"</#if>-->
        <#--<#if status == "Price should have a numerical value - format 0.0000">style="color: red"</#if>-->
        <#--<#if status == "Price should be a number greater than 0 - format 0.0000">style="color: red"</#if>-->
        <#--<#if status == "You don't have enough money!">style="color: red"</#if>-->
        <#--<#if status?ends_with("not valid") >style="color: red"</#if>-->
<#-->${status}</div>-->
<#--</div>-->
</body>
</html>


