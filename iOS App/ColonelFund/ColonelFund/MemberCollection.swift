//
//  MemberCollection.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/31/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

protocol MemberCollectionProtocol {
    func memberDataDownloaded()
}

class MemberCollection: NSObject, URLSessionDelegate {
    var delegate: MemberCollectionProtocol!
    let jsonFileName = "members"
    let URL_FOR_NAMES = "https://wesll.com/colonelfund/members.php"
    var memberMap: [String: Member] = [:]
    var memberArray: [Member] = []
    
    override init() {
        super.init()
        updateFromRemote()
    }
    
    func updateFromRemote() {
        let url = URL(string: URL_FOR_NAMES)!
        let defaultSession = Foundation.URLSession(configuration: URLSessionConfiguration.default)
        var httpStatus = 0
        let task = defaultSession.dataTask(with: url) { (data, response, error) in
            let res = response as? HTTPURLResponse
            httpStatus = res!.statusCode
            if (error != nil) {
                print("Error downloading remote member data. Error code: \(String(describing: error))")
            } else {
                if (httpStatus == 200) {
                    print("Remote member data downloaded successfully")
                    self.processData(data: data!)
                } else {
                    print("Error downloading remote member data. Server response: \(httpStatus)")
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
                print("Failed saving member json file: " + error.localizedDescription)
            }
        }
        return false
    }
    
    func restoreFromFile(fileName: String) -> Bool {
        let dir = try? FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
        if let fileURL = dir?.appendingPathComponent(fileName).appendingPathExtension("json") {
            print("Member Collection found under: \(fileName).json")
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
                        let userID = key
                        let firstName = value["firstName"] as! String
                        let lastName = value["lastName"] as! String
                        let userName = firstName.lowercased() + lastName.lowercased()
                        let emailAddress = value["emailAddress"] as! String
                        let phoneNumber = value["phoneNumber"] as! String
                        let newMember = Member(userID: userID, firstName: firstName, lastName: lastName, userName: userName, emailAddress: emailAddress, phoneNumber: phoneNumber)
                        memberMap[userID.lowercased()] = newMember
                    }
                } else if let jsonArray = object as? [AnyObject] {
                    for item in jsonArray {
                        let newMember = try Member(json: item as! [String : AnyObject])
                        memberMap[newMember.getUserID()] = newMember
                    }
                }
                DispatchQueue.main.async(execute: { () -> Void in
                    self.delegate.memberDataDownloaded()
                })
                return true
            } catch {
                print("Error! Unable to parse \(fileName).json")
                DispatchQueue.main.async(execute: { () -> Void in
                    self.delegate.memberDataDownloaded()
                })
            }
        }
        return false
    }
    
    func count() -> Int {
        return memberMap.count
    }
    
    func getMembers() -> [Member] {
        memberArray.removeAll()
        for (key, value) in memberMap {
            memberArray.append(value)
        }
        memberArray = memberArray.sorted(by: {$0.getLastName() < $1.getLastName()})
        return memberArray
    }
    
    func getMember(userID: String) -> Member? {
        let keyExists = memberMap[userID.lowercased()] != nil
        if keyExists {
            return memberMap[userID.lowercased()]!
        } else {
            print("Member: \(userID) does not exist")
            return nil
        }
    }
}
