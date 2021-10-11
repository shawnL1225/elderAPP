<?php
require_once __DIR__.'/../config.php';

$case_id = $_POST["id"];

    $sql = "SELECT *
            FROM go_out_case
            WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $case_id);
    $stmt->execute();
    $result = $stmt->get_result();
    $row = $result->fetch_assoc();
    if(isset($row)){
        $sql = "DELETE FROM go_out_case WHERE id=$case_id";
        $stmt = $conn->prepare($sql);
        $success =  $stmt->execute();
        if ($success != false) 
            echo "ok";
        else echo "error";
    }else{
        echo "error";
    }

?>