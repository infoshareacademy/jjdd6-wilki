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
        <td>${avgBuyPrice} PLN</td>
        <td>${value} PLN</td>
        <td>${currentPrice} PLN</td>
        <td>${currentValue} PLN</td>
        <td>${currentReturn} %</td>
        <td>${stopLoss} PLN</td>
        <td>${takeProfit} PLN</td>
    </tr>
    <tr>
        <td colspan="9" id="center">${fullName}</td>
    </tr>
    <tr>
        <th colspan="3">Current</th>
        <th colspan="3">Target</th>
        <th colspan="3">Stop-Loss</th>
    </tr>
    <tr>
        <td id="to_left">Price</td>
        <td>${currentPrice} PLN</td>
        <td></td>
        <td id="to_left">Price</td>
        <td>${takeProfit} PLN</td>
        <td>?</td>
        <td id="to_left">Price</td>
        <td>${stopLoss} PLN</td>
        <td>?</td>
    </tr>
    <tr>
        <td id="to_left">Value</td>
        <td>${currentValue} PLN</td>
        <td></td>
        <td id="to_left">Value</td>
        <td>${takeProfitValue} PLN</td>
        <td></td>
        <td id="to_left">Value</td>
        <td>${stopLossValue} PLN</td>
        <td></td>
    </tr>
    <tr>
        <td id="to_left">Return</td>
        <td>${currentReturn} %</td>
        <td></td>
        <td id="to_left">Return</td>
        <td>?%</td>
        <td></td>
        <td id="to_left">Return</td>
        <td>?%</td>
        <td></td>
    </tr>
    <tr>
        <td id="to_left">Profit</td>
        <td>?</td>
        <td></td>
        <td id="to_left">Profit</td>
        <td>?</td>
        <td></td>
        <td id="to_left">Loss</td>
        <td>?</td>
        <td></td>
    </tr>
    <tr>
        <th colspan="9">Additional Information</th>
    </tr>
    <tr>
        <td id="to_left">Current P/E</td>
        <td>${currentPE}</td>
        <td></td>
        <td id="to_left">Target P/E</td>
        <td>${targetPE}</td>
        <td></td>
        <td id="to_left">Fees Paid</td>
        <td>${feeAmount}</td>
        <td></td>
    </tr>
    <tr>
        <td id="to_left">Risk-Reward</td>
        <td>${riskReward}</td>
        <td></td>
        <td id="to_left">Last Volume</td>
        <td>${volume}</td>
        <td></td>
        <td id="to_left">To-Date Profit</td>
        <td>?</td>
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
                <td>${transaction.profit?string["###0.00##"]} PLN</td>
                <td>${transaction.transactionFeeValue?string["###0.00##"]} PLN</td>
            </tr>
        </#items>
    </#list>

</table>


