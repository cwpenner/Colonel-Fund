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
    @IBOutlet weak var memberDonateButton: UIBarButtonItem!
    @IBOutlet weak var memberPaymentDescriptionLabel: UILabel!
    @IBOutlet weak var memberSelectPaymentButton: UIButton!
    @IBOutlet weak var memberPaymentImageView: UIImageView!
    
    var donateMember: Member! = nil
    
    override func viewDidLoad() {
        nameLabel.text = donateMember.getFormattedFullName()
        usernameLabel.text = donateMember.getUserName()
        
        BraintreeViewController(donationTextField: memberDonationTextField, donateButton: memberDonateButton, paymentDescriptionLabel: memberPaymentDescriptionLabel, selectPaymentButton: memberSelectPaymentButton, paymentIconView: memberPaymentImageView, donationType: donateMember)

        super.viewDidLoad()
        
        if member.getProfilePicURL().isEmpty {
            placeholderProfilePic(member: member)
        } else {
            loadProfilePicFromURL(url: member.getProfilePicURL())
        }
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
    
    func placeholderProfilePic(member: Member) {
        let placeholder = UILabel()
        placeholder.frame.size = CGSize(width: 100.0, height: 100.0)
        placeholder.textColor = UIColor.white
        placeholder.font = UIFont.boldSystemFont(ofSize: 40)
        placeholder.text = String(describing: member.getFirstName().first!) + String(describing: member.getLastName().first!)
        placeholder.textAlignment = NSTextAlignment.center
        placeholder.backgroundColor = UIColor.darkGray
        placeholder.layer.cornerRadius = 50.0
        placeholder.layer.masksToBounds = true
        
        UIGraphicsBeginImageContext(placeholder.frame.size)
        placeholder.layer.render(in: UIGraphicsGetCurrentContext()!)
        profilePicImageView.image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
    }
    
    func loadProfilePicFromURL(url: String) {
        let imageURL = URL(string: url)
        do {
            let imageData = try Data(contentsOf: imageURL!)
            profilePicImageView.image = UIImage(data: imageData)
            profilePicImageView.layer.cornerRadius = 50.0
            profilePicImageView.layer.masksToBounds = true
        } catch {
            print("Error processing profile pic: \(error.localizedDescription)")
        }
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
            
            transactionSummaryViewController.tempNameText = "Member: " + nameLabel.text!
            transactionSummaryViewController.tempAmountText = "$" + memberDonationTextField.text!
            transactionSummaryViewController.tempPaymentDescriptionText = memberPaymentDescriptionLabel.text!
            transactionSummaryViewController.tempPaymentMethodImage = memberPaymentImageView.image!
            transactionSummaryViewController.tempTransactionIDText = "" //TODO: Update with transaction ID
            
        default:
            fatalError("Unexpected Segue Identifier: \(String(describing: segue.identifier))")
        }
    }
 

}
