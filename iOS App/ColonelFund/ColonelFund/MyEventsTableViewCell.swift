//
//  MyEventsTableViewCell.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/30/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import UIKit

class MyEventsTableViewCell: UITableViewCell {
    
    //MARK: Properties
    @IBOutlet var nameLabel: UILabel!
    @IBOutlet var memberLabel: UILabel!
    @IBOutlet var eventIconImageView: UIImageView!
    @IBOutlet var progressBar: UIProgressView!
    @IBOutlet var dayLabel: UILabel!
    @IBOutlet var monthLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
