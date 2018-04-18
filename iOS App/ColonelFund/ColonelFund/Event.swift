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
    private var eventPicData: String
    private var associatedMember: String
    private var eventType: String
    private var eventTime: String
    private var address: Address
    private var associatedEmail: String //delete
    
    init(title: String) {
        self.title = title
        self.eventDate = ""
        self.eventDescription = ""
        self.fundGoal = 0
        self.currentFunds = 0
        self.eventPicURL = ""
        self.eventPicData = "default"
        self.associatedMember = ""
        self.eventType = ""
        self.eventTime = ""
        self.address = Address()
        self.associatedEmail = "" //delete
    }
    
    init(title: String, eventDate: String, eventDescription: String, fundGoal: Double, currentFunds: Double, associatedMember: String, eventType: String) {
        self.title = title
        self.eventDate = eventDate
        self.eventDescription = eventDescription
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = ""
        self.eventPicData = "default"
        self.associatedMember = associatedMember
        self.eventType = eventType
        self.eventTime = ""
        self.address = Address()
        self.associatedEmail = "" //delete
    }
    
    init(title: String, eventDate: String, eventDescription: String, fundGoal: Double, currentFunds: Double, eventPicURL: String, associatedMember: String, eventType: String) {
        self.title = title
        self.eventDate = eventDate
        self.eventDescription = eventDescription
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = eventPicURL
        self.eventPicData = "default"
        self.associatedMember = associatedMember
        self.eventType = eventType
        self.eventTime = ""
        self.address = Address()
        self.associatedEmail = "" //delete
    }
    
    init(title: String, eventDate: String, eventDescription: String, fundGoal: Double, currentFunds: Double, eventPicData: String, associatedMember: String, eventType: String, eventTime: String, address: Address) {
        self.title = title
        self.eventDate = eventDate
        self.eventDescription = eventDescription
        self.fundGoal = fundGoal
        self.currentFunds = currentFunds
        self.eventPicURL = ""
        self.eventPicData = eventPicData
        self.associatedMember = associatedMember
        self.eventType = eventType
        self.eventTime = eventTime
        self.address = address
        self.associatedEmail = "" //delete
    }
    
    required init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        title = try values.decode(String.self, forKey: .title)
        eventDate = try values.decode(String.self, forKey: .eventDate)
        eventDescription = try values.decode(String.self, forKey: .eventDescription)
        fundGoal = (try Double(values.decode(String.self, forKey: .fundGoal)))!
        currentFunds = (try Double(values.decode(String.self, forKey: .currentFunds)))!
        eventPicURL = try values.decode(String.self, forKey: .eventPicURL)
        associatedMember = try values.decode(String.self, forKey: .associatedMember)
        eventType = try values.decode(String.self, forKey: .eventType)
        associatedEmail = try values.decode(String.self, forKey: .associatedEmail) //delete
        self.eventPicData = "default"
        //TODO: fix below once database updated with eventTime and address
        self.eventTime = ""
        self.address = Address()
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
    
    func getEventTime() -> String {
        return eventTime
    }
    
    func setEventTime(eventTime: String) {
        self.eventTime = eventTime
    }
    
    func getAddress() -> Address {
        return address
    }
    
    func setAddress(address: Address) {
        self.address = address
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(title, forKey: .title)
        try container.encode(eventDate, forKey: .eventDate)
        try container.encode(eventDescription, forKey: .eventDescription)
        try container.encode(String(fundGoal), forKey: .fundGoal)
        try container.encode(String(currentFunds), forKey: .currentFunds)
        try container.encode(eventPicData, forKey: .eventPicData)
        try container.encode(associatedMember, forKey: .associatedMember)
        try container.encode(eventType, forKey: .eventType)
        //TODO: uncomment below once database updated with eventTime and address
//        try container.encode(eventTime, forKey: .eventTime)
//        try container.encode(address, forKey: .address)
        //try container.encode(associatedEmail, forKey: .associatedEmail) //delete
    }
    
    func toFormEncoded() -> String {
        return "title=\(self.title)&associatedMember=\(self.associatedMember)&eventDate=\(self.eventDate)&fundGoal=\(self.fundGoal)&currentFunds=\(self.currentFunds)&description=\(self.eventDescription)&type=\(self.eventType)&image=\(self.eventPicData)"
        //TODO: uncomment below once database updated with eventTime and address
//        return "title=\(self.title)&associatedMember=\(self.associatedMember)&eventDate=\(self.eventDate)&fundGoal=\(self.fundGoal)&currentFunds=\(self.currentFunds)&description=\(self.eventDescription)&type=\(self.eventType)&image=\(self.eventPicData)&eventTime=\(self.eventTime)&address=\(self.address.toString())"
    }
    
    enum CodingKeys: String, CodingKey {
        case title
        case eventDate
        case eventDescription = "description"
        case fundGoal
        case currentFunds
        case eventPicURL = "imageURL"
        case eventPicData = "image"
        case associatedMember
        case eventType = "type"
        case eventTime
        case address
        case associatedEmail //delete
    }
}
