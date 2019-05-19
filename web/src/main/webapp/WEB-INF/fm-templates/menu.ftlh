<html>
<head>
  <meta charset="utf-8">
  <title>Stock Wallet</title>
    <link rel="icon"
          type="image/png"
          href="/welcome/logo.png">
  <link rel="stylesheet" type="text/css" href="/css/style.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body>
<div id="container">
    <div id="left_menu">
        <img src="" width="20px" height="20px"> DASHBOARD</br>
        <a href="/wallet"><div class="menu_option" <#if content == "show_wallet"> id="selected" </#if>><img src="" width="20px" height="20px"> Show wallet</div></a></br>
        <a href="/share-buy"><div class="menu_option" <#if content == "buy_shares"> id="selected" </#if>><img src="" width="20px" height="20px"> Report BUY</div></a></br>
        <a href="/share-sell"><div class="menu_option" <#if content == "shares_to_sell"> id="selected" </#if><#if content == "sell_specified"> id="selected" </#if>><img src="" width="20px" height="20px"> Report SELL</div></a></br>
        <a href="/increase-reduce-free-cash"><div class="menu_option" <#if content == "manage"> id="selected" </#if>><img src="" width="20px" height="20px"> Manage free cash</div></a></br></br></br></br>
        <img src="" width="20px" height="20px"> ANALYTICS</br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Most popular?</div></br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Most profitable?</div></br>
        <div class="menu_option" <#if content == ""> id="selected" </#if>>Profit chart</div></br>
        <a href="/transactions"><div class="menu_option" <#if content == "transaction"> id="selected" </#if>><img src="" width="20px" height="20px"> Transactions list</div></a></br>
        </br></br></br></br>
        <img src="" width="20px" height="20px"> USER</br>
        <div id="menu_option_user"><img src="${profilePicURL}"/> <span>${userName}</span></div>
        <div class="menu_option"><img src="" width="20px" height="20px"> Log out</div></br>
        <#if isAdmin == 1><a href="/admin"><div class="menu_option" <#if content == "manage"> id="selected" </#if>>Admin panel</div></a></br></br></br></br></#if>

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
        <#elseif content == "transaction">
            <#include "transaction.ftlh">
        </#if>
    </div>
</div>
</body>
</html>