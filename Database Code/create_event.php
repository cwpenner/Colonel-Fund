<?php

require_once 'update_event.php';

$db = new update_event();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['title']) &&
    isset($_POST['associatedMember']) &&
    isset($_POST['eventDate']) &&
    isset($_POST['fundGoal']) &&
    isset($_POST['currentFunds']) &&
    isset($_POST['description']) &&
    isset($_POST['type'])) {

    // receiving the post params
    $title = $_POST['title'];
    $associatedMember = $_POST['associatedMember'];
    $eventDate = $_POST['eventDate'];
    $fundGoal = $_POST['fundGoal'];
    $currentFunds = $_POST['currentFunds'];
    $description = $_POST['description'];
    $type = $_POST['type'];

    // check if event is already existed with the same title
    if ($db->checkExistingEvent($title)) {
        // event already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Event already exists with " . $title;
        echo json_encode($response);
    } else {
        // create a new event
        $event = $db->storeEvent($title, $associatedMember, $eventDate, $fundGoal, $currentFunds, $description, $type);
        if ($event) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["event"]["title"] = $event["title"];
            $response["event"]["associatedMember"] = $event["associatedMember"];
            $response["event"]["eventDate"] = $event["eventDate"];
            $response["event"]["fundGoal"] = $event["fundGoal"];
            $response["event"]["currentFunds"] = $event["currentFunds"];
            $response["event"]["description"] = $event["description"];
            $response["event"]["type"] = $event["type"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (title, associatedMember, eventDate, fundGoal, currentFunds, description, type) is missing!";
    echo json_encode($response);
}
?>





