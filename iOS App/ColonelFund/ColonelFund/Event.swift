//
//  Event.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

class Event: NSObject, Codable {
    
    private var title: String
    private var eventDate: String
    private var eventDescription: String
    private var fundGoal: Double
    private var currentFunds: Double
    private var eventPicURL: String
    private var associatedMember: String
    private var eventType: String
    
    init(title: String) {
        self.title = title
        self.eventDate = ""
        self.eventDescription = ""
        self.fundGoal = 0
        self.currentFunds = 0
        self.eventPicURL = ""
        self.associatedMember = ""
        self.eventType = ""
    }
    
    init(title: String, eventDate: String, eventDescription: String, fundGoal: Double, currentFunds: Double, associatedMember: String, eventType: String) {
        self.title = title
        self.eventDate = eventDate
        self.eventDescription = eventDescription
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = ""
        self.associatedMember = associatedMember
        self.eventType = eventType
    }
    
    init(title: String, eventDate: String, eventDescription: String, fundGoal: Double, currentFunds: Double, eventPicURL: String, associatedMember: String, eventType: String) {
        self.title = title
        self.eventDate = eventDate
        self.eventDescription = eventDescription
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = eventPicURL
        self.associatedMember = associatedMember
        self.eventType = eventType
    }
    
    init(json: [String: Any]) throws {
        guard let title = json["title"] as? String else {
            throw SerializationError.missing("title")
        }
        guard let eventDate = json["eventDate"] as? String else {
            throw SerializationError.missing("eventDate")
        }
        guard let eventDescription = json["description"] as? String else {
            throw SerializationError.missing("description")
        }
        guard let fundGoal = json["fundGoal"] as? Double else {
            throw SerializationError.missing("fundGoal")
        }
        guard let currentFunds = json["currentFunds"] as? Double else {
            throw SerializationError.missing("currentFunds")
        }
        guard let associatedMember = json["associatedMember"] as? String else {
            throw SerializationError.missing("associatedMember")
        }
        guard let eventType = json["type"] as? String else {
            throw SerializationError.missing("type")
        }
        
        self.title = title
        self.eventDate = eventDate
        self.eventDescription = eventDescription
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = ""
        self.associatedMember = associatedMember
        self.eventType = eventType
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
        //        let dict = ["title": self.title,
        //                    "eventDate": self.eventDate,
        //                    "description": self.eventDescription,
        //                    "fundGoal": self.fundGoal,
        //                    "currentFunds": self.currentFunds,
        //                    "associatedMember": self.memberString,
        //                    "type": self.eventType]
        let encoder = JSONEncoder()
        let jsonData = try? encoder.encode(self) //change to dict if this contains too much data
        return jsonData!
    }
    
    enum SerializationError: Error {
        case missing(String)
        case invalid(String, Any)
    }
}
