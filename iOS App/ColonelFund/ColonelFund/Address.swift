//
//  Address.swift
//  ColonelFund
//
//  Created by Chris Penner on 3/26/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import Foundation

class Address: Codable {
    private var addressLine1: String
    private var addressLine2: String
    private var city: String
    private var state: String
    private var zipCode: String
    
    init() {
        self.addressLine1 = ""
        self.addressLine2 = ""
        self.city = ""
        self.state = ""
        self.zipCode = ""
    }
    
    init(addressLine1: String, addressLine2: String, city: String, state: String, zipCode: String) {
        self.addressLine1 = addressLine1
        self.addressLine2 = addressLine2
        self.city = city
        self.state = state
        self.zipCode = zipCode
    }
    
    required init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        addressLine1 = try values.decode(String.self, forKey: .addressLine1)
        addressLine2 = try values.decode(String.self, forKey: .addressLine2)
        city = try values.decode(String.self, forKey: .city)
        state = try values.decode(String.self, forKey: .state)
        zipCode = try values.decode(String.self, forKey: .zipCode)
    }
    
    func toString() -> String {
        if addressLine2 != "" {
            return "\(addressLine1)\n\(addressLine2)\n\(city) \(state) \(zipCode)"
        } else {
            return "\(addressLine1)\n\(city) \(state) \(zipCode)"
        }
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(addressLine1, forKey: .addressLine1)
        try container.encode(addressLine2, forKey: .addressLine2)
        try container.encode(city, forKey: .city)
        try container.encode(state, forKey: .state)
        try container.encode(zipCode, forKey: .zipCode)
    }
    
    enum CodingKeys: String, CodingKey {
        case addressLine1
        case addressLine2
        case city
        case state
        case zipCode
    }
}
