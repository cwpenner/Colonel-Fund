<?php

class update_event {

    private $conn;

    //constructor
    function __construct() {
        require_once 'android_login_connect.php';
        // connecting to database
        $db = new android_login_connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {}

    public function checkExistingEvent($title) {
        $stmt = $this->conn->prepare("SELECT title from events WHERE upper(title) = ?");
        $stmt->bind_param("s", strtoupper($title));
        $stmt->execute();
        $stmt->store_result();

        if($stmt->num_rows > 0) {
            // title existed
            $stmt->close();
            return true;
        } else {
            // title not existed
            $stmt->close();
            return false;
        }
    }

    public function storeEvent($title, $associatedMember, $eventDate, $fundGoal, $currentFunds, $description, $type, $imageURL, $eventTime, $address) {
        $stmt = $this->conn->prepare("INSERT INTO events(title, associatedMember, eventDate, fundGoal, currentFunds, description, type, imageURL, eventTime, address) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("ssssssssss", $title, $associatedMember, $eventDate, $fundGoal, $currentFunds, $description, $type, $imageURL, $eventTime, $address);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if($result) {
            $stmt = $this->conn->prepare("SELECT title, associatedMember, eventDate, fundGoal, currentFunds, description, type, imageURL, eventTime, address FROM events WHERE upper(title) = ?");
            $stmt->bind_param("s", strtoupper($title));
            $stmt->execute();
            $stmt->bind_result($token1,$token2,$token3,$token4,$token5,$token6,$token7,$token8,$token9,$token10);
            while($stmt->fetch()) {
                $event["title"] = $token1;
                $event["associatedMember"] = $token2;
                $event["eventDate"] = $token3;
                $event["fundGoal"] = $token4;
                $event["currentFunds"] = $token5;
                $event["description"] = $token6;
                $event["type"] = $token7;
                $event["imageURL"] = $token8;
                $event["eventTime"] = $token9;
                $event["address"] = $token10;
            }
            $stmt->close();
            return $event;
        } else {
            return false;
        }
    }

    public function returnEmail($userID) {
        $stmt = $this->conn->prepare("SELECT emailAddress FROM members WHERE userID = ?");
        $stmt->bind_param("s", $userID);
        $stmt->execute();
        $stmt->bind_result($token1);
        $stmt->fetch();
        $stmt->close();
        return $token1;
    }

}