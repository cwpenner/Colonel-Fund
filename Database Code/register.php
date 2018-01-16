<?php

require_once 'update_user_info.php';
$db = new update_user_info();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['gender']) && isset($_POST['age'])) {

    // receiving the post params
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $gender = $_POST['gender'];
    $age = $_POST['age'];

    // check if user is already existed with the same email
    if ($db->CheckExistingUser($email)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $email;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->StoreUserInfo($name, $email, $password, $gender, $age);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["gender"] = $user["gender"];
            $response["user"]["age"] = $user["age"];
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
    $response["error_msg"] = "Required parameters (name, email, password, gender or age) is missing!";
    echo json_encode($response);
}
?>