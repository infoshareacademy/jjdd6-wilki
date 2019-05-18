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
        <td>${ticker}</td>
        <td>${amount}</td>
        <td>${avgBuyPrice} PLN</td>
        <td>${value} PLN</td>
        <td>${currentPrice} PLN</td>
        <td>${currentValue} PLN</td>
        <td>${currentReturn}%</td>
        <td>${stopLoss} PLN</td>
        <td>${takeProfit} PLN</td>
    </tr>
    <tr>
        <td colspan="9">${fullName}</td>
    </tr>
    <tr>
        <th colspan="3">Current</th>
        <th colspan="3">Target</th>
        <th colspan="3">Stop-Loss</th>
    </tr>
    <tr>
        <td>Price</td>
        <td>${currentPrice} PLN</td>
        <td></td>
        <td>Price</td>
        <td>${takeProfit} PLN</td>
        <td>?</td>
        <td>Price</td>
        <td>${stopLoss} PLN</td>
        <td>?</td>
    </tr>
    <tr>
        <td>Value</td>
        <td>${currentValue} PLN</td>
        <td></td>
        <td>Value</td>
        <td>${takeProfitValue} PLN</td>
        <td></td>
        <td>Value</td>
        <td>${stopLossValue} PLN</td>
        <td></td>
    </tr>
    <tr>
        <td>Return</td>
        <td>${currentReturn}%</td>
        <td></td>
        <td>Return</td>
        <td>?%</td>
        <td></td>
        <td>Return</td>
        <td>?%</td>
        <td></td>
    </tr>
    <tr>
        <td>Profit</td>
        <td>?</td>
        <td></td>
        <td>Profit</td>
        <td>?</td>
        <td></td>
        <td>Loss</td>
        <td>?</td>
        <td></td>
    </tr>
    <tr>
        <th colspan="9">Additional Information</th>
    </tr>
    <tr>
        <td>Current P/E</td>
        <td>${currentPE}</td>
        <td></td>
        <td>Target P/E</td>
        <td>${targetPE}</td>
        <td></td>
        <td>Fees Paid</td>
        <td>${feeAmount}</td>
        <td></td>
    </tr>
    <tr>
        <td>Risk-Reward</td>
        <td>${riskReward}</td>
        <td></td>
        <td>Last Volume</td>
        <td>${volume}</td>
        <td></td>
        <td>To-Date Profit</td>
        <td>?</td>
        <td></td>
    </tr>
</table>
<table id="specified_transaction_view">
    <tr>
        <th>Transaction history</th>
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
                <td></td>
            </tr>
        </#items>
    </#list>

</table>


