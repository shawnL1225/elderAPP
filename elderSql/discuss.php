<?php
require_once 'config.php';
$type = $_POST['type'];
if($type == "selectTitle"){

    $sql = "SELECT * FROM discuss_title";
    $stmt = $conn->prepare($sql);
    $stmt->execute();
    $result = $stmt->get_result();


    $title = array();
    while($row = $result->fetch_assoc()){
        array_push($title, $row);
    }
    echo json_encode($title,JSON_UNESCAPED_UNICODE);
}

elseif($type == "selectComment"){
    $tid = $_POST['tid'];
    $sql = "SELECT * FROM discuss_comment WHERE tid=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param('i', $tid);
    $stmt->execute();
    $result = $stmt->get_result();


    $comments = array();
    while($row = $result->fetch_assoc()){
        array_push($comments, $row);
    }
    echo json_encode($comments,JSON_UNESCAPED_UNICODE);
}

else if($type == "storeTitle"){
    $uid = $_POST["uid"];
    $title = $_POST["title"];
    $sql = "INSERT INTO discuss_title (title, uid) VALUES (?,?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param('si', $title, $uid);
    $stmt->execute();
    if($stmt->affected_rows > 0){
        echo "success";
    }else{
        echo "failure ".$stmt->error;
    }
}
else if($type == "storeComment"){
    $tid = $_POST["tid"];
    $uid = $_POST["uid"];
    $comment = $_POST["comment"];
    $sql = "INSERT INTO discuss_comment (tid, comment, uid) VALUES (?,?,?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param('isi', $tid, $comment, $uid);
    $stmt->execute();
    if($stmt->affected_rows > 0){
        echo "success";
    }else{
        echo "failure ".$stmt->error;
    }
}
else if($type == "deletePost"){
    $tid = $_POST["tid"];
    $sql = "DELETE FROM discuss_title WHERE id=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $tid);
    $stmt->execute();

    if($stmt->affected_rows > 0){
        echo "success delete";
        
    }else{
        echo "failure".$stmt->error;
    }

}
else if($type == "deleteComment"){
    $id = $_POST["id"];
    $sql = "DELETE FROM discuss_comment WHERE id=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $id);
    $stmt->execute();

    if($stmt->affected_rows > 0){
        echo "success delete";
        
    }else{
        echo "failure".$stmt->error;
    }
}
else if($type == "editComment"){
    $id = $_POST["id"];
    $comment = $_POST["comment"];
    $sql = "UPDATE discuss_comment SET comment=? WHERE id=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("si",$comment, $id);
    $stmt->execute();

    if($stmt->affected_rows > 0){
        echo "success edit";
        
    }else{
        echo "failure".$stmt->error;
    }
}
?>