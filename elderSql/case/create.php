<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once __DIR__ . '/../config.php';

$date = $_POST["date"];
$place_id = $_POST["place"];
$submitter = $_POST["uid"];
$invite = $_POST["invite"];
$sex_limit = $_POST["sex_limit"];

if (isset($_POST["public"])) $public = $_POST["public"];

if (isset($public))
        $sql = "INSERT INTO go_out_case (date, place_id, submitter, invited, public, sex_limit)
            VALUES ('$date',$place_id, $submitter , '$invite','$public','$sex_limit')";
else
        $sql = "INSERT INTO go_out_case (date, place_id, submitter, invited, sex_limit)
            VALUES ('$date',$place_id, $submitter , '$invite', '$sex_limit')";

$stmt = $conn->prepare($sql);
$stmt->execute();

$result = $stmt->get_result();

if ($stmt->affected_rows == 1)
        echo "ok";
else echo "error";
