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
    isset($_POST['type']) &&
    isset($_POST['image'])) {

    // receiving the post params
    $title = $_POST['title'];
    $associatedMember = $_POST['associatedMember'];
    $eventDate = $_POST['eventDate'];
    $fundGoal = $_POST['fundGoal'];
    $currentFunds = $_POST['currentFunds'];
    $description = $_POST['description'];
    $type = $_POST['type'];
    $image = $_POST['image'];
    $rand = rand(1000,9999);
    $imageName = "$associatedMember-$rand.jpeg";
    $imagePath = "events_images/$imageName";
    $imageURL = "https://wesll.com/colonelfund/$imagePath";

    // check if event is already existed with the same title
    if ($db->checkExistingEvent($title)) {
        // event already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Event already exists with " . $title;
        echo json_encode($response);
    } else {
        // put event image in file system
        file_put_contents("$imagePath", base64_decode($image));

        // create a new event
        $event = $db->storeEvent($title, $associatedMember, $eventDate, $fundGoal, $currentFunds, $description, $type, $imageURL);
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
            $response["event"]["imageURL"] = $event["imageURL"];
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
    $response["error_msg"] = "Required parameters (title, associatedMember, eventDate, fundGoal, currentFunds, description, type, image) is missing!";
    echo json_encode($response);
}
?>





