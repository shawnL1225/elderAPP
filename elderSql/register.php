<?php

if(isset($_POST['name']) && isset($_POST['phone'])){
    require_once "config.php";
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
    $headshot = $_POST['headshot'];
    $email = $_POST['email'];

    //check is phone not use
    $sql = "SELECT * FROM user WHERE phone=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $phone);
    $stmt->execute();
    $result = $stmt->get_result();
    if($result->num_rows>0){
        echo "isExist";
        return;
    }



    if($identity == "0"){   //elder
        $sql = "insert into user 
            (name, phone, password, identity, sex, address, remarks_illness, remarks_eating, remarks_other, contactPhone, contactEmail, headshot) 
            values (?,?,?,?,?,?,?,?,?,?,?,?)";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sssissssssss", $name, $phone, md5($password), $identity, $sex, $address,
                        $remarks_ill, $remarks_eating, $remarks_other, $contactPhone, $contactEmail, $headshot);
        $stmt->execute();


        if($stmt->affected_rows > 0){
            echo "success add elder";
            
        }else{
            echo "failure".$stmt->error;
        }
    }

    else if($identity == "1"){ //volunteer
        $sql = "insert into user 
            (name, phone, password, identity, sex, department, email, headshot) 
            values (?,?,?,?,?,?,?,?)";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sssissss", $name, $phone, md5($password), $identity, $sex, $department, $email, $headshot);
        $stmt->execute();

        if($stmt->affected_rows > 0){
            echo "success add volunteer";
            
        }else{
            echo "failure".$stmt->error;
        }
    }
}

?>