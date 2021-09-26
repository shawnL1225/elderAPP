<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once __DIR__.'/../config.php';

$case_id = $_POST["case_id"];
$uid = $_POST["uid"];

            
    $sql = "SELECT *
            FROM go_out_case
            WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $case_id);
    $stmt->execute();
    $result = $stmt->get_result();
    $row = $result->fetch_assoc();
    if(isset($row["receiver"])){
        echo "error";
    }else{
        $sql = "UPDATE go_out_case SET receiver=$uid WHERE id=$case_id";
        $stmt = $conn->prepare($sql);
        $success =  $stmt->execute();
        if ($success != false) 
            echo "ok";
        else echo "error";
    }

?>