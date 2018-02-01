//
//  EventCollection.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/31/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

class EventCollection {
    let jsonFileName = "events"
    let URL_FOR_NAMES = "https://wesll.com/colonelfund/events.php"
    var eventMap: [String: Event] = [:]
    var eventArray: [Event] = []
    
    init() {
        restoreFromFile(fileName: jsonFileName, fileType: "json")
//        for (key, value) in eventMap {
//            print("Key: \(key), Value: \(value)")
//        }
    }
    
    func updateFromRemote() {
        
    }
    
    func restoreFromFile(fileName: String, fileType: String) -> Bool {
        if let url = Bundle.main.url(forResource: fileName, withExtension: fileType) {
            print("Event Library collection found under: \(fileName).\(fileType)")
            do {
                let data = try Data(contentsOf: url)
                let object = try JSONSerialization.jsonObject(with: data, options: .allowFragments)
                if let dict = object as? [String: AnyObject] {
                    for (key, value) in dict {
                        let eventID = key
                        let title = value["Title"] as! String
                        let eventDate = value["EventDate"] as! String
                        let description = value["Description"] as! String
                        let fundGoal = value["FundGoal"] as! Double
                        let currentFunds = value["CurrentFunds"] as! Double
                        let memberName = value["AssociatedMember"] as! String
                        let mc = MemberCollection()
                        let associatedMember = mc.getMember(userID: memberName)
                        let newEvent = Event(eventID: eventID, title: title, eventDate: eventDate, description: description, fundGoal: fundGoal, currentFunds: currentFunds, associatedMember: associatedMember!)
                        eventMap[eventID.lowercased()] = newEvent
                    }
                }
                return true
            } catch {
                print("Error! Unable to parse  \(fileName).json")
            }
        }
        return false
    }
    
    func count() -> Int {
        return eventMap.count
    }
    
    func getEvents() -> [Event] {
        for (key, value) in eventMap {
            eventArray.append(value)
        }
        eventArray = eventArray.sorted(by: {$0.getEventDate() < $1.getEventDate()})
        return eventArray
    }
    
    func getEvent(eventID: String) -> Event? {
        let keyExists = eventMap[eventID.lowercased()] != nil
        if keyExists {
            return eventMap[eventID.lowercased()]!
        } else {
            print("Event: \(eventID) does not exist")
            return nil
        }
    }
}
