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
    @IBOutlet weak var eventDonateButton: UIBarButtonItem!
    @IBOutlet weak var eventPaymentDescriptionLabel: UILabel!
    @IBOutlet weak var eventSelectPaymentButton: UIButton!
    @IBOutlet weak var eventPaymentImageView: UIImageView!
    
    var donateEvent: Event! = nil
    var tempMemberText = ""
    
    override func viewDidLoad() {
        eventTitleLabel.text = donateEvent.getTitle()
        eventDateLabel.text = donateEvent.getEventDate()
        eventMemberLabel.text = tempMemberText
        eventFundGoalLabel.text = "$" + String(donateEvent.getFundGoal())
        eventCurrentFundsLabel.text = "$" + String(donateEvent.getCurrentFunds())
        
        BraintreeViewController(donationTextField: eventDonationTextField, donateButton: eventDonateButton, paymentDescriptionLabel: eventPaymentDescriptionLabel, selectPaymentButton: eventSelectPaymentButton, paymentIconView: eventPaymentImageView, donationType: donateEvent)
        
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

    
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch(segue.identifier ?? "") {
        case "ShowTransactionSummary":
            guard let transactionSummaryViewController = segue.destination as? TransactionSummaryViewController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            transactionSummaryViewController.tempNameText = "Event: " + eventTitleLabel.text!
            transactionSummaryViewController.tempAmountText = "$" + eventDonationTextField.text!
            transactionSummaryViewController.tempPaymentDescriptionText = eventPaymentDescriptionLabel.text!
            transactionSummaryViewController.tempPaymentMethodImage = eventPaymentImageView.image!
            transactionSummaryViewController.tempTransactionIDText = "" //TODO: Update with transaction ID
            
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }

}
