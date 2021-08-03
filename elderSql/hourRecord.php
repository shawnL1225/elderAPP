<?php
require_once 'config.php';
$type = $_POST["type"];

if($type == "uploadHour"){
    $date = $_POST["date"];
    $time = $_POST["time"];
    $hour = $_POST["hour"];
    $content = $_POST["content"];
    $uid = $_POST["uid"];

    $sql = "INSERT INTO hour_record (uid, date, time, hour, content) VALUES (?,?,?,?,?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("issis", $uid, $date, $time, $hour, $content);
    $stmt->execute();

    if($stmt->affected_rows > 0){
        echo "success add working data";
        
    }else{
        echo "failure ".$stmt->error;
    }
}
?>