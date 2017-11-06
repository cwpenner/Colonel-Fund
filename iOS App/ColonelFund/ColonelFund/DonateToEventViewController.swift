//
//  DonateToEventViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class DonateToEventViewController: UIViewController {

    //MARK: Properties
    @IBOutlet weak var eventTitleLabel: UILabel!
    @IBOutlet weak var eventDateLabel: UILabel!
    @IBOutlet weak var eventMemberLabel: UILabel!
    @IBOutlet weak var eventFundGoalLabel: UILabel!
    @IBOutlet weak var eventCurrentFundsLabel: UILabel!
    @IBOutlet weak var eventDonationTextField: UITextField!
    
    var tempTitleText: String = ""
    var tempDateText: String = "218-07-04"
    var tempMemberText: String = "John Smith"
    var tempFundGoalText: String = "$1,000.00"
    var tempCurrentFundsText: String = "$75.63"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        eventTitleLabel.text = tempTitleText
        eventDateLabel.text = tempDateText
        eventMemberLabel.text = tempMemberText
        eventFundGoalLabel.text = tempFundGoalText
        eventCurrentFundsLabel.text = tempCurrentFundsText
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
