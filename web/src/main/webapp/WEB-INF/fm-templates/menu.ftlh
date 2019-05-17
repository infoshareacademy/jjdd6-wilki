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
        <a href="/share-buy"><div class="menu_option" <#if content == "buy_shares"> id="selected" </#if>>Report BUY</div></a></br>
        <a href="/share-sell"><div class="menu_option" <#if content == "shares_to_sell"> id="selected" </#if><#if content == "sell_specified"> id="selected" </#if>>Report SELL</div></a></br>
        <a href="/increase-reduce-free-cash"><div class="menu_option" <#if content == "manage"> id="selected" </#if>>Manage free cash</div></a></br></br></br></br>
        ANALYTICS</br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Most popular?</div></br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Most profitable?</div></br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Profit chart</div></br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Show available companies</div></br></br></br></br></br>
        USER</br>
        <div class="menu_option" id="menu_option_user"><img src="${profilePicURL}"/></br>${userName}</div>
        <div class="menu_option">Log out</div></br>
    </div>
    <div id="upper_menu">
        <div class="upper_menu_info">Free cash </br> <b>${freeCash?string["# ##0.00 PLN"]}</b></div>
        <div class="upper_menu_info">Return on equity</br> <b>${roe}%</b></div>
        <div class="upper_menu_info" style="background-image: url('/chart?type=mini&ticker=${mpTicker}');background-size: 312px;background-repeat: no-repeat;background-position: center">Best performing</br>
            <b>${mpTicker}</b>    ${mpReturn}
            </div>
        <div class="upper_menu_info" style="background-image: url('/chart?type=mini&ticker=${wpTicker}');background-size: 312px;background-repeat: no-repeat;background-position: center; color:red">Worst performing</br>
            <b>${wpTicker}</b>    ${wpReturn}
     </div>
    </div>
    <div id="content">
        <#if content == "show_wallet">
            <#include "owned-shares-list.ftlh">
        <#elseif content == "buy_shares">
            <#include "buy-shares.ftlh">
        <#elseif content == "shares_to_sell">
            <#include "sell-shares.ftlh">
        <#elseif content == "sell_specified">
            <#include "sell-specified.ftlh">
        <#elseif content == "manage">
            <#include "manage-free-cash.ftlh">
        <#elseif content == "specified_share">
            <#include "specified-share.ftlh">
        </#if>
    </div>
</div>
</body>
</html>