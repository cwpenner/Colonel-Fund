//
//  DonateToEventViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit
import BraintreeDropIn
import Braintree

class DonateToEventViewController: BraintreeViewController {

    //MARK: Properties
    @IBOutlet weak var eventTitleLabel: UILabel!
    @IBOutlet weak var eventDateLabel: UILabel!
    @IBOutlet weak var eventMemberLabel: UILabel!
    @IBOutlet weak var eventFundGoalLabel: UILabel!
    @IBOutlet weak var eventCurrentFundsLabel: UILabel!
    @IBOutlet weak var eventDonationTextField: UITextField!
    @IBOutlet weak var eventDonateButton: UIButton!
    @IBOutlet weak var eventPaymentDescriptionLabel: UILabel!
    @IBOutlet weak var eventSelectPaymentButton: UIButton!
    @IBOutlet weak var eventPaymentImageView: UIImageView!
    
    var tempTitleText: String = ""
    var tempDateText: String = "2018-07-04"
    var tempMemberText: String = "John Smith"
    var tempFundGoalText: String = "$1,000.00"
    var tempCurrentFundsText: String = "$75.63"
    
    override func viewDidLoad() {
        eventTitleLabel.text = tempTitleText
        eventDateLabel.text = tempDateText
        eventMemberLabel.text = tempMemberText
        eventFundGoalLabel.text = tempFundGoalText
        eventCurrentFundsLabel.text = tempCurrentFundsText
        
        BraintreeViewController(donationTextField: eventDonationTextField, donateButton: eventDonateButton, paymentDescriptionLabel: eventPaymentDescriptionLabel, selectPaymentButton: eventSelectPaymentButton, paymentIconView: eventPaymentImageView)
        setEventTitle(newEventTitle: eventTitleLabel.text!)
        
        super.viewDidLoad()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    @IBAction func eventSelectPaymentMethodButtonPressed(_ sender: Any) {
        super.selectPaymentButtonPressed()
    }
    
    @IBAction func eventDonateButtonPressed(_ sender: Any) {
        super.donateButtonPressed()
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
