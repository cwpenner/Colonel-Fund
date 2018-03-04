//
//  Event.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

class Event: NSObject, Codable {
    
    private var eventID: String
    private var title: String
    private var eventDate: String
    private var eventDescription: String
    private var fundGoal: Double
    private var currentFunds: Double
    private var eventPicURL: String
    private var associatedMember: String
    private var eventType: String
    
    init(eventID: String) {
        self.eventID = eventID
        self.title = ""
        self.eventDate = ""
        self.eventDescription = ""
        self.fundGoal = 0
        self.currentFunds = 0
        self.eventPicURL = ""
        self.associatedMember = ""
        self.eventType = ""
    }
    
    init(eventID: String, title: String, eventDate: String, eventDescription: String, fundGoal: Double, currentFunds: Double, associatedMember: String, eventType: String) {
        self.eventID = eventID
        self.title = title
        self.eventDate = eventDate
        self.eventDescription = eventDescription
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = ""
        self.associatedMember = associatedMember
        self.eventType = eventType
    }
    
    init(eventID: String, title: String, eventDate: String, eventDescription: String, fundGoal: Double, currentFunds: Double, eventPicURL: String, associatedMember: String, eventType: String) {
        self.eventID = eventID
        self.title = title
        self.eventDate = eventDate
        self.eventDescription = eventDescription
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = eventPicURL
        self.associatedMember = associatedMember
        self.eventType = eventType
    }
    
    //TODO: revise with updated keys when Richard is done (most likely drop capitals)
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
        guard let eventDescription = json["eventDescription"] as? String else {
            throw SerializationError.missing("eventDescription")
        }
        guard let fundGoal = json["FundGoal"] as? Double else {
            throw SerializationError.missing("FundGoal")
        }
        guard let currentFunds = json["CurrentFunds"] as? Double else {
            throw SerializationError.missing("CurrentFunds")
        }
        guard let associatedMember = json["AssociatedMember"] as? String else {
            throw SerializationError.missing("AssociatedMember")
        }
        guard let eventType = json["EventType"] as? String else {
            throw SerializationError.missing("EventType")
        }
        
        self.eventID = eventID
        self.title = title
        self.eventDate = eventDate
        self.eventDescription = eventDescription
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = ""
        self.associatedMember = associatedMember
        self.eventType = eventType
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
    
    func getEventDescription() -> String {
        return self.eventDescription
    }
    
    func setEventDescription(eventDescription: String) {
        self.eventDescription = eventDescription
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
    
    func getAssociatedMember() -> String {
        return associatedMember
    }
    
    func setAssociatedMember(associatedMember: String) {
        self.associatedMember = associatedMember
    }
    
    func getEventType() -> String {
        return eventType
    }
    
    func setEventType(eventType: String) {
        self.eventType = eventType
    }
    
    func toJSON() -> Data {
        //        let dict = ["EventID": self.eventID,
        //                    "Title": self.title,
        //                    "EventDate": self.eventDate,
        //                    "eventDescription": self.eventDescription,
        //                    "FundGoal": self.fundGoal,
        //                    "CurrentFunds": self.currentFunds,
        //                    "AssociatedMember": self.memberString,
        //                    "EventType": self.eventType]
        let encoder = JSONEncoder()
        let jsonData = try? encoder.encode(self) //change to dict if this contains too much data
        return jsonData!
    }
    
    enum SerializationError: Error {
        case missing(String)
        case invalid(String, Any)
    }
}
