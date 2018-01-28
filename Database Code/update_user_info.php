<?php

class update_user_info {

    private $conn;

    // constructor
    function __construct() {
        require_once 'android_login_connect.php';
        // connecting to database
        $db = new android_login_connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {

    }

    /**
     * Storing new user
     * returns user details
     */
    public function StoreUserInfo($userID, $firstName, $lastName, $emailAddress, $password, $phoneNumber) {
        $hash = $this->hashFunction($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO members(userID, firstName, lastName, emailAddress, phoneNumber, encrypted_password, salt) VALUES(?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("sssssss", $userID, $firstName, $lastName, $emailAddress, $phoneNumber, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT userID, firstName, lastName, emailAddress, phoneNumber, encrypted_password, salt FROM members WHERE emailAddress = ?");
            $stmt->bind_param("s", $emailAddress);
            $stmt->execute();
            $stmt-> bind_result($token2,$token3,$token4,$token5,$token6,$token7,$token8);
            while ( $stmt-> fetch() ) {
                $user["userID"] = $token2;
                $user["firstName"] = $token3;
                $user["lastName"] = $token4;
                $user["emailAddress"] = $token5;
                $user["phoneNumber"] = $token6;
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
    public function VerifyUserAuthentication($emailAddress, $password) {

        $stmt = $this->conn->prepare("SELECT userID, firstName, lastName, emailAddress, phoneNumber, encrypted_password, salt FROM members WHERE emailAddress = ?");

        $stmt->bind_param("s", $emailAddress);

        if ($stmt->execute()) {
            $stmt-> bind_result($token2,$token3,$token4,$token5,$token6,$token7,$token8);

            while ( $stmt-> fetch() ) {
                $user["userID"] = $token2;
                $user["firstName"] = $token3;
                $user["lastName"] = $token4;
                $user["emailAddress"] = $token5;
                $user["phoneNumber"] = $token6;
                $user["encrypted_password"] = $token7;
                $user["salt"] = $token8;
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
     */
    public function CheckExistingUser($emailAddress) {
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
     * @param password
     * returns salt and encrypted password
     */
    public function hashFunction($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkHashFunction($salt, $password) {
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
        return $hash;
    }

}

?>