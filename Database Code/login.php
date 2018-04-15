<?php
require_once 'update_user_info.php';
$db = new update_user_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['emailAddress']) && isset($_POST['password'])) {

    // receiving the post params
    $emailAddress = $_POST['emailAddress'];
    $password = $_POST['password'];

    // get the user by email and password
    $user = $db->VerifyUserAuthentication($emailAddress, $password);

    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["user"]["userID"] = $user["userID"];
        $response["user"]["firstName"] = $user["firstName"];
        $response["user"]["lastName"] = $user["lastName"];
        $response["user"]["emailAddress"] = $user["emailAddress"];
        $response["user"]["phoneNumber"] = $user["phoneNumber"];
        $response["user"]["username"] = $user["username"];
        $response["user"]["profilePicURL"] = $user["profilePicURL"];
        $response["user"]["facebookID"] = $user["facebookID"];
        $response["user"]["googleID"] = $user["googleID"];
        $response["user"]["firebaseID"] = $user["firebaseID"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
?>

