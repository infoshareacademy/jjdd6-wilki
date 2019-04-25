<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Wallet App</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<div id="container">
    <div id="left_menu">
        DASHBOARD</br>
        <div class="menu_option">Show wallet</div></br>
        <div class="menu_option">Buy shares</div></br>
        <div class="menu_option">Sell shares</div></br>
        <div class="menu_option">Manage free cash</div></br></br></br></br>
        ANALYTICS</br>
        <div class="menu_option">Most popular?</div></br>
        <div class="menu_option">Most profitable?</div></br>
        <div class="menu_option">Profit chart</div></br>
        <div class="menu_option">Show available companies</div></br></br></br></br></br>
        USER</br>
        <div class="menu_option">(nazwa uzytkownika)</div></br>
        <div class="menu_option">Log out</div></br>
    </div>
    <div id="upper_menu">
        <div class="upper_menu_info">profit</div>
        <div class="upper_menu_info">ROE</div>
        <div class="upper_menu_info">najwięjszy profit jakiejś akcji (aktualnej) </div>
        <div class="upper_menu_info">największa strata aktualnej akcji </div>
    </div>
    <div id="content">
        <#include "owned-shares-list.ftlh">
    </div>
</div>
</body>
</html>