//
//  DonateToMemberViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 11/5/17.
//  Copyright © 2017 PennerTech. All rights reserved.
//

import UIKit

class DonateToMemberViewController: BraintreeViewController {
    
    
    //MARK: Properties
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var memberDonationTextField: UITextField!
    @IBOutlet weak var profilePicImageView: UIImageView!
    @IBOutlet weak var memberDonateButton: UIButton!
    @IBOutlet weak var memberPaymentDescriptionLabel: UILabel!
    @IBOutlet weak var memberSelectPaymentButton: UIButton!
    @IBOutlet weak var memberPaymentImageView: UIImageView!
    
    var tempNameText: String = ""
    var tempUsernameText: String = ""
    
    override func viewDidLoad() {
        nameLabel.text = tempNameText
        usernameLabel.text = tempUsernameText
        
        BraintreeViewController(donationTextField: memberDonationTextField, donateButton: memberDonateButton, paymentDescriptionLabel: memberPaymentDescriptionLabel, selectPaymentButton: memberSelectPaymentButton, paymentIconView: memberPaymentImageView)
        super.memberName = nameLabel.text!

        super.viewDidLoad()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func memberSelectPaymentMethodButtonPressed(_ sender: Any) {
        super.selectPaymentButtonPressed()
    }
    
    @IBAction func memberDonateButtonPressed(_ sender: Any) {
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
