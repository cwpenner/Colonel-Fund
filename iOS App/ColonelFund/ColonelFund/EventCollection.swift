//
//  EventCollection.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/31/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

protocol EventCollectionProtocol {
    func eventDataDownloaded()
}

class EventCollection: NSObject, URLSessionDelegate {
    var delegate: EventCollectionProtocol!
    let jsonFileName = "events"
    let URL_FOR_EVENTS = "https://wesll.com/colonelfund/events.php"
    var eventMap: [String: Event] = [:]
    var eventArray: [Event] = []
    
    override init() {
        super.init()
        updateFromRemote()
    }
    
    func updateFromRemote() {
        let url = URL(string: URL_FOR_EVENTS)!
        let defaultSession = Foundation.URLSession(configuration: URLSessionConfiguration.default)
        var httpStatus = 0
        let task = defaultSession.dataTask(with: url) { (data, response, error) in
            let res = response as? HTTPURLResponse
            httpStatus = res!.statusCode
            if (error != nil) {
                print("Error downloading remote event data. Error code: \(String(describing: error))")
            } else {
                if (httpStatus == 200) {
                    print("Remote event data downloaded successfully")
                    self.processData(data: data!)
                } else {
                    print("Error downloading remote event data. Server response: \(httpStatus)")
                    self.restoreFromFile(fileName: self.jsonFileName)
                }
            }
        }
        task.resume()
    }
    
    func processData(data: Data) {
        self.saveJSONLocal(jsonData: data, fileName: self.jsonFileName)
        self.restoreFromFile(fileName: self.jsonFileName)
    }
    
    func saveJSONLocal(jsonData: Data, fileName: String) -> Bool {
        let dir = try? FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
        if let fileURL = dir?.appendingPathComponent(fileName).appendingPathExtension("json") {
            
            do {
                try jsonData.write(to: fileURL, options: .atomic)
                print("Successfully saved local json file: \(fileName).json")
                return true
            } catch {
                print("Failed saving event json file: " + error.localizedDescription)
            }
        }
        return false
    }
    
    func restoreFromFile(fileName: String) -> Bool {
        let dir = try? FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
        if let fileURL = dir?.appendingPathComponent(fileName).appendingPathExtension("json") {
            print("Event Collection found under: \(fileName).json")
            do {
                let jsonData: Data?
                do {
                    jsonData = try Data(contentsOf: fileURL)
                } catch {
                    let url = Bundle.main.url(forResource: fileName, withExtension: "json")
                    jsonData = try Data(contentsOf: url!)
                    saveJSONLocal(jsonData: jsonData!, fileName: fileName)
                }
                let object = try JSONSerialization.jsonObject(with: jsonData!, options: .allowFragments)
                if let dict = object as? [String: AnyObject] {
                    for (key, value) in dict {
                        let eventID = key
                        let title = value["Title"] as! String
                        let eventDate = value["EventDate"] as! String
                        let eventDescription = value["Description"] as! String
                        let fundGoal = value["FundGoal"] as! Double
                        let currentFunds = value["CurrentFunds"] as! Double
                        let associatedMember = value["AssociatedMember"] as! String
                        let newEvent = Event(eventID: eventID, title: title, eventDate: eventDate, eventDescription: eventDescription, fundGoal: fundGoal, currentFunds: currentFunds, associatedMember: associatedMember)
                        eventMap[eventID.lowercased()] = newEvent
                    }
                } else if let jsonArray = object as? [AnyObject] {
                    for item in jsonArray {
                        let newEvent = try Event(json: item as! [String : AnyObject])
                        eventMap[newEvent.getEventID()] = newEvent
                    }
                }
                DispatchQueue.main.async(execute: { () -> Void in
                    self.delegate.eventDataDownloaded()
                })
                return true
            } catch {
                print("Error! Unable to parse \(fileName).json")
                DispatchQueue.main.async(execute: { () -> Void in
                    self.delegate.eventDataDownloaded()
                })
            }
        }
        return false
    }
    
    func count() -> Int {
        return eventMap.count
    }
    
    func getEvents() -> [Event] {
        eventArray.removeAll()
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
