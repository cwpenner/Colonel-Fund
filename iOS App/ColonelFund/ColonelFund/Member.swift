//
//  Member.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

class Member: Codable {
    private var userID: String
    private var firstName: String
    private var lastName: String
    private var userName: String
    private var emailAddress: String
    private var phoneNumber: String
    private var profilePicURL: String
    private var associatedEvents: [Event]
    
    init(userID: String) {
        self.userID = userID
        self.firstName = ""
        self.lastName = ""
        self.userName = ""
        self.emailAddress = ""
        self.phoneNumber = ""
        self.profilePicURL = ""
        self.associatedEvents = []
    }
    
    init(userID: String, firstName: String, lastName: String, userName: String, emailAddress: String, phoneNumber: String) {
        self.userID = userID
        self.firstName = firstName
        self.lastName = lastName
        self.userName = userName
        self.emailAddress = emailAddress
        self.phoneNumber = phoneNumber
        self.profilePicURL = ""
        self.associatedEvents = []
    }
    
    init(userID: String, firstName: String, lastName: String, userName: String, emailAddress: String, phoneNumber: String, profilePicURL: String) {
        self.userID = userID
        self.firstName = firstName
        self.lastName = lastName
        self.userName = userName
        self.emailAddress = emailAddress
        self.phoneNumber = phoneNumber
        self.profilePicURL = profilePicURL
        self.associatedEvents = []
    }
    
    init(json: [String: Any]) throws {
        guard let userID = json["UserID"] as? String else {
            throw SerializationError.missing("UserID")
        }
        guard let firstName = json["FirstName"] as? String else {
            throw SerializationError.missing("FirstName")
        }
        guard let lastName = json["LastName"] as? String else {
            throw SerializationError.missing("LastName")
        }
        guard let emailAddress = json["EmailAddress"] as? String else {
            throw SerializationError.missing("EmailAddress")
        }
        guard let phoneNumber = json["PhoneNumber"] as? String else {
            throw SerializationError.missing("PhoneNumber")
        }
        self.userID = userID
        self.firstName = firstName
        self.lastName = lastName
        self.userName = ""
        self.emailAddress = emailAddress
        self.phoneNumber = phoneNumber
        self.profilePicURL = ""
        self.associatedEvents = []
    }
    
    func getUserID() -> String {
        return self.userID
    }
    
    func setUserID(userID: String) {
        self.userID = userID
    }
    
    func getFirstName() -> String {
        return self.firstName
    }
    
    func setFirstName(firstName: String) {
        self.firstName = firstName
    }
    
    func getLastName() -> String {
        return self.lastName
    }
    
    func setLastName(lastName: String) {
        self.lastName = lastName
    }
    
    func getUserName() -> String {
        return self.userName
    }
    
    func setUserName(userName: String) {
        self.userName = userName
    }
    
    func getEmailAddress() -> String {
        return self.emailAddress
    }
    
    func setEmailAddress(emailAddress: String) {
        self.emailAddress = emailAddress
    }
    
    func getPhoneNumber() -> String {
        return self.phoneNumber
    }
    
    func setPhoneNumber(phoneNumber: String) {
        self.phoneNumber = phoneNumber
    }
    
    func getProfilePicURL() -> String {
        return self.profilePicURL
    }
    
    func setProfilePicURL(profilePicURL: String) {
        self.profilePicURL = profilePicURL
    }
    
    func getAssociatedEvents() -> [Event] {
        return self.associatedEvents
    }
    
    func setAssociatedEvents() {
        let ec = EventCollection()
        let eventList = ec.eventMap
        
        for (key, value) in eventList {
            if (value.getAssociatedMember().getUserID() == self.userID) {
                associatedEvents.append(value)
            }
            print("Member: \(userID) has the following associated events")
            print(self.associatedEvents)
        }
    }
    
    func getFormattedFullName() -> String {
        //return self.firstName.capitalized + " " + self.lastName.capitalized
        return self.firstName + " " + self.lastName
    }
    
    func toJSON() -> Data {
//        let dict = ["UserID": self.userID,
//                    "FirstName": self.firstName,
//                    "LastName": self.lastName,
//                    "EmailAddress": self.emailAddress,
//                    "PhoneNumber": self.phoneNumber]
        let encoder = JSONEncoder()
        let jsonData = try? encoder.encode(self) //change to dict if this contains too much data
        return jsonData!
    }
    
    enum SerializationError: Error {
        case missing(String)
        case invalid(String, Any)
    }
    
}
