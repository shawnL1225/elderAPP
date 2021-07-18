<?php
require_once 'config.php';
$type = $_POST["type"];

if($type == "getData"){
    $selectUID = $_POST['uid'];
    $sql = "SELECT * FROM user WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $selectUID);
    $stmt->execute();
    $result = $stmt->get_result();

    $row = $result->fetch_assoc();
        
    
    

    
    echo json_encode($row,JSON_UNESCAPED_UNICODE);

}

?>