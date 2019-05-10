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
        <a href="/wallet"><div class="menu_option" <#if content == "show_wallet"> id="selected" </#if>>Show wallet</div></a></br>
        <a href="/share-buy"><div class="menu_option" <#if content == "add_shares"> id="selected" </#if>>Add shares</div></a></br>
        <a href="/share-sell"><div class="menu_option" <#if content == "shares_to_sell" || content == "sell_selected"> id="selected" </#if>>Sell shares</div></a></br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Manage free cash</div></br></br></br></br>
        ANALYTICS</br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Most popular?</div></br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Most profitable?</div></br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Profit chart</div></br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Show available companies</div></br></br></br></br></br>
        USER</br>
        <div class="menu_option"><img src="${profilePicURL}"/></div>
        <div class="menu_option">${userName}</div></br>
        <div class="menu_option">Log out</div></br>
    </div>
    <div id="upper_menu">
        <div class="upper_menu_info">Free Cash </br> #{freeCash} PLN</div>
        <div class="upper_menu_info">ROE</br> ${roe}%</div>
        <div class="upper_menu_info">Imieniny:<br>Jurand, Judyta</div>
        <div class="upper_menu_info" style="color:red">Najbliższa niedziela niehandlowa!</div>
    </div>
    <div id="content">
        <#if content == "show_wallet">
            <#include "owned-shares-list.ftlh">
        <#elseif content == "add_shares">
            <#include "buy-shares.ftlh">
        <#elseif content == "shares_to_sell">
            <#include "sell-shares.ftlh">
        <#elseif content == "sell_selected">
            <#include "sell-selected.ftlh">
        </#if>
    </div>
</div>
</body>
</html>