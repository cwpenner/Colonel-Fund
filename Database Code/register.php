<?php

require_once 'update_user_info.php';
$db = new update_user_info();

// json response array
$response = array("error" => FALSE);

//if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['gender']) && isset($_POST['age'])) {

if (isset($_POST['userID']) && isset($_POST['firstName']) && isset($_POST['lastName']) && isset($_POST['emailAddress']) && isset($_POST['password']) && isset($_POST['phoneNumber'])) {

    // receiving the post params
    $userID = $_POST['userID'];
    $firstName = $_POST['firstName'];
    $lastName = $_POST['lastName'];
    $emailAddress = $_POST['emailAddress'];
    $password = $_POST['password'];
    $phoneNumber = $_POST['phoneNumber'];

    // check if user is already existed with the same email
    if ($db->CheckExistingUser($emailAddress)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $emailAddress;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->StoreUserInfo($userID, $firstName, $lastName, $emailAddress, $password, $phoneNumber);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["user"]["userID"] = $user["userID"];
            $response["user"]["firstName"] = $user["firstName"];
            $response["user"]["lastName"] = $user["lastName"];
            $response["user"]["emailAddress"] = $user["emailAddress"];
            $response["user"]["phoneNumber"] = $user["phoneNumber"];
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
    $response["error_msg"] = "Required parameters (userID, firstName, lastName, emailAddress, password, phoneNumber) is missing!";
    echo json_encode($response);
}
?>