<?php

if(isset($_POST['username']) && isset($_POST['phone'])){
    require_once "config.php";
    require_once "validate.php";
    $username = validate($_POST['username']);
    $password = validate($_POST['password']);
    $phone = validate($_POST['phone']);
    $identity = validate($_POST['identity']);
    $remarks_ill = validate($_POST['remarks_ill']);
    $remarks_eating = validate($_POST['remarks_eating']);
    $remarks_other = validate($_POST['remarks_other']);
    $contactPhone = validate($_POST['contactPhone']);
    $contactEmail = validate($_POST['contactEmail']);
    $department = validate($_POST['department']);

    if($identity == "0"){   //長者
        $sql = "insert into user 
            (username, phone, password, identity, remarks_illness, remarks_eating, remarks_other, contactPhone, contactEmail) 
            values (?,?,?,?,?,?,?,?,?)";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sssisssss", $username, $phone, md5($password), $identity, 
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
            (username, phone, password, identity, department) 
            values (?,?,?,?,?)";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sssis", $username, $phone, md5($password), $identity, $department);
        $stmt->execute();

        if($stmt->affected_rows > 0){
            echo "success add volunteer";
            
        }else{
            echo "failure".$stmt->error;
        }
    }
    
}

?>