<?php
require_once "config.php";

if(isset($_POST['phone']) && isset($_POST['password'])){
    $phone = $_POST['phone'];
    $password = $_POST['password'];
    
    $sql = "select * from user where phone=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $phone);
    $stmt->execute();
    $result = $stmt->get_result();

    if($result->num_rows == 0){
        echo  "NoUser";
        return;
    }

    $sql2 = "SELECT * from user where phone=? AND password=?";
    $stmt2 = $conn->prepare($sql2);
    $stmt2->bind_param("ss", $phone,  md5($password));
    $stmt2->execute();
    $result2 = $stmt2->get_result();
    if($result2->num_rows > 0){
        echo "success";
        $row = $result2->fetch_assoc();
        echo "-".$row["id"];
        echo "-".$row["identity"];
    }
    else
        echo "WrongPass";
}


?>
