<?php
require_once 'config.php';
$type = $_POST["type"];
if($type == "getEventAll"){

    $sql = "SELECT * FROM event";
    $stmt = $conn->prepare($sql);
    $stmt->execute();
    $result = $stmt->get_result();


    $event = array();
    while($row = $result->fetch_assoc()){
        $sql2 = "SELECT * FROM event_attendee WHERE eid=?";
        $stmt2 = $conn->prepare($sql2);
        $stmt2->bind_param("i", $row["id"]);
        $stmt2->execute();
        $result2 = $stmt2->get_result();
        $row["attendee"] = array();
        while($row2 = $result2->fetch_assoc()){
            $row["attendee"][] = $row2["uid"];
        }

        array_push($event, $row);
    }
    echo json_encode($event,JSON_UNESCAPED_UNICODE);
}
else if($type == "getEventMine"){
    $selectUID = $_POST["uid"];
    $sql = "SELECT * FROM event INNER JOIN event_attendee ON event.id=event_attendee.eid WHERE event_attendee.uid = ?";
    
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $selectUID);
    $stmt->execute();
    $result = $stmt->get_result();


    $event = array();
    while($row = $result->fetch_assoc()){
        $sql2 = "SELECT * FROM event_attendee WHERE eid=?";
        $stmt2 = $conn->prepare($sql2);
        $stmt2->bind_param("i", $row["id"]);
        $stmt2->execute();
        $result2 = $stmt2->get_result();
        $row["attendee"] = array();
        while($row2 = $result2->fetch_assoc()){
            $row["attendee"][] = $row2["uid"];
        }

        array_push($event, $row);
    }
    echo json_encode($event,JSON_UNESCAPED_UNICODE);
}


?>