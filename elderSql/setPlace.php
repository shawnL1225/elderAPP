<?php
    require_once 'config.php';
if( isset($_POST['select'])){

    $selectUID = $_POST['uid'];
    $sql = "SELECT id, title, description, uid, iconID FROM place WHERE uid = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $selectUID);
    $stmt->execute();
    $stmt->bind_result($id, $title, $description, $uid, $iconID);


    $place = array();
    while($stmt->fetch()){
        $temp = array();
        $temp['id'] = $id;
        $temp['title'] = $title;
        $temp['description'] = $description;
        $temp['iconID'] = $iconID;
        array_push($place, $temp);
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