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


    
    if($stmt->affected_rows> 0){
        echo "success";
        
    }else{
        echo "failure".$stmt->error;
    }



}
else if($type == "updateVolunteer"){
    $uid = $_POST['uid'];
    $name = $_POST['name'];
    $phone = $_POST['phone'];
    $sex = $_POST['sex'];
    $department = $_POST['department'];
    $headshot = $_POST['headshot'];
    $email = $_POST['email'];

    $sql = "UPDATE user SET name=?, phone=?, sex=?, department=?, email=?, headshot=? WHERE id=? ";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssssssi", $name, $phone, $sex, $department, $email, $headshot, $uid);
    $stmt->execute();




    if($stmt->affected_rows> 0){
        echo "success update volunteer";
        
    }else{
        echo "failure".$stmt->error;
    }
}
else if($type == "updatePassword"){
    $password = $_POST['password'];
    $uid = $_POST['uid'];

    $sql = "UPDATE user SET password=? WHERE id=? ";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("si", md5($password), $uid);
    $stmt->execute();

    if($stmt->affected_rows> 0){
        echo "success update password";
        
    }else{
        echo "failure".$stmt->error;
    }
}
else if($type == "deleteUser"){
    $uid = $_POST['uid'];

    $sql = "DELETE FROM user WHERE id=? ";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $uid);
    $stmt->execute();

    if($stmt->affected_rows> 0){
        echo "success delete user";
        
    }else{
        echo "failure".$stmt->error;
    }
}

?>