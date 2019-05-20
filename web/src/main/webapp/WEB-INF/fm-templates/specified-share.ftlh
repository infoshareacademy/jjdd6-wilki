<table id="specified_share_view">
    <tr>
        <th>Ticker</th>
        <th>Amount</th>
        <th>Buy Price</th>
        <th>Value</th>
        <th>Current Price</th>
        <th>Current Value</th>
        <th>Return</th>
        <th>Stop-loss Price</th>
        <th>Take-profit Price</th>
    </tr>
    <tr>
        <td id="center">${ticker}</td>
        <td>${amount}</td>
        <td>${avgBuyPrice?string["###0.00## PLN"]}</td>
        <td>${value?string["###0.00 PLN"]}</td>
        <td>${currentPrice?string["###0.00## PLN"]}</td>
        <td>${currentValue?string["###0.00 PLN"]}</td>
        <td>${currentReturn?string["###0.00"]}%</td>
        <td>${stopLoss?string["###0.00## PLN"]}</td>
        <td>${takeProfit?string["###0.00## PLN"]}</td>
    </tr>
    <tr>
        <td colspan="9" id="center"><img src="/chart?type=full&ticker=${ticker}&buyprice=${avgBuyPrice}"/></td>
    </tr>
    <tr>
        <th colspan="3">Current</th>
        <th colspan="3">Target</th>
        <th colspan="3">Stop-Loss</th>
    </tr>
    <tr>
        <td id="to_left">Price</td>
        <td>${currentPrice?string["###0.00## PLN"]}</td>
        <td></td>
        <td id="to_left">Price</td>
        <td>${takeProfit?string["###0.00## PLN"]}</td>
        <td><#if takeProfit == 0>-<#else>${(takeProfit / currentPrice-1)?string["###0.00 %"]}</#if></td>
        <td id="to_left">Price</td>
        <td>${stopLoss} PLN</td>
        <td><#if stopLoss == 0>-<#else>${(stopLoss / currentPrice-1)?string["###0.00 %"]}</#if></td>
    </tr>
    <tr>
        <td id="to_left">Value</td>
        <td>${currentValue?string["###0.00 PLN"]}</td>
        <td></td>
        <td id="to_left">Value</td>
        <td>${takeProfitValue?string["###0.00 PLN"]}</td>
        <td></td>
        <td id="to_left">Value</td>
        <td>${stopLossValue?string["###0.00 PLN"]}</td>
        <td></td>
    </tr>
    <tr>
        <td id="to_left">Return</td>
        <td>${currentReturn?string["###0.00"]}%</td>
        <td></td>
        <td id="to_left">Return</td>
        <td><#if takeProfit == 0>-<#else>${targetReturn?string["###0.00"]}%</#if></td>
        <td></td>
        <td id="to_left">Return</td>
        <td><#if stopLoss == 0>-<#else>${stopLossReturn?string["###0.00"]}%</#if></td>
        <td></td>
    </tr>
    <tr>
        <td id="to_left">Profit</td>
        <td>${currentProfit?string["###0.00 PLN"]}</td>
        <td></td>
        <td id="to_left">Profit</td>
        <td><#if takeProfit == 0>-<#else>${targetProfit?string["###0.00 PLN"]}</#if></td>
        <td></td>
        <td id="to_left">Loss</td>
        <td><#if stopLoss == 0>-<#else>${(stopLossProfit)?string["###0.00 PLN"]}</#if></td>
        <td></td>
    </tr>
    <tr>
        <th colspan="9">Additional Information</th>
    </tr>
    <tr>
        <td id="to_left">Current P/E</td>
        <td>${currentPE?string["###0.0"]}</td>
        <td></td>
        <td id="to_left">Target P/E</td>
        <td>${targetPE?string["###0.0"]}</td>
        <td></td>
        <td id="to_left">Fees Paid</td>
        <td>${feeAmount?string["###0.00 PLN"]}</td>
        <td></td>
    </tr>
    <tr>
        <td id="to_left">Risk-Reward</td>
        <td>${riskReward?string["0.0"]}</td>
        <td></td>
        <td id="to_left">Last Volume</td>
        <td>${volume}</td>
        <td></td>
        <td id="to_left">To-Date Profit</td>
        <td>${totalProfit?string["###0.00 PLN"]}</td>
        <td></td>
    </tr>
</table>
<table id="specified_transaction_view">
    <tr>
        <th colspan="6">Transaction history</th>
    </tr>
    <tr>
        <th>Date</th>
        <th>Type</th>
        <th>Amount</th>
        <th>Price</th>
        <th>Profit</th>
        <th>Fee</th>
    </tr>
    <#list transactions>
        <#items as transaction>
            <tr>
                <td id="center">${transaction.date}</td>
                <td id="center">${transaction.type}</td>
                <td>${transaction.amount?string["0"]}</td>
                <td>${transaction.price?string["###0.00##"]} PLN</td>
                <td>${transaction.profit?string["###0.00"]} PLN</td>
                <td>${transaction.transactionFeeValue?string["###0.00"]} PLN</td>
            </tr>
        </#items>
    </#list>

</table>