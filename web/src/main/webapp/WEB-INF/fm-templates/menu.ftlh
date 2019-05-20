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
        <img src="/icons/dashboard.png" width="20px" height="20px"> &nbsp;DASHBOARD</br>
        <a href="/wallet"><div class="menu_option" <#if content == "show_wallet"> id="selected" </#if>><img src="/icons/show_wallet.png" width="20px" height="20px"> &nbsp;Show wallet</div></a></br>
        <a href="/share-buy"><div class="menu_option" <#if content == "buy_shares"> id="selected" </#if>><img src="/icons/report_buy.png" width="20px" height="20px"> &nbsp;Report BUY</div></a></br>
        <a href="/share-sell"><div class="menu_option" <#if content == "shares_to_sell"> id="selected" </#if><#if content == "sell_specified"> id="selected" </#if>><img src="/icons/report_sell.png" width="20px" height="20px"> &nbsp;Report SELL</div></a></br>
        <a href="/increase-reduce-free-cash"><div class="menu_option" <#if content == "manage"> id="selected" </#if>><img src="/icons/manage_free_cash.png" width="20px" height="20px"> &nbsp;Manage free cash</div></a></br></br></br></br>
        <img src="/icons/analytics.png" width="20px" height="20px"> &nbsp;ANALYTICS</br>
        <a href="/stats?view=wallet"><div class="menu_option" <#if content == "wallet_stats"> id="selected" </#if>><img src="/icons/wallet_stats.png" width="20px" height="20px"> &nbsp;Wallet stats</div></a></br>
        <a href="/stats?view=app"><div class="menu_option" <#if content == "app_stats"> id="selected" </#if>><img src="/icons/app_stats.png" width="20px" height="20px"> &nbsp;App stats</div></a></br>
        <a href="/stats?view=wse"><div class="menu_option" <#if content == "wse_stats"> id="selected" </#if>><img src="/icons/wse_stats.png" width="20px" height="20px"> &nbsp;WSE stats</div></a></br>
        <a href="/transactions"><div class="menu_option" <#if content == "transaction"> id="selected" </#if>><img src="/icons/transactions_list.png" width="20px" height="20px"> &nbsp;Transactions list</div></a></br>
        </br></br></br>
        <img src="/icons/user.png" width="20px" height="20px"> &nbsp;USER</br>
        <div id="menu_option_user"><img src="${profilePicURL}"/> <span>${userName}</span></div>
        <a href="/logout"><div class="menu_option"><img src="/icons/log_out.png" width="20px" height="20px"> &nbsp;Log out</div></a></br>
        <#if isAdmin == 1><a href="/admin"><div class="menu_option" <#if content == "manage"> id="selected" </#if>>Admin panel</div></a></br></#if>
        </br>
        <div id="copy">&copy; 2019 Wilki</div>

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
        <#elseif content == "app_stats">
            <#include "appStats.ftlh">
        <#elseif content == "wallet_stats">
            <#include "walletStats.ftlh">
        <#elseif content == "wse_stats">
            <#include "wseStats.ftlh">
        <#elseif content == "set_sl">
            <#include "set-sl.ftlh">
        <#elseif content == "set_tp">
            <#include "set-tp.ftlh">
        </#if>
    </div>
</div>
</body>
</html>