<?php
require_once 'config.php';
$type = $_POST["type"];

if($type == "getData"){
    $selectUID = $_POST['uid'];
    $sql = "SELECT * FROM user WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $selectUID);
    $stmt->execute();
    $result = $stmt->get_result();

    $row = $result->fetch_assoc();
    echo json_encode($row,JSON_UNESCAPED_UNICODE);

}
else if($type == "updateElder"){

    $name = $_POST['name'];
    $password = $_POST['password'];
    $phone = $_POST['phone'];
    $remarks_ill = $_POST['remarks_ill'];
    $remarks_eating = $_POST['remarks_eating'];
    $remarks_other = $_POST['remarks_other'];
    $contactPhone = $_POST['contactPhone'];
    $contactEmail = $_POST['contactEmail'];
    $sex = $_POST['sex'];
    $address = $_POST['address'];
    $uid = $_POST['uid'];
    $headshot = $_POST['headshot'];

    $sql = "UPDATE user SET name=?, phone=?, remarks_illness=?, remarks_eating=?, remarks_other=?,
                contactPhone=?, contactEmail=?, address=?, sex=?, headshot=? WHERE id=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssssssssssi", $name, $phone,  $remarks_ill, $remarks_eating, $remarks_other,
                    $contactPhone, $contactEmail, $address, $sex, $headshot, $uid);
    $stmt->execute();


    
    if($password != ""){
        $sql2 = "UPDATE user SET password=? WHERE id=?";
        $stmt2 = $conn->prepare($sql2);
        $stmt2->bind_param("si", md5($password), $uid);
        $stmt2->execute();

    }
    if($stmt->affected_rows + $stmt2->affected_rows> 0){
        echo "success";
        
    }else{
        echo "failure".$stmt->error;
    }



}

?>