//
//  AssociatedEventsTableViewCell.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/22/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class AssociatedEventsTableViewCell: UITableViewCell {
    
    //MARK: Properties
    @IBOutlet weak var eventNameLabel: UILabel!
    
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
