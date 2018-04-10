<?php

class update_user_info
{

    private $conn;

    // constructor
    function __construct()
    {
        require_once 'android_login_connect.php';
        // connecting to database
        $db = new android_login_connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() { }

    /**
     * Storing new user
     * returns user details
     * @param $userID
     * @param $firstName
     * @param $lastName
     * @param $emailAddress
     * @param $password
     * @param $phoneNumber
     * @param $username
     * @param $profilePicURL
     * @param $facebookID
     * @param $googleID
     * @param $firebaseID
     * @return bool
     */
    public function StoreUserInfo($userID, $firstName, $lastName, $emailAddress, $password, $phoneNumber, $username, $profilePicURL, $facebookID, $googleID, $firebaseID)
    {
        $hash = $this->hashFunction($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO members(userID, firstName, lastName, emailAddress, phoneNumber, encrypted_password, salt, username, profilePicURL, facebookID, googleID, firebaseID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("ssssssssssss", $userID, $firstName, $lastName, $emailAddress, $phoneNumber, $encrypted_password, $salt, $username, $profilePicURL, $facebookID, $googleID, $firebaseID);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT userID, firstName, lastName, emailAddress, phoneNumber, username, profilePicURL, facebookID, googleID, firebaseID FROM members WHERE emailAddress = ?");
            $stmt->bind_param("s", $emailAddress);
            $stmt->execute();
            $stmt->bind_result($token2, $token3, $token4, $token5, $token6, $token7, $token8, $token9, $token10, $token11);
            while ($stmt->fetch()) {
                $user["userID"] = $token2;
                $user["firstName"] = $token3;
                $user["lastName"] = $token4;
                $user["emailAddress"] = $token5;
                $user["phoneNumber"] = $token6;
                $user["username"] = $token7;
                $user["profilePicURL"] = $token8;
                $user["facebookID"] = $token9;
                $user["googleID"] = $token10;
                $user["firebaseID"] = $token11;
            }
            $stmt->close();
            return $user;
        } else {
            return false;
        }
    }

    /**
     * Get user by email and password
     * @param $emailAddress
     * @param $password
     * @return null
     */
    public function VerifyUserAuthentication($emailAddress, $password)
    {

        $stmt = $this->conn->prepare("SELECT userID, firstName, lastName, emailAddress, phoneNumber, encrypted_password, salt, username, profilePicURL, facebookID, googleID, firebaseID FROM members WHERE emailAddress = ?");

        $stmt->bind_param("s", $emailAddress);

        if ($stmt->execute()) {
            $stmt->bind_result($token2, $token3, $token4, $token5, $token6, $token7, $token8, $token9, $token10, $token11, $token12, $token13);

            while ($stmt->fetch()) {
                $user["userID"] = $token2;
                $user["firstName"] = $token3;
                $user["lastName"] = $token4;
                $user["emailAddress"] = $token5;
                $user["phoneNumber"] = $token6;
                $user["encrypted_password"] = $token7;
                $user["salt"] = $token8;
                $user["username"] = $token9;
                $user["profilePicURL"] = $token10;
                $user["facebookID"] = $token11;
                $user["googleID"] = $token12;
                $user["firebaseID"] = $token13;
            }

            $stmt->close();

            // verifying user password
            $salt = $token8;
            $encrypted_password = $token7;
            $hash = $this->CheckHashFunction($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    /**
     * Check user is existed or not
     * @param $emailAddress
     * @return bool
     */
    public function CheckExistingUser($emailAddress)
    {
        $stmt = $this->conn->prepare("SELECT emailAddress from members WHERE emailAddress = ?");

        $stmt->bind_param("s", $emailAddress);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param $password
     * returns salt and encrypted password
     * @return array
     */
    public function hashFunction($password)
    {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * returns hash string
     * @param $salt
     * @param $password
     * @return string
     */
    public function checkHashFunction($salt, $password)
    {
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
        return $hash;
    }

}

?>