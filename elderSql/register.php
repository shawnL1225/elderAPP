<?php

if(isset($_POST['name']) && isset($_POST['phone'])){
    require_once "config.php";
    require_once "validate.php";
    $name = $_POST['name'];
    $password = $_POST['password'];
    $phone = $_POST['phone'];
    $identity = $_POST['identity'];
    $remarks_ill = $_POST['remarks_ill'];
    $remarks_eating = $_POST['remarks_eating'];
    $remarks_other = $_POST['remarks_other'];
    $contactPhone = $_POST['contactPhone'];
    $contactEmail = $_POST['contactEmail'];
    $department = $_POST['department'];
    $sex = $_POST['sex'];
    $address = $_POST['address'];

    if($identity == "0"){   //長者
        $sql = "insert into user 
            (name, phone, password, identity, sex, address, remarks_illness, remarks_eating, remarks_other, contactPhone, contactEmail) 
            values (?,?,?,?,?,?,?,?,?,?,?)";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sssisssssss", $name, $phone, md5($password), $identity, $sex, $address,
                        $remarks_ill, $remarks_eating, $remarks_other, $contactPhone, $contactEmail);
        $stmt->execute();


        if($stmt->affected_rows > 0){
            echo "success add elder";
            
        }else{
            echo "failure".$stmt->error;
        }
    }

    else if($identity == "1"){ //志工
        $sql = "insert into user 
            (name, phone, password, identity, sex, department) 
            values (?,?,?,?,?,?)";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sssiss", $name, $phone, md5($password), $identity, $sex, $department);
        $stmt->execute();

        if($stmt->affected_rows > 0){
            echo "success add volunteer";
            
        }else{
            echo "failure".$stmt->error;
        }
    }
}

?>