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
    <a href="/admin/fileupload.html">UPLOAD FILE</a></br></br>
    <a href="/wallet">GO BACK TO WALLET</a>


</div>
</body>
</html>


