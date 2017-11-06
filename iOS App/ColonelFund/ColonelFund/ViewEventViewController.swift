//
//  ViewEventViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class ViewEventViewController: UIViewController {
    
    //MARK: Properties
    @IBOutlet weak var eventImageView: UIImageView!
    @IBOutlet weak var eventTitleLabel: UILabel!
    @IBOutlet weak var eventDateLabel: UILabel!
    @IBOutlet weak var eventMemberLabel: UILabel!
    @IBOutlet weak var eventFundGoalLabel: UILabel!
    @IBOutlet weak var eventCurrentFundsLabel: UILabel!
    @IBOutlet weak var eventDescriptionLabel: UILabel!
    
    var tempTitleText: String = ""
    var tempDateText: String = "218-07-04"
    var tempMemberText: String = "John Smith"
    var tempFundGoalText: String = "$1,000.00"
    var tempCurrentFundsText: String = "$75.63"
    var tempDescriptionText: String = "This will be a fun event for everyone to get together and enjoy some delicious food and great company!"
    
    override func viewDidLoad() {
        super.viewDidLoad()

        eventTitleLabel.text = tempTitleText
        eventDateLabel.text = tempDateText
        eventMemberLabel.text = tempMemberText
        eventFundGoalLabel.text = tempFundGoalText
        eventCurrentFundsLabel.text = tempCurrentFundsText
        eventDescriptionLabel.text = tempDescriptionText
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowDonateToEvent":
            guard let donateToEventViewController = segue.destination as? DonateToEventViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            donateToEventViewController.tempTitleText = eventTitleLabel.text!
            donateToEventViewController.tempDateText = eventDateLabel.text!
            donateToEventViewController.tempMemberText = eventMemberLabel.text!
            donateToEventViewController.tempFundGoalText = eventFundGoalLabel.text!
            donateToEventViewController.tempCurrentFundsText = eventCurrentFundsLabel.text!
            
        default:
            fatalError("Unexpected Segue Identifier: \(segue.identifier)")
        }
    }

}
