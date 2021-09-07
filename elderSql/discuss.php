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

if($type == "selectComment"){
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

?>