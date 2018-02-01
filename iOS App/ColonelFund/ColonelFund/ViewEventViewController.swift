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
    
    var event: Event = Event(eventID: "temp")
    
    override func viewDidLoad() {
        super.viewDidLoad()

        eventTitleLabel.text = event.getTitle()
        eventDateLabel.text = event.getEventDate()
        eventMemberLabel.text = event.getAssociatedMember().getFormattedFullName()
        eventFundGoalLabel.text = "$" + String(event.getFundGoal())
        eventCurrentFundsLabel.text = "$" + String(event.getCurrentFunds())
        eventDescriptionLabel.text = event.getDescription()
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
            
            donateToEventViewController.event = event
            
        default:
            fatalError("Unexpected Segue Identifier: \(segue.identifier)")
        }
    }

}
