<?php
    require_once "config.php";
    $type = isset($_POST['type']);

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


?>