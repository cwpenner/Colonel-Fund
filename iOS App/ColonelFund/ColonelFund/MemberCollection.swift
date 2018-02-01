//
//  MemberCollection.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/31/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

class MemberCollection {
    let jsonFileName = "members"
    let URL_FOR_NAMES = "https://wesll.com/colonelfund/members.php"
    var memberMap: [String: Member] = [:]
    var memberArray: [Member] = []
    
    init() {
        restoreFromFile(fileName: jsonFileName, fileType: "json")
//        for (key, value) in memberMap {
//            print("Key: \(key), Value: \(value)")
//        }
    }
    
    func updateFromRemote() {
        
    }
    
    func restoreFromFile(fileName: String, fileType: String) -> Bool {
        if let url = Bundle.main.url(forResource: fileName, withExtension: fileType) {
            print("Member Library collection found under: \(fileName).\(fileType)")
            do {
                let data = try Data(contentsOf: url)
                let object = try JSONSerialization.jsonObject(with: data, options: .allowFragments)
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
                }
                return true
            } catch {
                print("Error! Unable to parse  \(fileName).json")
            }
        }
        return false
    }
    
    func count() -> Int {
        return memberMap.count
    }
    
    func getMembers() -> [Member] {
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
