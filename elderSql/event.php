<?php



require_once 'config.php';
$type = $_POST["type"];
if($type == "getEvent"){

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
else if($type == "attend"){
    $uid = $_POST["uid"];
    $eid = $_POST["eid"];
    $sql = "INSERT INTO event_attendee (uid, eid) VALUES (?,?)";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $uid, $eid);
    $stmt->execute();

    if($stmt->affected_rows > 0){
        echo "success attend";
        
    }else{
        echo "failure ".$stmt->error;
    }
}
else if($type == "disAttend"){
    $uid = $_POST["uid"];
    $eid = $_POST["eid"];
    $sql = "DELETE FROM event_attendee WHERE uid=? AND eid=?";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $uid, $eid);
    $stmt->execute();

    if($stmt->affected_rows > 0){
        echo "success delete";
        
    }else{
        echo "failure ".$stmt->error;
    }
}
else if($type == "getNameList"){
    $nameString = "";
    $eid = $_POST["eid"];
    $sql = "SELECT user.name FROM event_attendee INNER JOIN user ON user.id=event_attendee.uid WHERE eid=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $eid);
    $stmt->execute();
    $result = $stmt->get_result();
    for($i=0; $row = $result->fetch_assoc();$i++){
        $nameString .= $row["name"];
        if($i%3 == 2)
            $nameString .="\n";
        else
            $nameString .=" / ";
    }
    echo $nameString;


}
else if($type == "addEvent"){
    $title = $_POST["title"];
    $location = $_POST["location"];
    $content = $_POST["content"];
    $holder = $_POST["holder"];
    $date = $_POST["date"];
    $img = $_POST["img"];
    $sql = "INSERT INTO event (title, location, content, holder, date, status) VALUES (?,?,?,?,?,0)";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sssss", $title, $location, $content, $holder, $date);
    $stmt->execute();


    if($stmt->affected_rows > 0){

        $eid = $conn->insert_id;
        echo "success add event {$eid} ";

        $file_dir = "./event_img/".$eid.".jpg";
        base64_to_jpg($img, $file_dir);
        // header("Location: imageUpload.php?eid=".$eid."&img=".$img);

        
    }else{
        echo "failure ".$stmt->error;
    }

    
}
function base64_to_jpg( $base64, $output_file ) {
    $ifp = fopen( $output_file, "wb" ); 
    fwrite( $ifp, base64_decode( $base64) ); 
    fclose( $ifp ); 
}

?>