<html>
<head>
    <meta charset="utf-8">
    <title>Wallet App</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body style="font-family: 'Franklin Gothic Medium', Arial, sans-serif; color: white; font-size: 25px">
<div>
    <img src="${profilePicURL}"/><br>
    <form action="/admin" method="post">
        User id: ${user.id}
        <input type="hidden" name="userId" value="${user.id}"><br>
        First name:
        <input type="text" name="name" value="${user.name}"><br>
        Last name:
        <input type="text" name="surname" value="${user.surname}"><br>
        E-Mail:
        <input type="text" name="email" value="${(user.email)!""}"><br>
        User has admin rights
        <input type="checkbox" name="isAdmin" value="admin"
                <#if isAdmin == 1>
        checked
                </#if>><br><br>
        <input type="submit" value="Apply changes">
    </form>
    <a href="/wallet">GO BACK TO WALLET</a>
</div>
</body>
</html>
