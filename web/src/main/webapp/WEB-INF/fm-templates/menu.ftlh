<html>
<head>
  <meta charset="utf-8">
  <title>Wallet App</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body>
<div id="container">
    <div id="left_menu">
        DASHBOARD</br>
        <a href="/wallet"><div class="menu_option" <#if content == 1> id="selected" </#if>>Show wallet</div></a></br>
        <a href="/share-buy"><div class="menu_option" <#if content == 2> id="selected" </#if>>Buy shares</div></a></br>
        <a href="/share-sell"><div class="menu_option" <#if content == 3> id="selected" </#if>>Sell shares</div></a></br>
        <div class="menu_option" <#if content == 4> id="selected" </#if>>Manage free cash</div></br></br></br></br>
        ANALYTICS</br>
        <div class="menu_option" <#if content == 5> id="selected" </#if>>Most popular?</div></br>
        <div class="menu_option" <#if content == 6> id="selected" </#if>>Most profitable?</div></br>
        <div class="menu_option" <#if content == 7> id="selected" </#if>>Profit chart</div></br>
        <div class="menu_option" <#if content == 8> id="selected" </#if>>Show available companies</div></br></br></br></br></br>
        USER</br>
        <div class="menu_option">(nazwa uzytkownika)</div></br>
        <div class="menu_option">Log out</div></br>
    </div>
    <div id="upper_menu">
        <div class="upper_menu_info">Free Cash </br> #{freeCash} PLN</div>
        <div class="upper_menu_info">ROE </br> ${roe} %</div>
        <div class="upper_menu_info">najwięjszy profit jakiejś akcji (aktualnej) </div>
        <div class="upper_menu_info">największa strata aktualnej akcji </div>
    </div>
    <div id="content">
        <#if content == 1>
            <#include "owned-shares-list.ftlh">
            <#elseif content == 2>
            <#include "buy-shares.ftlh">
            <#elseif content == 3>
            <#include "sell-shares.ftlh">
        </#if>
    </div>
</div>
</body>
</html>