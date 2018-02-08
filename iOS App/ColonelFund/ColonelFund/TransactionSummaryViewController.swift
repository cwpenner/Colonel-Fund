//
//  TransactionSummaryViewController.swift
//  ColonelFund
//
//  Created by Chris Penner on 1/18/18.
//  Copyright © 2018 PennerTech. All rights reserved.
//

import UIKit

class TransactionSummaryViewController: UIViewController {
    
    //MARK: Properties
    @IBOutlet var donationRecipientLabel: UILabel!
    @IBOutlet var donationAmountLabel: UILabel!
    @IBOutlet var donationMethodImageView: UIImageView!
    @IBOutlet var donationMethodLabel: UILabel!
    @IBOutlet var transactionIDLabel: UILabel!
    
    var tempNameText: String = ""
    var tempEventText: String = ""
    var tempAmountText: String = ""
    var tempPaymentMethodImage: UIImage?
    var tempPaymentDescriptionText: String = ""
    var tempTransactionIDText: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        donationRecipientLabel.text = tempNameText
        donationAmountLabel.text = tempAmountText
        donationMethodImageView.image = tempPaymentMethodImage!
        donationMethodLabel.text = tempPaymentDescriptionText
        transactionIDLabel.text = tempTransactionIDText
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func continueButtonPressed(_ sender: Any) {
        self.performSegue(withIdentifier: "ShowMainScreen", sender: self)
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
