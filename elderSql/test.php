<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
    require_once 'config.php';
    

    $sql = "SELECT * FROM user";
    $stmt = $conn->prepare($sql);
    $stmt->execute();
    $result = $stmt->get_result();
    while($row = $result->fetch_assoc()){
        echo $row["name"];
    }



?>

