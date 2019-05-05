<?php
$connect = mysqli_connect("0.0.0.0:6603", "root", "pass123", "tickers");
$output = '';
if(isset($_POST["query"]))
{
 $search = mysqli_real_escape_string($connect, $_POST["query"]);
 $query = "
  SELECT * FROM TICKERS
  WHERE ticker LIKE '%".$search."%'
  OR name LIKE '%".$search."%'
 ";
}
else
{
 $query = "
  SELECT * FROM TICKERS ORDER BY ticker
 ";
}
$result = mysqli_query($connect, $query);
if(mysqli_num_rows($result) > 0)
{
 $output .= '
  <div id="result">
   <table id="result_table">
    <tr>
     <th>Ticker</th>
     <th>Name</th>
    </tr>
 ';
 while($row = mysqli_fetch_array($result))
 {
  $output .= '
   <tr>
    <td>'.$row["ticker"].'</td>
    <td>'.$row["name"].'</td>
   </tr>
  ';
 }
 echo $output;
}
else
{
 echo 'Data Not Found';
}
?>