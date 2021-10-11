<?php
    require_once "config.php";
    $type = $_POST['type'];

    if($type == 'select-elder'){

        $selectUID = $_POST['uid'];
        $sql = "SELECT user.name, user.phone, user.id, user.headshot FROM friend INNER JOIN user ON friend.volunteerID=user.id WHERE elderID = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("i", $selectUID);
        $stmt->execute();
        $result = $stmt->get_result();


        $friend = array();
        while($row = $result->fetch_assoc()){
            array_push($friend, $row);
        }
        echo json_encode($friend,JSON_UNESCAPED_UNICODE);

    }
    else if($type == 'select-volunteer'){

        $selectUID = $_POST['uid'];
        $sql = "SELECT user.name, user.phone, user.id, user.headshot FROM friend INNER JOIN user ON friend.elderID=user.id WHERE volunteerID = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("i", $selectUID);
        $stmt->execute();
        $result = $stmt->get_result();


        $friend = array();
        while($row = $result->fetch_assoc()){
            array_push($friend, $row);
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
    else if($type == "insert"){
        
        $elderID = $_POST['elderID'];
        $phone = $_POST['phone'];

        //find user with this phone number
        $sql = "SELECT id FROM user WHERE phone = ? AND identity = 1";
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