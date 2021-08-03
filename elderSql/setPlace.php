<?php
require_once 'config.php';
if( isset($_POST['select'])){

    $selectUID = $_POST['uid'];
    $sql = "SELECT id, title, description, uid, iconID FROM place WHERE uid = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $selectUID);
    $stmt->execute();
    $result = $stmt->get_result();


    $place = array();
    while($row = $result->fetch_assoc()){
        array_push($place, $row);
    }
    echo json_encode($place,JSON_UNESCAPED_UNICODE);
}

if(isset($_POST['insert'])){

    $title = $_POST['title'];
    $desc = $_POST['desc'];
    $uid = $_POST['uid'];
    $iconID = $_POST['iconID'];

    $sql = "insert into place (title, description, uid, iconID) values (?,?,?,?)";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssii", $title, $desc, $uid, $iconID);
    $stmt->execute();

    if($stmt->affected_rows > 0){
        echo "success add place";
        
    }else{
        echo "failure".$stmt->error;
    }
}

if(isset($_POST['delete'])){

    $id = $_POST['id'];

    $sql = "DELETE FROM place WHERE id = ?";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $id);
    $stmt->execute();

    if($stmt->affected_rows > 0){
        echo "success delete place";
        
    }else{
        echo "failure".$stmt->error;
    }
}

    


?>