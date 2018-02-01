//
//  Event.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

class Event: Codable {
    private var eventID: String
    private var title: String
    private var eventDate: String
    private var description: String
    private var fundGoal: Double
    private var currentFunds: Double
    private var eventPicURL: String
    private var associatedMember: Member
    
    init(eventID: String) {
        self.eventID = eventID
        self.title = ""
        self.eventDate = ""
        self.description = ""
        self.fundGoal = 0
        self.currentFunds = 0
        self.eventPicURL = ""
        self.associatedMember = Member(userID: "temp")
    }
    
    init(eventID: String, title: String, eventDate: String, description: String, fundGoal: Double, currentFunds: Double, associatedMember: Member) {
        self.eventID = eventID
        self.title = title
        self.eventDate = eventDate
        self.description = description
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = ""
        self.associatedMember = associatedMember
    }
    
    init(eventID: String, title: String, eventDate: String, description: String, fundGoal: Double, currentFunds: Double, eventPicURL: String, associatedMember: Member) {
        self.eventID = eventID
        self.title = title
        self.eventDate = eventDate
        self.description = description
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = eventPicURL
        self.associatedMember = associatedMember
    }
    
    init(json: [String: Any]) throws {
        guard let eventID = json["EventID"] as? String else {
            throw SerializationError.missing("EventID")
        }
        guard let title = json["Title"] as? String else {
            throw SerializationError.missing("Title")
        }
        guard let eventDate = json["EventDate"] as? String else {
            throw SerializationError.missing("EventDate")
        }
        guard let description = json["Description"] as? String else {
            throw SerializationError.missing("Description")
        }
        guard let fundGoal = json["FundGoal"] as? Double else {
            throw SerializationError.missing("FundGoal")
        }
        guard let currentFunds = json["CurrentFunds"] as? Double else {
            throw SerializationError.missing("CurrentFunds")
        }
        guard let memberName = json["AssociatedMember"] as? String else {
            throw SerializationError.missing("AssociatedMember")
        }
        let mc = MemberCollection()
        let associatedMember = mc.getMember(userID: memberName)
        
        self.eventID = eventID
        self.title = title
        self.eventDate = eventDate
        self.description = description
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = ""
        self.associatedMember = associatedMember!
    }
    
    func getEventID() -> String {
        return self.eventID
    }
    
    func setEventID(eventID: String) {
        self.eventID = eventID
    }
    
    func getTitle() -> String {
        return self.title
    }
    
    func setTitle(title: String) {
        self.title = title
    }
    
    func getEventDate() -> String {
        return self.eventDate
    }
    
    func setEventDate(eventDate: String) {
        self.eventDate = eventDate
    }
    
    func getDescription() -> String {
        return self.description
    }
    
    func setDescription(description: String) {
        self.description = description
    }
    
    func getFundGoal() -> Double {
        return self.fundGoal
    }
    
    func setFundGoal(fundGoal: Double) {
        self.fundGoal = fundGoal
    }
    
    func getCurrentFunds() -> Double {
        return self.currentFunds
    }
    
    func setCurrentFunds(currentFunds: Double) {
        self.currentFunds = currentFunds
    }
    
    func getEventPicURL() -> String {
        return self.eventPicURL
    }
    
    func setEventPicURL(eventPicURL: String) {
        self.eventPicURL = eventPicURL
    }
    
    func getAssociatedMember() -> Member {
        return associatedMember
    }
    
    func setAssociatedMember(associatedMember: Member) {
        self.associatedMember = associatedMember
    }
    
    func toJSON() -> Data {
        //        let dict = ["EventID": self.eventID,
        //                    "Title": self.title,
        //                    "EventDate": self.eventDate,
        //                    "Description": self.description,
        //                    "FundGoal": self.fundGoal,
        //                    "CurrentFunds": self.currentFunds,
        //                    "AssociatedMember": self.associatedMember]
        let encoder = JSONEncoder()
        let jsonData = try? encoder.encode(self) //change to dict if this contains too much data
        return jsonData!
    }
    
    enum SerializationError: Error {
        case missing(String)
        case invalid(String, Any)
    }
}
