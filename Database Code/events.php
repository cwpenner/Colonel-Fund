<?php

$response = array("error" => FALSE);

require_once 'update_event.php';
$something = new update_event();

$conn = __construct();

// constructor
function __construct() {
    require_once 'android_login_connect.php';
    // connecting to database
    $db = new android_login_connect();
    $conn = $db->connect();
    return $conn;
}

$query = "SELECT title, associatedMember, eventDate, fundGoal, currentFunds, description, type, imageURL, eventTime, address FROM events";
$response = mysqli_query($conn,$query) or die("Couldn't execute query.");

$rows = array();
while($r = mysqli_fetch_assoc($response)) {

    $r['associatedEmail'] = $something->returnEmail($r['associatedMember']);
//    $r['address'] = substr($r['address'],0);
//    $addressArray = array();
//    $r['address'] = substr($r['address'], 1, -1);
//    $r['address'] = $addressArray;

    $rows[] = $r;
}

echo json_encode($rows);

mysqli_close($conn);
?>

