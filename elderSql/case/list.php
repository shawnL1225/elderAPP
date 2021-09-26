<?php
error_reporting(E_ALL);

ini_set('display_errors', 1);
require_once __DIR__ . '/../config.php';

$new_time = date("Y-m-d H:i:s", strtotime('-1 hours'));

$sql = "SELECT go_out_case.id, go_out_case.date, go_out_case.receiver, go_out_case.invited, go_out_case.public, go_out_case.sex_limit, 
                    user.name as user_name, user.phone as user_phone, user.headshot as user_headshot,
                    place.id as place_id, place.title as place_title, place.description as place_desc , place.iconID as place_icon 
            FROM go_out_case
            INNER JOIN user
            ON go_out_case.submitter=user.id
            INNER JOIN place
            ON go_out_case.place_id=place.id
            WHERE go_out_case.date > '$new_time'";

if (isset($_POST["uid"])) {
        $submitter = $_POST["uid"];
        $sql .= "\nWHERE go_out_case.submitter = $submitter";
}

$stmt = $conn->prepare($sql);
$stmt->execute();
$result = $stmt->get_result();

$cases = array();

while ($row = $result->fetch_assoc()) {
        $case["id"] = $row['id'];
        $case["date"] = $row['date'];
        $case["submitter"]["name"] = $row["user_name"];
        $case["submitter"]["phone"] = $row["user_phone"];
        $case["submitter"]["headshot"] = $row["user_headshot"];
        $case["place"]["id"] = $row["place_id"];
        $case["place"]["title"] = $row["place_title"];
        $case["place"]["description"] = $row["place_desc"];
        $case["place"]["iconID"] = $row["place_icon"];
        $case["public"] = $row["public"];
        $case["sex_limit"] = $row["sex_limit"];

        if (isset($row["receiver"])) {
                $sql = "SELECT id,name, phone, headshot
                 FROM user
                 WHERE user.id = " . $row["receiver"];
                $stmt = $conn->prepare($sql);
                $stmt->execute();
                $user_result = $stmt->get_result();
                $row2 = $user_result->fetch_assoc();

                $case["receiver"] = $row2;
        } else {
                $case["receiver"] = null;
        }

        $user_list = array();
        if ($row["invited"] == "") {
                $count = 0;
        } else {
                $invited_list =  explode(",", $row["invited"]);
                $count = count($invited_list);
        }
        while ($count > 0) {
                $uid = $invited_list[$count - 1];
                $sql = "SELECT id,name, phone, headshot
                    FROM user
                    WHERE user.id = $uid";

                $stmt = $conn->prepare($sql);
                $stmt->execute();
                $user_result = $stmt->get_result();
                $row = $user_result->fetch_assoc();
                array_push($user_list, $row);
                $count--;
        }

        $case["invited"] = $user_list;
        array_push($cases, $case);
}

echo json_encode($cases, JSON_UNESCAPED_UNICODE);
