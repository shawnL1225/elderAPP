<?php
    require_once 'config.php';
if( isset($_POST['select'])){

    $selectUID = $_POST['uid'];
    $sql = "SELECT id, title, description, uid FROM place WHERE uid = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $selectUID);
    $stmt->execute();
    $stmt->bind_result($id, $title, $description, $uid);


    $place = array();
    while($stmt->fetch()){
        $temp = array();
        $temp['id'] = $id;
        $temp['title'] = $title;
        $temp['description'] = $description;
        $temp['uid'] = $uid;
        array_push($place, $temp);
    }
    echo json_encode($place,JSON_UNESCAPED_UNICODE);
}

if(isset($_POST['insert'])){

    $title = $_POST['title'];
    $desc = $_POST['desc'];
    $uid = $_POST['uid'];

    $sql = "insert into place (title, description, uid) values (?,?,?)";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssi", $title, $desc, $uid);
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