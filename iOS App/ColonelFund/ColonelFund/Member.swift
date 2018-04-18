//
//  Member.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

class Member: NSObject, Codable {
    private var userID: String
    private var firstName: String
    private var lastName: String
    private var username: String
    private var emailAddress: String
    private var phoneNumber: String
    private var profilePicURL: String
    private var facebookID: String //TODO: consider this for linking account
    private var googleID: String   //TODO: consider this for linking account
    private var associatedEvents: [Event]
    
    init(userID: String) {
        self.userID = userID
        self.firstName = ""
        self.lastName = ""
        self.username = ""
        self.emailAddress = ""
        self.phoneNumber = ""
        self.profilePicURL = ""
        self.facebookID = ""
        self.googleID = ""
        self.associatedEvents = []
    }
    
    init(userID: String, firstName: String, lastName: String, userName: String, emailAddress: String, phoneNumber: String) {
        self.userID = userID
        self.firstName = firstName
        self.lastName = lastName
        self.username = userName
        self.emailAddress = emailAddress
        self.phoneNumber = phoneNumber
        self.profilePicURL = ""
        self.facebookID = ""
        self.googleID = ""
        self.associatedEvents = []
    }
    
    init(facebookID: String, firstName: String, lastName: String, profilePicURL: String) {
        self.userID = ""
        self.firstName = firstName
        self.lastName = lastName
        self.username = ""
        self.emailAddress = ""
        self.phoneNumber = ""
        self.profilePicURL = profilePicURL
        self.facebookID = facebookID
        self.googleID = ""
        self.associatedEvents = []
        super.init()
        
        makeUsername()
        self.userID = "01" //TODO: change once we have usernames figured out
    }
    
    init(googleID: String, emailAddress: String, firstName: String, lastName: String, profilePicURL: String) {
        self.userID = ""
        self.firstName = firstName
        self.lastName = lastName
        self.username = ""
        self.emailAddress = emailAddress
        self.phoneNumber = ""
        self.profilePicURL = profilePicURL
        self.facebookID = ""
        self.googleID = googleID
        self.associatedEvents = []
        super.init()
        
        makeUsername()
        self.userID = "01" //TODO: change once we have usernames figured out
    }
    
    required init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        userID = try values.decode(String.self, forKey: .userID)
        firstName = try values.decode(String.self, forKey: .firstName)
        lastName = try values.decode(String.self, forKey: .lastName)
        emailAddress = try values.decode(String.self, forKey: .emailAddress)
        phoneNumber = try values.decode(String.self, forKey: .phoneNumber)
        username = try values.decode(String.self, forKey: .username)
        profilePicURL = try values.decode(String.self, forKey: .profilePicURL)
        facebookID = try values.decode(String.self, forKey: .facebookID)
        googleID = try values.decode(String.self, forKey: .googleID)
        self.username = firstName.lowercased() + lastName.lowercased()
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
    
    func getUsername() -> String {
        return self.username
    }
    
    func setUsername(username: String) {
        self.username = username
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
    
    func getFacebookID() -> String {
        return facebookID
    }
    
    func setFacebookID(facebookID: String) {
        self.facebookID = facebookID
    }
    
    func getAssociatedEvents() -> [Event] {
        return self.associatedEvents
    }
    
    func setAssociatedEvents() {
        self.associatedEvents.removeAll()
        for (item) in EventCollection.sharedInstance.eventArray {
            if (item.getAssociatedMember() == self.userID) {
                associatedEvents.append(item)
            }
        }
    }
    
    func getFormattedFullName() -> String {
        return self.firstName + " " + self.lastName
    }
    
    func makeUsername() {
        self.username = self.firstName.lowercased() + self.lastName.lowercased()
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(userID, forKey: .userID)
        try container.encode(firstName, forKey: .firstName)
        try container.encode(lastName, forKey: .lastName)
        try container.encode(emailAddress, forKey: .emailAddress)
        try container.encode(phoneNumber, forKey: .phoneNumber)
        try container.encode(username, forKey: .username)
        try container.encode(profilePicURL, forKey: .profilePicURL)
        try container.encode(facebookID, forKey: .facebookID)
        try container.encode(googleID, forKey: .googleID)
    }
    
    enum CodingKeys: String, CodingKey {
        case userID
        case firstName
        case lastName
        case username
        case emailAddress
        case phoneNumber
        case profilePicURL
        case facebookID
        case googleID
        case associatedEvents
    }
}
