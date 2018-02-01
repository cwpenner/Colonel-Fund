//
//  User.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/31/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

class User {
    static var currentUser: Member = Member(userID: "temp")
    
    static func getCurrentUser() -> Member {
        return self.currentUser
    }
    
    static func setCurrentUser(currentUser: Member) {
        self.currentUser = currentUser
    }
    
    static func logout() {
        self.currentUser = Member(userID: "temp")
    }
}
