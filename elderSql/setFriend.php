<?php
    require_once "config.php";
    $type = $_POST['type'];

    if($type == 'select'){

        $selectUID = $_POST['uid'];
        $sql = "SELECT user.username, user.phone, user.id FROM friend INNER JOIN user ON friend.volunteerID=user.id WHERE elderID = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("i", $selectUID);
        $stmt->execute();
        $stmt->bind_result($name, $phone, $id);


        $friend = array();
        while($stmt->fetch()){
            $temp = array();
            $temp['id'] = $id;
            $temp['name'] = $name;
            $temp['phone'] = $phone;
            array_push($friend, $temp);
        }
        echo json_encode($friend,JSON_UNESCAPED_UNICODE);

    }
    else if($type == "delete"){
        $volunteerID = $_POST['volunteerID'];
        $elderID = $_POST['elderID'];
        $sql = "DELETE FROM friend WHERE volunteerID = ? AND elderID = ?";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ii", $volunteerID, $elderID);
        $stmt->execute();

        if($stmt->affected_rows > 0){
            echo "success delete friend";
            
        }else{
            echo "failure".$stmt->error;
        }
    }
    if($type == "insert"){
        
        $elderID = $_POST['elderID'];
        $phone = $_POST['phone'];

        //find user with this phone number
        $sql = "SELECT id FROM user WHERE phone = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $phone);
        $stmt->execute();
        $result = $stmt->get_result();
        if($result->num_rows == 0){
            echo "NoUser";
            exit;
        }
        $correspondID; 
        if ($row = $result->fetch_assoc()) {
            $correspondID = $row['id'];
        }

        //check if they are already friend
        $sql = "SELECT id FROM friend WHERE elderID = ? AND volunteerID = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ii", $elderID, $correspondID);
        $stmt->execute();
        $result = $stmt->get_result();
        if($result->num_rows > 0){
            echo "Exist";
            exit;
        }

        $sql = "INSERT INTO friend (elderID, volunteerID) value (?,?)";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ii", $elderID, $correspondID);
        $stmt->execute();

        if($stmt->affected_rows > 0){
            echo "success insert friend";
            
        }else{
            echo "failure".$stmt->error;
        }
    }


?>