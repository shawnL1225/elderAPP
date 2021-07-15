<?php

if(isset($_POST['phone']) && isset($_POST['password'])){

    require_once "config.php";
    require_once "validate.php";
    $phone = validate($_POST['phone']);
    $password = validate($_POST['password']);
    
    $sql = "select * from user where phone='$phone'";
    $result = $conn->query($sql);
    if($result->num_rows == 0){
        echo  "NoUser";
        return;
    }

    $sql2 = "select * from user where phone='$phone' and password='". md5($password) . "'";
    $result = $conn->query($sql2);
    if($result->num_rows > 0){
        echo "success";
        $row = $result->fetch_assoc();
        echo "-".$row["id"];
        echo "-".$row["identity"];
        

        
    }
    else
        echo "WrongPass";
}


?>